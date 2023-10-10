package touch.baton.domain.supporter.command.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import touch.baton.config.RepositoryTestConfig;
import touch.baton.domain.member.command.Supporter;
import touch.baton.domain.member.query.repository.SupporterQueryRepository;
import touch.baton.fixture.domain.MemberFixture;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class SupporterQueryRepositoryTest extends RepositoryTestConfig {

    @Autowired
    private SupporterQueryRepository supporterQueryRepository;

    @DisplayName("Supporter 식별자값으로 Member 를 패치 조인하여 조회한다.")
    @Test
    void joinMemberBySupporterId() {
        // given
        final Supporter savedSupporter = persistSupporter(MemberFixture.createHyena());

        // when
        final Optional<Supporter> maybeSupporter = supporterQueryRepository.joinMemberBySupporterId(savedSupporter.getId());

        // then
        assertAll(
                () -> assertThat(maybeSupporter).isPresent(),
                () -> assertThat(maybeSupporter.get().getId()).isEqualTo(savedSupporter.getId()),
                () -> assertThat(maybeSupporter.get().getIntroduction()).isEqualTo(savedSupporter.getIntroduction()),
                () -> assertThat(maybeSupporter.get().getReviewCount()).isEqualTo(savedSupporter.getReviewCount()),
                () -> assertThat(maybeSupporter.get().getSupporterTechnicalTags()).isEqualTo(savedSupporter.getSupporterTechnicalTags()),
                () -> assertThat(maybeSupporter.get().getMember()).isEqualTo(savedSupporter.getMember())
        );

    }
}
