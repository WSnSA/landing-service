package mn.landing.landing_service.Model;

import mn.landing.landing_service.Entity.Role;

public class LoginResponse {

    public Long id;
    public String email;
    public String fullName;
    public Role role;

    public LoginResponse(Long id, String email, String fullName, Role role) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
    }
}
