package mn.landing.landing_service.Service;

import mn.landing.landing_service.Entity.*;
import mn.landing.landing_service.Model.*;
import mn.landing.landing_service.Repository.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final BCryptPasswordEncoder encoder;
    private final MailService mailService;

    public AuthService(UserRepository userRepository,
                       PasswordResetTokenRepository tokenRepository,
                       BCryptPasswordEncoder encoder,
                       MailService mailService) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.encoder = encoder;
        this.mailService = mailService;
    }

    public User register(RegisterRequest req) {

        if (req.terms == null || !req.terms) {
            throw new RuntimeException("TERMS_NOT_ACCEPTED");
        }

        if (req.password == null || req.password.length() < 8) {
            throw new RuntimeException("PASSWORD_TOO_SHORT");
        }

        if (!req.password.equals(req.confirmPassword)) {
            throw new RuntimeException("PASSWORD_NOT_MATCH");
        }

        if (userRepository.existsByEmail(req.email)) {
            throw new RuntimeException("EMAIL_ALREADY_EXISTS");
        }

        if (userRepository.existsByRegisterNumber(req.registerNumber)) {
            throw new RuntimeException("REGISTER_NUMBER_EXISTS");
        }

        if (userRepository.existsByPhone(req.phone)) {
            throw new RuntimeException("PHONE_EXISTS");
        }

        User user = new User();
        user.setFullName(req.fullname);
        user.setEmail(req.email);
        user.setRegisterNumber(req.registerNumber.toUpperCase());
        user.setPhone(req.phone);
        user.setPasswordHash(encoder.encode(req.password));

        return userRepository.save(user);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }


/* ---------- LOGIN ---------- */
    public LoginResponse login(LoginRequest req) {

        User user = userRepository.findByEmail(req.username)
                .or(() -> userRepository.findByPhone(req.username))
                .orElseThrow(() -> new RuntimeException("USER_NOT_FOUND"));

        if (!encoder.matches(req.password, user.getPasswordHash())) {
            throw new RuntimeException("INVALID_PASSWORD");
        }

        return new LoginResponse(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getRole()
        );
    }

    /* ---------- FORGOT PASSWORD ---------- */
    public void forgotPassword(ForgotPasswordRequest req) {

        // 1. Хэрэглэгч шалгах
        User user = userRepository.findByEmail(req.email)
                .orElseThrow(() -> new RuntimeException("USER_NOT_FOUND"));

        // 2. Reset token үүсгэх
        PasswordResetToken token = new PasswordResetToken();
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiresAt(LocalDateTime.now().plusMinutes(30));

        tokenRepository.save(token);

        // 3. Reset link бэлдэх (FRONT-END URL)
        String resetLink =
                "http://103.87.255.135:3000/reset-password?token=" + token.getToken();

        // 4. HTML mail content үүсгэх
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

        // 5. MAIL ИЛГЭЭХ (CORE)
        mailService.sendHtmlMail(
                user.getEmail(),
                "Нууц үг сэргээх",
                html
        );
    }


    /* ---------- RESET PASSWORD ---------- */
    public void resetPassword(ResetPasswordRequest req) {

        if (!req.newPassword.equals(req.confirmPassword)) {
            throw new RuntimeException("PASSWORD_NOT_MATCH");
        }

        PasswordResetToken token = tokenRepository.findByToken(req.token)
                .orElseThrow(() -> new RuntimeException("INVALID_TOKEN"));

        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("TOKEN_EXPIRED");
        }

        User user = token.getUser();
        user.setPasswordHash(encoder.encode(req.newPassword));
        userRepository.save(user);

        tokenRepository.delete(token);
    }
}


