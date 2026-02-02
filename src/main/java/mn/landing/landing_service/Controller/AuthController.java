package mn.landing.landing_service.Controller;

import mn.landing.landing_service.Model.*;
import mn.landing.landing_service.Service.AuthService;
import mn.landing.landing_service.Entity.User;
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
        return new RegisterResponse(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getRole()
        );
    }


    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/forgot-password")
    public void forgot(@RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request);
    }

    @PostMapping("/reset-password")
    public void reset(@RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
    }

    @GetMapping("/getAll")
    public List<User> getAll(){
        return authService.getAll();
    }

}
