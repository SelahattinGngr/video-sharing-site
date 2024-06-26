package video_sharing_site.back_end.VideoSite.Entity;

import java.sql.Timestamp;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

/*
 * @PrePersist : Bu anotasyon, entity ilk kez veritabanına kaydedilmeden hemen önce çalıştırılır.
 * @PreUpdate  : Bu anotasyon, entity her güncellendiğinde çalıştırılır.
 */

// Lombok'un getter, setter, toString, equalsAndHashCode ve constructor'ları otomatik olusturmasini saglar
@Data
@Entity
@Table(name = "users")
public class UsersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "followers_count")
    private long followersCount;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "username", nullable = false, unique = true)
    private String userName;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "refresh_token", nullable = true)
    private String refreshToken;

    @Column(name = "access_token", nullable = true)
    private String accessToken;

    @Column(name = "role") // true = admin, false = user
    private boolean role;

    @Column(name = "birthday", nullable = false)
    private LocalDate birthday;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @PrePersist
    public void onCreate() {
        this.followersCount = 0;
        this.role = false;
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = new Timestamp(System.currentTimeMillis());
    }
}
