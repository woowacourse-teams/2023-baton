package touch.baton.domain.supporter.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import touch.baton.config.RepositoryTestConfig;
import touch.baton.tobe.domain.member.command.Member;
import touch.baton.tobe.domain.member.command.repository.MemberCommandRepository;
import touch.baton.tobe.domain.member.command.Supporter;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.SupporterFixture;
import touch.baton.tobe.domain.member.query.repository.SupporterQueryRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class SupporterQueryRepositoryTest extends RepositoryTestConfig {

    @Autowired
    private MemberCommandRepository memberCommandRepository;

    @Autowired
    private SupporterQueryRepository supporterQueryRepository;

    @DisplayName("Supporter 식별자값으로 Member 를 패치 조인하여 조회한다.")
    @Test
    void joinMemberBySupporterId() {
        // given
        final Member savedMember = memberCommandRepository.save(MemberFixture.createHyena());
        final Supporter savedSupporter = supporterQueryRepository.save(SupporterFixture.create(savedMember));

        // when
        final Optional<Supporter> maybeSupporter = supporterQueryRepository.joinMemberBySupporterId(savedSupporter.getId());

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
