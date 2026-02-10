package mn.landing.landing_service.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import mn.landing.landing_service.Entity.Role;
import mn.landing.landing_service.Entity.User;
import mn.landing.landing_service.Model.RegisterRequest;
import mn.landing.landing_service.Model.UserSummaryResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminUserService {

    private final AuthService authService;

    @PersistenceContext
    private EntityManager em;

    public AdminUserService(AuthService authService) {
        this.authService = authService;
    }

    public boolean adminExists() {
        Long cnt = em.createQuery("select count(u) from User u where u.role = :role", Long.class)
                .setParameter("role", Role.ADMIN)
                .getSingleResult();
        return cnt != null && cnt > 0;
    }

    public List<UserSummaryResponse> listUsers() {
        List<User> list = em.createQuery("select u from User u order by u.id desc", User.class)
                .getResultList();
        return list.stream()
                .map(u -> new UserSummaryResponse(u.getId(), u.getEmail(), u.getFullName(), u.getRole()))
                .toList();
    }

    /**
     * Create a brand-new admin user.
     * Uses AuthService.register(...) to keep validation/password hashing consistent,
     * then promotes role to ADMIN.
     */
    @Transactional
    public void createAdmin(RegisterRequest request) {
        if (request == null) throw new RuntimeException("BAD_REQUEST");
        // Make sure registration validations won't fail because of terms/confirmPassword
        if (request.terms == null) request.terms = true;
        if (request.confirmPassword == null) request.confirmPassword = request.password;

        User created = authService.register(request);
        promoteAdmin(created.getId());
    }

    /** Promote existing user to ADMIN */
    @Transactional
    public void promoteAdmin(Long userId) {
        if (userId == null) throw new RuntimeException("BAD_REQUEST");
        int updated = em.createQuery("update User u set u.role = :role where u.id = :id")
                .setParameter("role", Role.ADMIN)
                .setParameter("id", userId)
                .executeUpdate();
        if (updated == 0) throw new RuntimeException("USER_NOT_FOUND");
    }

    /**
     * Bootstrap the very first admin without HTTP endpoints.
     * Call from a CommandLineRunner (env-based) only.
     */
    @Transactional
    public void bootstrapFirstAdmin(RegisterRequest request) {
        if (adminExists()) return;
        createAdmin(request);
    }
}
