package touch.baton.tobe.domain.oauth.command.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import touch.baton.config.RepositoryTestConfig;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RefreshTokenFixture;
import touch.baton.tobe.domain.member.command.Member;
import touch.baton.tobe.domain.oauth.command.token.RefreshToken;
import touch.baton.tobe.domain.oauth.command.token.Token;

import java.time.LocalDateTime;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static touch.baton.fixture.vo.ExpireDateFixture.expireDate;
import static touch.baton.fixture.vo.TokenFixture.token;
import static touch.baton.util.TestDateFormatUtil.createExpireDate;

class RefreshTokenCommandRepositoryTest extends RepositoryTestConfig {

    @Autowired
    private RefreshTokenCommandRepository refreshTokenCommandRepository;

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

        final LocalDateTime expireDate = createExpireDate(now().plusDays(30));

        final Token ethanToken = token("ethan RefreshToken");
        final RefreshToken expected = RefreshTokenFixture.create(ethan, ethanToken, expireDate(expireDate));
        final Token ditooToken = token("ditoo RefreshToken");
        final RefreshToken otherToken = RefreshTokenFixture.create(ditoo, ditooToken, expireDate(expireDate));
        em.persist(expected);
        em.persist(otherToken);

        em.flush();
        em.clear();

        // when
        final Optional<RefreshToken> actual = refreshTokenCommandRepository.findByToken(ethanToken);

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual).isPresent();
            softly.assertThat(actual.get().getToken()).isEqualTo(expected.getToken());
            softly.assertThat(actual.get().getExpireDate()).isEqualTo(expected.getExpireDate());
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

        final LocalDateTime expireDate = createExpireDate(now().plusDays(30));

        final RefreshToken expected = RefreshTokenFixture.create(owner, token("ethan RefreshToken"), expireDate(expireDate));
        final RefreshToken differentRefreshToken = RefreshTokenFixture.create(notOwner, token("ditoo RefreshToken"), expireDate(expireDate));
        em.persist(expected);
        em.persist(differentRefreshToken);

        em.flush();
        em.clear();

        // when
        final Optional<RefreshToken> actual = refreshTokenCommandRepository.findByMember(owner);

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual).isPresent();
            softly.assertThat(actual.get().getToken()).isEqualTo(expected.getToken());
            softly.assertThat(actual.get().getExpireDate()).isEqualTo(expected.getExpireDate());
        } );
    }
}
