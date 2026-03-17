package mn.landing.landing_service.Model;

import mn.landing.landing_service.Entity.Role;

public class RegisterResponse {
    public Long id;
    public String email;
    public String fullName;
    public Role role;

    public String accessToken;
    public String refreshToken;

    public RegisterResponse(Long id, String email, String fullName, Role role, String accessToken, String refreshToken) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
