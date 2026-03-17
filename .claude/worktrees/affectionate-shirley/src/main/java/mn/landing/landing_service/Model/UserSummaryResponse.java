package mn.landing.landing_service.Model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSummaryResponse {
    public Long id;
    public String email;
    public String fullName;
    public Object role;

    public UserSummaryResponse(Long id, String email, String fullName, Object role) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
    }
}
