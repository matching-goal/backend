package matchingGoal.matchingGoal.member.model.entity;


import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
public class Member{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @Setter
    private String imageUrl;

    @Setter
    private String name;

    @Setter
    private String password;

    @Setter
    private String nickname;

    @Setter
    private String introduction;

    @Setter
    private String region;

    @CreatedDate
    private LocalDateTime createdDate;

    @Builder.Default
    @Setter
    private boolean isDeleted = false;

    @Setter
    private LocalDateTime deletedDate;

    @Setter
    private Integer requestCount;

}
