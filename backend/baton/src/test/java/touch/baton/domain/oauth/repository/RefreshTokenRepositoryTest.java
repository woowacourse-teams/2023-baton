package touch.baton.domain.oauth.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import touch.baton.config.RepositoryTestConfig;
import touch.baton.domain.member.Member;
import touch.baton.domain.oauth.token.RefreshToken;
import touch.baton.domain.oauth.token.Token;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RefreshTokenFixture;

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

        final Token ethanTokenValue = token("ethan RefreshToken");
        final RefreshToken actual = RefreshTokenFixture.create(ethan, ethanTokenValue, expireDate(now().plusDays(30)));
        final Token ditooTokenValue = token("ditoo RefreshToken");
        final RefreshToken ditooRefreshToken = RefreshTokenFixture.create(ditoo, ditooTokenValue, expireDate(now().plusDays(30)));
        refreshTokenRepository.save(actual);
        refreshTokenRepository.save(ditooRefreshToken);
        em.flush();
        em.clear();

        // when
        final Optional<RefreshToken> expected = refreshTokenRepository.findByToken(ethanTokenValue);

        // then
        assertSoftly(softly -> {
            softly.assertThat(expected).isPresent();
            softly.assertThat(expected.get().getToken()).isEqualTo(actual.getToken());
            softly.assertThat(expected.get().getExpireDate()).isEqualTo(actual.getExpireDate());
        } );
    }

    @DisplayName("리프레시 토큰을 사용자로 찾을 수 있다.")
    @Test
    void findByMember() {
        // given
        final Member ethan = MemberFixture.createEthan();
        final Member ditoo = MemberFixture.createDitoo();
        em.persist(ethan);
        em.persist(ditoo);

        final RefreshToken actual = RefreshTokenFixture.create(ethan, token("ethan RefreshToken"), expireDate(now().plusDays(30)));
        final RefreshToken ditooRefreshToken = RefreshTokenFixture.create(ditoo, token("ditoo RefreshToken"), expireDate(now().plusDays(30)));
        refreshTokenRepository.save(actual);
        refreshTokenRepository.save(ditooRefreshToken);
        em.flush();
        em.clear();

        // when
        final Optional<RefreshToken> expected = refreshTokenRepository.findByMember(ethan);

        // then
        assertSoftly(softly -> {
            softly.assertThat(expected).isPresent();
            softly.assertThat(expected.get().getToken()).isEqualTo(actual.getToken());
            softly.assertThat(expected.get().getExpireDate()).isEqualTo(actual.getExpireDate());
        } );
    }
}
