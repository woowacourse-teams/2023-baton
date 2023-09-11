package touch.baton.domain.oauth.token;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import touch.baton.domain.common.BaseEntity;
import touch.baton.domain.member.Member;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class RefreshToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "fk_refresh_token_to_member"), nullable = false)
    private Member member;

    @Embedded
    private Token token;

    @Builder
    private RefreshToken(final Member member, final Token token) {
        this.member = member;
        this.token = token;
    }

    public void updateToken(final Token token) {
        this.token = token;
    }

    public boolean isNotOwner(final Member member) {
        return !this.member.equals(member);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isBefore(getUpdatedAt().plusDays(EXPIRE_DATE));
    }
}
