package touch.baton.domain.member.query.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import touch.baton.config.RepositoryTestConfig;
import touch.baton.domain.member.command.Supporter;
import touch.baton.domain.member.command.vo.ReviewCount;
import touch.baton.fixture.domain.MemberFixture;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class RankQuerydslRepositoryTest extends RepositoryTestConfig {

    @Autowired
    private RankQuerydslRepository rankQueryDslRepository;

    @DisplayName("Supporter의 ReviewCount가 높은 순으로 count 개수 만큼 조회한다.")
    @Test
    void findMostReviewSupporterByCount() {
        // given
        persistSupporter(new ReviewCount(4), MemberFixture.createEthan());
        persistSupporter(new ReviewCount(3), MemberFixture.createHyena());
        persistSupporter(new ReviewCount(8), MemberFixture.createDitoo());

        // when
        final int count = 2;
        final List<Supporter> actual = rankQueryDslRepository.findMostReviewSupporterByCount(count);

        // then
        assertAll(
                () -> assertThat(actual).hasSize(count),
                () -> assertThat(actual.get(0).getReviewCount().getValue()).isEqualTo(8),
                () -> assertThat(actual.get(1).getReviewCount().getValue()).isEqualTo(4)
        );
    }

    @DisplayName("Supporter의 count가 안되더라도 ReviewCount가 0이면 조회되지 않는다.")
    @Test
    void findMostReviewSupporterByCount_having_count_is_zero() {
        // given
        persistSupporter(new ReviewCount(0), MemberFixture.createEthan());
        persistSupporter(new ReviewCount(3), MemberFixture.createHyena());
        persistSupporter(new ReviewCount(8), MemberFixture.createDitoo());

        // when
        final int count = 3;
        final int zeroCount = 1;
        final List<Supporter> actual = rankQueryDslRepository.findMostReviewSupporterByCount(count);

        // then
        assertAll(
                () -> assertThat(actual).hasSize(count - zeroCount),
                () -> assertThat(actual.get(0).getReviewCount().getValue()).isEqualTo(8),
                () -> assertThat(actual.get(1).getReviewCount().getValue()).isEqualTo(3)
        );
    }
}
