package touch.baton.domain.oauth.command.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import touch.baton.config.RepositoryTestConfig;
import touch.baton.domain.member.command.Member;
import touch.baton.domain.oauth.command.token.RefreshToken;
import touch.baton.domain.oauth.command.token.Token;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RefreshTokenFixture;

import java.time.LocalDateTime;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
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
        final Member ethan = persistMember(MemberFixture.createEthan());
        final Member ditoo = persistMember(MemberFixture.createDitoo());

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
        });
    }

    @DisplayName("리프레시 토큰을 사용자로 찾을 수 있다.")
    @Test
    void findByMember() {
        // given
        final Member owner = persistMember(MemberFixture.createEthan());
        final Member notOwner = persistMember(MemberFixture.createDitoo());

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
        });
    }

    @DisplayName("사용자를 이용해 리프레시 토큰을 삭제할 수 있다.")
    @Test
    void logout() {
        // given
        final Member owner = persistMember(MemberFixture.createEthan());

        final LocalDateTime expireDate = createExpireDate(now().plusDays(30));

        final RefreshToken expected = RefreshTokenFixture.create(owner, token("ethan RefreshToken"), expireDate(expireDate));
        em.persist(expected);

        em.flush();
        em.clear();

        // when
        refreshTokenCommandRepository.deleteByMember(owner);

        // then
        assertThat(refreshTokenCommandRepository.findByMember(owner)).isNotPresent();
    }
}
