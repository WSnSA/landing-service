package mn.landing.landing_service.Bootstrap;

import mn.landing.landing_service.Model.RegisterRequest;
import mn.landing.landing_service.Service.AdminUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Production-safe ADMIN bootstrapping.
 *
 * If no ADMIN exists, and properties are provided, creates the first ADMIN account at startup.
 *
 * Usage (env):
 *   LANDING_BOOTSTRAP_ADMIN_EMAIL
 *   LANDING_BOOTSTRAP_ADMIN_PASSWORD
 *   LANDING_BOOTSTRAP_ADMIN_FULLNAME (optional)
 *   LANDING_BOOTSTRAP_ADMIN_PHONE (optional)
 *   LANDING_BOOTSTRAP_ADMIN_REGISTER_NUMBER (optional)
 *
 * After the first run, REMOVE these env vars.
 */
@Component
public class AdminBootstrapRunner implements CommandLineRunner {

    private final AdminUserService adminUserService;

    @Value("${LANDING_BOOTSTRAP_ADMIN_EMAIL:}")
    private String email;

    @Value("${LANDING_BOOTSTRAP_ADMIN_PASSWORD:}")
    private String password;

    @Value("${LANDING_BOOTSTRAP_ADMIN_FULLNAME:Landing Admin}")
    private String fullName;

    @Value("${LANDING_BOOTSTRAP_ADMIN_PHONE:}")
    private String phone;

    @Value("${LANDING_BOOTSTRAP_ADMIN_REGISTER_NUMBER:}")
    private String registerNumber;

    public AdminBootstrapRunner(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @Override
    public void run(String... args) {
        if (email == null || email.isBlank()) return;
        if (password == null || password.isBlank()) return;

        if (adminUserService.adminExists()) return;

        RegisterRequest req = new RegisterRequest();
        req.fullname = fullName;
        req.email = email;
        req.registerNumber = registerNumber;
        req.phone = phone;
        req.password = password;
        req.confirmPassword = password;
        req.terms = true;

        adminUserService.bootstrapFirstAdmin(req);
    }
}
