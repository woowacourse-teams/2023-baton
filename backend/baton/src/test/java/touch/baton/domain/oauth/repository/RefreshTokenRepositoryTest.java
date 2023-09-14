package touch.baton.domain.oauth.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import touch.baton.config.RepositoryTestConfig;
import touch.baton.domain.member.Member;
import touch.baton.domain.oauth.token.ExpireDate;
import touch.baton.domain.oauth.token.RefreshToken;
import touch.baton.domain.oauth.token.Token;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RefreshTokenFixture;

import java.time.LocalDateTime;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static touch.baton.fixture.vo.ExpireDateFixture.expireDate;
import static touch.baton.fixture.vo.TokenFixture.token;

class RefreshTokenRepositoryTest extends RepositoryTestConfig {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private EntityManager em;

    @DisplayName("리프레시 토큰을 토큰으로 찾을 수 있다.")
    @Test
    void findByToken() {
        // given
        final Member ethan = MemberFixture.createEthan();
        final Member ditoo = MemberFixture.createDitoo();
        em.persist(ethan);
        em.persist(ditoo);

        final LocalDateTime expireDate = now().plusDays(30);
        final Token ethanToken = token("ethan RefreshToken");
        final RefreshToken actual = RefreshTokenFixture.create(ethan, ethanToken, expireDate(expireDate));
        final Token ditooToken = token("ditoo RefreshToken");
        final RefreshToken otherToken = RefreshTokenFixture.create(ditoo, ditooToken, expireDate(expireDate));
        em.persist(actual);
        em.persist(otherToken);

        em.flush();
        em.clear();

        // when
        final Optional<RefreshToken> expected = refreshTokenRepository.findByToken(ethanToken);

        // then
        assertSoftly(softly -> {
            softly.assertThat(expected).isPresent();
            softly.assertThat(expected.get().getToken().getValue()).isEqualTo(actual.getToken().getValue());
            softly.assertThat(expected.get().getExpireDate().getValue()).isEqualTo(actual.getExpireDate().getValue());
        } );
    }

    @DisplayName("리프레시 토큰을 사용자로 찾을 수 있다.")
    @Test
    void findByMember() {
        // given
        final Member owner = MemberFixture.createEthan();
        final Member notOwner = MemberFixture.createDitoo();
        em.persist(owner);
        em.persist(notOwner);

        final ExpireDate expireDate = expireDate(now().plusDays(30));
        final RefreshToken actual = RefreshTokenFixture.create(owner, token("ethan RefreshToken"), expireDate);
        final RefreshToken differentRefreshToken = RefreshTokenFixture.create(notOwner, token("ditoo RefreshToken"), expireDate);
        em.persist(actual);
        em.persist(differentRefreshToken);

        em.flush();
        em.clear();

        // when
        final Optional<RefreshToken> expected = refreshTokenRepository.findByMember(owner);

        // then
        assertSoftly(softly -> {
            softly.assertThat(expected).isPresent();
            softly.assertThat(expected.get().getToken()).isEqualTo(actual.getToken());
            softly.assertThat(expected.get().getExpireDate()).isEqualTo(actual.getExpireDate());
        } );
    }
}
