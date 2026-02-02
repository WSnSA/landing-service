package mn.landing.landing_service.Model;

import mn.landing.landing_service.Entity.Role;

public class RegisterResponse {

    private Long id;
    private String email;
    private String fullName;
    private Role role;

    public RegisterResponse(Long id, String email, String fullName, Role role) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
    }

    // getters
    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public Role getRole() {
        return role;
    }
}
