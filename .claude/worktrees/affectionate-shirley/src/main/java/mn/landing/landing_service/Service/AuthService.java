package mn.landing.landing_service.Service;

import mn.landing.landing_service.Entity.*;
import mn.landing.landing_service.Model.*;
import mn.landing.landing_service.Repository.*;
import mn.landing.landing_service.Security.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MailService mailService;
    private final JwtService jwtService;
    private final PasswordEncoder encoder;

    @Value("${app.jwt.refresh-exp-days:14}")
    private long refreshExpDays;

    @Value("${app.frontend.reset-password-url:http://localhost:5173/reset-password}")
    private String resetPasswordUrl;

    public AuthService(UserRepository userRepository,
                       PasswordResetTokenRepository tokenRepository,
                       RefreshTokenRepository refreshTokenRepository,
                       PasswordEncoder encoder,
                       MailService mailService,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.encoder = encoder;
        this.mailService = mailService;
        this.jwtService = jwtService;
    }

    /* ---------- LOGIN (JWT + REFRESH) ---------- */
    public LoginResponse login(LoginRequest req) {
        User user = userRepository.findByEmail(req.username)
                .or(() -> userRepository.findByPhone(req.username))
                .orElseThrow(() -> new RuntimeException("USER_NOT_FOUND"));

        if (!encoder.matches(req.password, user.getPasswordHash())) {
            throw new RuntimeException("INVALID_PASSWORD");
        }

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = createAndStoreRefreshToken(user); // raw token (client-д буцаана)

        return new LoginResponse(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getRole(),
                accessToken,
                refreshToken
        );
    }

    /* ---------- REFRESH (Rotate) ---------- */
    public RefreshTokenResponse refresh(RefreshTokenRequest req) {
        if (req.refreshToken == null || req.refreshToken.isBlank()) {
            throw new RuntimeException("REFRESH_TOKEN_REQUIRED");
        }

        String tokenHash = sha256Hex(req.refreshToken);

        RefreshToken stored = refreshTokenRepository.findByTokenHash(tokenHash)
                .orElseThrow(() -> new RuntimeException("INVALID_REFRESH_TOKEN"));

        if (stored.getRevokedAt() != null) throw new RuntimeException("REFRESH_TOKEN_REVOKED");
        if (stored.getExpiresAt().isBefore(LocalDateTime.now())) throw new RuntimeException("REFRESH_TOKEN_EXPIRED");

        // rotate: хуучныг revoke
        stored.setRevokedAt(LocalDateTime.now());
        refreshTokenRepository.save(stored);

        User user = stored.getUser();
        String newAccess = jwtService.generateAccessToken(user);
        String newRefresh = createAndStoreRefreshToken(user);

        return new RefreshTokenResponse(newAccess, newRefresh);
    }

    /* ---------- LOGOUT ---------- */
    public void logout(LogoutRequest req) {
        if (req.refreshToken == null || req.refreshToken.isBlank()) return;

        String tokenHash = sha256Hex(req.refreshToken);
        refreshTokenRepository.findByTokenHash(tokenHash).ifPresent(rt -> {
            rt.setRevokedAt(LocalDateTime.now());
            refreshTokenRepository.save(rt);
        });
    }

    /* ===================== helpers ===================== */

    // raw refresh token үүсгээд DB-д зөвхөн hash хадгална
    private String createAndStoreRefreshToken(User user) {
        String rawToken = UUID.randomUUID() + "." + UUID.randomUUID() + "." + UUID.randomUUID();
        String tokenHash = sha256Hex(rawToken);

        RefreshToken rt = new RefreshToken();
        rt.setUser(user);
        rt.setTokenHash(tokenHash);
        rt.setExpiresAt(LocalDateTime.now().plusDays(refreshExpDays));
        rt.setRevokedAt(null);

        refreshTokenRepository.save(rt);
        return rawToken;
    }

    private String sha256Hex(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(bytes.length * 2);
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString(); // 64 hex chars
        } catch (Exception e) {
            throw new RuntimeException("HASH_FAILED");
        }
    }



    public User register(RegisterRequest req) {

        if (req.terms == null || !req.terms) throw new RuntimeException("TERMS_NOT_ACCEPTED");
        if (req.password == null || req.password.length() < 8) throw new RuntimeException("PASSWORD_TOO_SHORT");
        if (!Objects.equals(req.password, req.confirmPassword)) throw new RuntimeException("PASSWORD_NOT_MATCH");

        if (userRepository.existsByEmail(req.email)) throw new RuntimeException("EMAIL_ALREADY_EXISTS");
        if (userRepository.existsByRegisterNumber(req.registerNumber)) throw new RuntimeException("REGISTER_NUMBER_EXISTS");
        if (userRepository.existsByPhone(req.phone)) throw new RuntimeException("PHONE_EXISTS");

        User user = new User();
        user.setFullName(req.fullname);
        user.setEmail(req.email);
        user.setRegisterNumber(req.registerNumber.toUpperCase());
        user.setPhone(req.phone);
        user.setPasswordHash(encoder.encode(req.password));

        return userRepository.save(user);
    }
    /* ---------- FORGOT PASSWORD ---------- */
    public void forgotPassword(ForgotPasswordRequest req) {

        User user = userRepository.findByEmail(req.email)
                .orElseThrow(() -> new RuntimeException("USER_NOT_FOUND"));

        PasswordResetToken token = new PasswordResetToken();
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiresAt(LocalDateTime.now().plusMinutes(30));
        tokenRepository.save(token);

        String resetLink = resetPasswordUrl + "?token=" + token.getToken();

        String html = """
        <html>
          <body style="font-family:Arial, sans-serif">
            <h2>Нууц үг сэргээх</h2>
            <p>Та доорх товч дээр дарж нууц үгээ шинэчилнэ үү.</p>

            <a href="%s"
               style="display:inline-block;
                      padding:10px 16px;
                      background:#2563EB;
                      color:white;
                      text-decoration:none;
                      border-radius:6px;
                      margin-top:12px;">
              Нууц үг сэргээх
            </a>

            <p style="margin-top:16px;font-size:13px;color:#555;">
              Энэ холбоос 30 минутын хугацаанд хүчинтэй.
            </p>

            <p style="font-size:12px;color:#999;">
              Хэрэв та энэ хүсэлтийг илгээгээгүй бол энэ мэйлийг үл тоомсорлоно уу.
            </p>
          </body>
        </html>
        """.formatted(resetLink);

        mailService.sendHtmlMail(user.getEmail(), "Нууц үг сэргээх", html);
    }

    /* ---------- RESET PASSWORD ---------- */
    public void resetPassword(ResetPasswordRequest req) {

        if (!Objects.equals(req.newPassword, req.confirmPassword)) {
            throw new RuntimeException("PASSWORD_NOT_MATCH");
        }

        PasswordResetToken token = tokenRepository.findByToken(req.token)
                .orElseThrow(() -> new RuntimeException("INVALID_TOKEN"));

        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("TOKEN_EXPIRED");
        }

        if (token.getUsedAt() != null) {
            throw new RuntimeException("TOKEN_ALREADY_USED");
        }

        User user = token.getUser();
        user.setPasswordHash(encoder.encode(req.newPassword));
        userRepository.save(user);

        token.setUsedAt(LocalDateTime.now());
        tokenRepository.save(token);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }
}
