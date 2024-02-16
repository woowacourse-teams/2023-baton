package touch.baton.domain.member.query.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import touch.baton.config.ServiceTestConfig;
import touch.baton.domain.member.command.Member;
import touch.baton.domain.member.command.vo.ReviewCount;
import touch.baton.domain.member.query.controller.response.RankResponses;
import touch.baton.domain.technicaltag.command.TechnicalTag;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.TechnicalTagFixture;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static touch.baton.fixture.vo.MemberNameFixture.memberName;

class RankQueryServiceTest extends ServiceTestConfig {

    private RankQueryService rankQueryService;

    @BeforeEach
    void setUp() {
        rankQueryService = new RankQueryService(rankQuerydslRepository);
    }

    @DisplayName("Supporter의 ReviewCount가 높은 순으로 count 개수 만큼 조회한다.")
    @Test
    void readMostReviewSupporter() {
        // given

        for (int reviewedCount = 0; reviewedCount < 5; reviewedCount++) {
            persistSupporter(new ReviewCount(reviewedCount), MemberFixture.create(memberName("member" + reviewedCount)));
        }

        final TechnicalTag javaTag = technicalTagQueryRepository.save(TechnicalTagFixture.createJava());
        final TechnicalTag springTag = technicalTagQueryRepository.save(TechnicalTagFixture.createSpring());
        final TechnicalTag reactTag = technicalTagQueryRepository.save(TechnicalTagFixture.createReact());

        final Member fourthRankMember = persistMember(MemberFixture.create(memberName("fourth")));
        final List<TechnicalTag> fourthRankMembersTechnicalTags = List.of(javaTag, springTag);
        persistSupporter(new ReviewCount(12), fourthRankMember, fourthRankMembersTechnicalTags);

        final Member thirdRankMember = persistMember(MemberFixture.create(memberName("third")));
        final List<TechnicalTag> thirdRankMembersTechnicalTags = List.of(javaTag, reactTag);
        persistSupporter(new ReviewCount(13), thirdRankMember, thirdRankMembersTechnicalTags);

        final Member firstRankMember = persistMember(MemberFixture.create(memberName("first")));
        final List<TechnicalTag> firstRankMembersTechnicalTags = List.of(javaTag, springTag);
        persistSupporter(new ReviewCount(15), firstRankMember, firstRankMembersTechnicalTags);

        final Member secondRankMember = persistMember(MemberFixture.create(memberName("second")));
        final List<TechnicalTag> secondRankMembersTechnicalTags = List.of(javaTag);
        persistSupporter(new ReviewCount(14), secondRankMember, secondRankMembersTechnicalTags);

        final Member fifthRankMember = persistMember(MemberFixture.create(memberName("fifth")));
        final List<TechnicalTag> fifthMembersTechnicalTags = List.of(reactTag);
        persistSupporter(new ReviewCount(11), fifthRankMember, fifthMembersTechnicalTags);

        // when
        final int maxCount = 5;
        final RankResponses actual = rankQueryService.readMostReviewSupporter(maxCount);

        // then
        assertAll(
                () -> assertResponse(actual.data().get(0), 1, "first", 15),
                () -> assertResponse(actual.data().get(1), 2, "second", 14),
                () -> assertResponse(actual.data().get(2), 3, "third", 13),
                () -> assertResponse(actual.data().get(3), 4, "fourth", 12),
                () -> assertResponse(actual.data().get(4), 5, "fifth", 11)
        );
    }

    private static void assertResponse(final RankResponses.SupporterResponse response, final int rank, final String memberName, final int reviewCount) {
        assertAll(
                () -> assertThat(response.rank()).isEqualTo(rank),
                () -> assertThat(response.name()).isEqualTo(memberName),
                () -> assertThat(response.reviewedCount()).isEqualTo(reviewCount),
                () -> assertThat(response.supporterId()).isNotZero(),
                () -> assertThat(response.imageUrl()).isNotBlank(),
                () -> assertThat(response.githubUrl()).isNotBlank(),
                () -> assertThat(response.technicalTags()).isNotEmpty())
        ;
    }
}
