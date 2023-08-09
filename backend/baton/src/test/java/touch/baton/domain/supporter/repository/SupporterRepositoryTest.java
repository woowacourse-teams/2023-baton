package touch.baton.domain.supporter.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import touch.baton.config.RepositoryTestConfig;
import touch.baton.domain.member.Member;
import touch.baton.domain.member.repository.MemberRepository;
import touch.baton.domain.supporter.Supporter;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.SupporterFixture;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class SupporterRepositoryTest extends RepositoryTestConfig {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SupporterRepository supporterRepository;

    @DisplayName("Supporter 식별자값으로 Member 를 패치 조인하여 조회한다.")
    @Test
    void joinMemberBySupporterId() {
        // given
        final Member savedMember = memberRepository.save(MemberFixture.createHyena());
        final Supporter savedSupporter = supporterRepository.save(SupporterFixture.create(savedMember));

        // when
        final Optional<Supporter> maybeSupporter = supporterRepository.joinMemberBySupporterId(savedSupporter.getId());

        // then
        assertAll(
                () -> assertThat(maybeSupporter).isPresent(),
                () -> assertThat(maybeSupporter.get().getId()).isEqualTo(savedSupporter.getId()),
                () -> assertThat(maybeSupporter.get().getIntroduction()).isEqualTo(savedSupporter.getIntroduction()),
                () -> assertThat(maybeSupporter.get().getReviewCount()).isEqualTo(savedSupporter.getReviewCount()),
                () -> assertThat(maybeSupporter.get().getSupporterTechnicalTags()).isEqualTo(savedSupporter.getSupporterTechnicalTags()),
                () -> assertThat(maybeSupporter.get().getMember()).isEqualTo(savedMember)
        );

    }
}
