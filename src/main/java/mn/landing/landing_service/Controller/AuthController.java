package mn.landing.landing_service.Controller;

import mn.landing.landing_service.Model.*;
import mn.landing.landing_service.Service.AuthService;
import mn.landing.landing_service.Entity.User;
import mn.landing.landing_service.Security.UserPrincipal;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public RegisterResponse register(@RequestBody RegisterRequest request) {
        User user = authService.register(request);

        // register хийгээд шууд login хийж token өгье гэвэл эндээс authService.login(...) дуудчихаж болно.
        // Одоохондоо шууд token үүсгэхгүй хувилбар.
        return new RegisterResponse(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getRole(),
                null,
                null
        );
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/refresh")
    public RefreshTokenResponse refresh(@RequestBody RefreshTokenRequest request) {
        return authService.refresh(request);
    }

    @PostMapping("/logout")
    public void logout(@RequestBody LogoutRequest request) {
        authService.logout(request);
    }

    @PostMapping("/forgot-password")
    public void forgot(@RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request);
    }

    @PostMapping("/reset-password")
    public void reset(@RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
    }

    @GetMapping("/me")
    public Object me(@AuthenticationPrincipal Object principal) {
        // JWT filter хэрэглэж байгаа үед principal нь UserPrincipal байна
        if (principal instanceof UserPrincipal up) {
            var u = up.getUser();
            return new Object() {
                public final Long id = u.getId();
                public final String email = u.getEmail();
                public final String fullName = u.getFullName();
                public final Object role = u.getRole();
            };
        }
        throw new RuntimeException("UNAUTHORIZED");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAll")
    public List<User> getAll() {
        return authService.getAll();
    }
}
