package mn.landing.landing_service.Entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "templates")
public class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type; // business, wedding, etc

    @Column(nullable = false)
    private String description; // business, wedding, etc

    @Column(columnDefinition = "json")
    private String schemaJson;

    private LocalDateTime createdAt = LocalDateTime.now();
}
