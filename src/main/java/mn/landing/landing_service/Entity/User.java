package mn.landing.landing_service.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
        name = "users",
        indexes = {
                @Index(name = "idx_users_email", columnList = "email"),
                @Index(name = "idx_users_phone", columnList = "phone")
        }
)
public class User extends BaseEntity {

    @Column(nullable = false, length = 255)
    private String fullName;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false, length = 10, unique = true)
    private String registerNumber; // РД

    @Column(nullable = false, length = 15, unique = true)
    private String phone; // Утас

    @Column(nullable = false, length = 100)
    private String passwordHash; // BCrypt ~60 chars

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role = Role.USER;
}
