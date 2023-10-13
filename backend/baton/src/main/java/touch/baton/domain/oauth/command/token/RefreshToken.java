package touch.baton.domain.oauth.command.token;

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
import touch.baton.domain.member.command.Member;
import touch.baton.domain.oauth.command.token.exception.RefreshTokenDomainException;

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

    @Embedded
    private ExpireDate expireDate;

    @Builder
    private RefreshToken(final Member member, final Token token, final ExpireDate expireDate) {
        this(null, member, token, expireDate);
    }

    private RefreshToken(final Long id, final Member member, final Token token, final ExpireDate expireDate) {
        validateNotNull(member, token, expireDate);
        this.id = id;
        this.member = member;
        this.token = token;
        this.expireDate = expireDate;
    }

    private void validateNotNull(final Member member, final Token token, final ExpireDate expireDate) {
        if (member == null) {
            throw new RefreshTokenDomainException("RefreshToken 의 member 는 null 일 수 없습니다.");
        }
        if (token == null) {
            throw new RefreshTokenDomainException("RefreshToken 의 token 은 null 일 수 없습니다.");
        }
        if (expireDate == null) {
            throw new RefreshTokenDomainException("RefreshToken 의 expireDate 는 null 일 수 없습니다.");
        }
    }

    public void updateToken(final Token token, final int expiredMinutes) {
        this.token = token;
        expireDate.refreshExpireTokenDate(expiredMinutes);
    }

    public boolean isNotOwner(final Member member) {
        return !this.member.equals(member);
    }

    public boolean isExpired() {
        return expireDate.isExpired();
    }
}
