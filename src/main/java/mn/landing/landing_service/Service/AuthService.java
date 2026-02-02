package mn.landing.landing_service.Service;

import mn.landing.landing_service.Entity.User;
import mn.landing.landing_service.Model.RegisterRequest;
import mn.landing.landing_service.Repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    public AuthService(UserRepository userRepository,
                       BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
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
}

