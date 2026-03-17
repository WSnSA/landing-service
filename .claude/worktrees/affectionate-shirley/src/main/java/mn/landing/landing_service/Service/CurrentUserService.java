package mn.landing.landing_service.Service;

import mn.landing.landing_service.Entity.User;
import mn.landing.landing_service.Repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    private final UserRepository userRepository;

    public CurrentUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User requireUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) throw new RuntimeException("UNAUTHORIZED");

        Object principal = auth.getPrincipal();

        // 1) Хэрэв principal нь User entity өөрөө байвал
        if (principal instanceof User u) return u;

        // 2) UserDetails байвал username аваад DB-с хайна
        String username;
        if (principal instanceof UserDetails ud) username = ud.getUsername();
        else if (principal instanceof String s) username = s;
        else {
            username = null;
        }

        if (username == null || username.isBlank()) throw new RuntimeException("UNAUTHORIZED");

        return userRepository.findByEmail(username)
                .or(() -> userRepository.findByPhone(username))
                .orElseThrow(() -> new RuntimeException("USER_NOT_FOUND"));
    }

    public User requireAdmin() {
        User me = requireUser();
        if (me.getRole() == null || !me.getRole().toString().equalsIgnoreCase("ADMIN")) {
            throw new RuntimeException("FORBIDDEN");
        }
        return me;
    }
}
