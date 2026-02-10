package mn.landing.landing_service.Controller;

import mn.landing.landing_service.Model.RegisterRequest;
import mn.landing.landing_service.Model.UserSummaryResponse;
import mn.landing.landing_service.Service.AdminUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUsersController {

    private final AdminUserService adminUserService;

    public AdminUsersController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ADMIN') or hasAuthority('ROLE_ADMIN')")
    @GetMapping
    public List<UserSummaryResponse> listUsers() {
        return adminUserService.listUsers();
    }

    // Create a NEW admin user (register + promote)
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ADMIN') or hasAuthority('ROLE_ADMIN')")
    @PostMapping("/create-admin")
    public void createAdmin(@RequestBody RegisterRequest request) {
        adminUserService.createAdmin(request);
    }

    // Promote an EXISTING user to admin
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('ADMIN') or hasAuthority('ROLE_ADMIN')")
    @PostMapping("/{userId}/promote-admin")
    public void promoteAdmin(@PathVariable Long userId) {
        adminUserService.promoteAdmin(userId);
    }
}
