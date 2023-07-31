package touch.baton.domain.supporter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import touch.baton.domain.common.vo.Grade;
import touch.baton.domain.common.vo.TagName;
import touch.baton.domain.common.vo.TotalRating;
import touch.baton.domain.member.Member;
import touch.baton.domain.member.vo.Company;
import touch.baton.domain.member.vo.Email;
import touch.baton.domain.member.vo.GithubUrl;
import touch.baton.domain.member.vo.ImageUrl;
import touch.baton.domain.member.vo.MemberName;
import touch.baton.domain.member.vo.OauthId;
import touch.baton.domain.supporter.exception.OldSupporterException;
import touch.baton.domain.supporter.vo.ReviewCount;
import touch.baton.domain.supporter.vo.StarCount;
import touch.baton.domain.technicaltag.SupporterTechnicalTag;
import touch.baton.domain.technicaltag.SupporterTechnicalTags;
import touch.baton.domain.technicaltag.TechnicalTag;
import touch.baton.fixture.domain.SupporterTechnicalTagFixture;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SupporterTest {

    private final Member member = Member.builder()
            .memberName(new MemberName("헤에디주"))
            .email(new Email("test@test.co.kr"))
            .oauthId(new OauthId("dsigjh98gh230gn2oinv913bcuo23nqovbvu93b12voi3bc31j"))
            .githubUrl(new GithubUrl("github.com/hyena0608"))
            .company(new Company("우아한형제들"))
            .imageUrl(new ImageUrl("imageUrl"))
            .build();

    @DisplayName("생성 테스트")
    @Nested
    class Create {

        @DisplayName("성공한다.")
        @Test
        void success() {
            assertThatCode(() -> Supporter.builder()
                    .reviewCount(new ReviewCount(10))
                    .starCount(new StarCount(10))
                    .totalRating(new TotalRating(100))
                    .grade(Grade.BARE_FOOT)
                    .member(member)
                    .supporterTechnicalTags(new SupporterTechnicalTags(new ArrayList<>()))
                    .build()
            ).doesNotThrowAnyException();
        }

        @DisplayName("startCount 가 null 이 들어갈 경우 예외가 발생한다.")
        @Test
        void fail_if_startCount_is_null() {
            assertThatThrownBy(() -> Supporter.builder()
                    .reviewCount(new ReviewCount(10))
                    .starCount(null)
                    .totalRating(new TotalRating(100))
                    .grade(Grade.BARE_FOOT)
                    .member(member)
                    .supporterTechnicalTags(new SupporterTechnicalTags(new ArrayList<>()))
                    .build()
            ).isInstanceOf(OldSupporterException.NotNull.class);
        }

        @DisplayName("totalRating 가 null 이 들어갈 경우 예외가 발생한다.")
        @Test
        void fail_if_totalRating_is_null() {
            assertThatThrownBy(() -> Supporter.builder()
                    .reviewCount(new ReviewCount(10))
                    .starCount(new StarCount(10))
                    .totalRating(null)
                    .grade(Grade.BARE_FOOT)
                    .member(member)
                    .supporterTechnicalTags(new SupporterTechnicalTags(new ArrayList<>()))
                    .build()
            ).isInstanceOf(OldSupporterException.NotNull.class);
        }

        @DisplayName("grade 가 null 이 들어갈 경우 예외가 발생한다.")
        @Test
        void fail_if_grade_is_null() {
            assertThatThrownBy(() -> Supporter.builder()
                    .reviewCount(new ReviewCount(10))
                    .starCount(new StarCount(10))
                    .totalRating(new TotalRating(100))
                    .grade(null)
                    .member(member)
                    .supporterTechnicalTags(new SupporterTechnicalTags(new ArrayList<>()))
                    .build()
            ).isInstanceOf(OldSupporterException.NotNull.class);
        }

        @DisplayName("member 가 null 이 들어갈 경우 예외가 발생한다.")
        @Test
        void fail_if_member_is_null() {
            assertThatThrownBy(() -> Supporter.builder()
                    .reviewCount(new ReviewCount(10))
                    .starCount(new StarCount(10))
                    .totalRating(new TotalRating(100))
                    .grade(Grade.BARE_FOOT)
                    .member(null)
                    .supporterTechnicalTags(new SupporterTechnicalTags(new ArrayList<>()))
                    .build()
            ).isInstanceOf(OldSupporterException.NotNull.class);
        }

        @DisplayName("supporterTechnicalTags 가 null 이 들어갈 경우 예외가 발생한다.")
        @Test
        void fail_if_supporterTechnicalTags_is_null() {
            assertThatThrownBy(() -> Supporter.builder()
                    .reviewCount(new ReviewCount(10))
                    .starCount(new StarCount(10))
                    .totalRating(new TotalRating(100))
                    .grade(Grade.BARE_FOOT)
                    .member(member)
                    .supporterTechnicalTags(null)
                    .build()
            ).isInstanceOf(OldSupporterException.NotNull.class);
        }
    }

    @DisplayName("supporter 의 supporterTechnicalTags 를 조회한다.")
    @Test
    void read_supporterTechnicalTags() {
        // given
        final Supporter supporter = Supporter.builder()
                .reviewCount(new ReviewCount(10))
                .starCount(new StarCount(10))
                .totalRating(new TotalRating(100))
                .grade(Grade.BARE_FOOT)
                .member(member)
                .supporterTechnicalTags(new SupporterTechnicalTags(
                        new ArrayList<>()))
                .build();

        final String expectedFirstTag = "java";
        final SupporterTechnicalTag javaTechnicalTag = SupporterTechnicalTagFixture.create(supporter,
                TechnicalTag.builder()
                        .tagName(new TagName(expectedFirstTag))
                        .build());

        final String expectedSecondTag = "spring";
        final SupporterTechnicalTag springTechnicalTag = SupporterTechnicalTagFixture.create(supporter,
                TechnicalTag.builder()
                        .tagName(new TagName(expectedSecondTag))
                        .build());

        final List<SupporterTechnicalTag> expected = List.of(javaTechnicalTag, springTechnicalTag);
        supporter.addAllSupporterTechnicalTags(expected);

        //when, then
        final List<SupporterTechnicalTag> actualTags = supporter.getSupporterTechnicalTags().getSupporterTechnicalTags();
        Assertions.assertEquals(actualTags.size(), 2);
        final String actualFirstTag = actualTags.get(0).getTechnicalTag().getTagName().getValue();
        Assertions.assertEquals(actualFirstTag, expectedFirstTag);
        final String actualSecondTag = actualTags.get(1).getTechnicalTag().getTagName().getValue();

        Assertions.assertEquals(actualSecondTag, expectedSecondTag);
    }
}
