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

    public AuthService(UserRepository userRepository,
                       PasswordResetTokenRepository tokenRepository,
                       BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.encoder = encoder;
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

        User user = userRepository.findByEmail(req.email)
                .orElseThrow(() -> new RuntimeException("USER_NOT_FOUND"));

        PasswordResetToken token = new PasswordResetToken();
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiresAt(LocalDateTime.now().plusMinutes(30));

        tokenRepository.save(token);

        // TODO: email / sms илгээх
        System.out.println("RESET TOKEN: " + token.getToken());
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


