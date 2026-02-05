package mn.landing.landing_service.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "media_files", indexes = {
        @Index(name = "idx_media_owner", columnList = "owner_user_id"),
        @Index(name = "idx_media_landing", columnList = "landing_id")
})
public class MediaFile extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_user_id", nullable = false)
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "landing_id")
    private Landing landing;

    @Column(nullable = false, length = 255)
    private String originalName;

    @Column(nullable = false, length = 120)
    private String mimeType;

    @Column(nullable = false)
    private Long sizeBytes;

    @Column(nullable = false, length = 500)
    private String storageKey; // MinIO/S3 key

    @Column(length = 1000)
    private String publicUrl;

    @Column(length = 128)
    private String checksum;
}
