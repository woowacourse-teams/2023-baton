package touch.baton.domain.supporter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import touch.baton.domain.common.vo.TagName;
import touch.baton.domain.member.Member;
import touch.baton.domain.member.vo.Company;
import touch.baton.domain.member.vo.GithubUrl;
import touch.baton.domain.member.vo.ImageUrl;
import touch.baton.domain.member.vo.MemberName;
import touch.baton.domain.member.vo.OauthId;
import touch.baton.domain.member.vo.SocialId;
import touch.baton.domain.supporter.exception.SupporterDomainException;
import touch.baton.domain.supporter.vo.ReviewCount;
import touch.baton.domain.technicaltag.SupporterTechnicalTag;
import touch.baton.domain.technicaltag.SupporterTechnicalTags;
import touch.baton.domain.technicaltag.TechnicalTag;
import touch.baton.fixture.domain.SupporterTechnicalTagFixture;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SupporterTest {

    private final Member member = Member.builder()
            .memberName(new MemberName("헤에디주"))
            .socialId(new SocialId("testSocialId"))
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
                    .member(member)
                    .supporterTechnicalTags(new SupporterTechnicalTags(new ArrayList<>()))
                    .build()
            ).doesNotThrowAnyException();
        }

        @DisplayName("member 가 null 이 들어갈 경우 예외가 발생한다.")
        @Test
        void fail_if_member_is_null() {
            assertThatThrownBy(() -> Supporter.builder()
                    .reviewCount(new ReviewCount(10))
                    .member(null)
                    .supporterTechnicalTags(new SupporterTechnicalTags(new ArrayList<>()))
                    .build()
            ).isInstanceOf(SupporterDomainException.class)
                    .hasMessage("Supporter 의 member 는 null 일 수 없습니다.");
        }

        @DisplayName("technicalTags 가 null 이 들어갈 경우 예외가 발생한다.")
        @Test
        void fail_if_supporterTechnicalTags_is_null() {
            assertThatThrownBy(() -> Supporter.builder()
                    .reviewCount(new ReviewCount(10))
                    .member(member)
                    .supporterTechnicalTags(null)
                    .build()
            ).isInstanceOf(SupporterDomainException.class)
                    .hasMessage("Supporter 의 supporterTechnicalTags 는 null 일 수 없습니다.");
        }
    }

    @DisplayName("supporter 의 technicalTags 를 조회한다.")
    @Test
    void read_supporterTechnicalTags() {
        // given
        final Supporter supporter = Supporter.builder()
                .reviewCount(new ReviewCount(10))
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

        // when
        final List<SupporterTechnicalTag> actualTags = supporter.getSupporterTechnicalTags().getSupporterTechnicalTags();

        // then
        assertAll(
                () -> assertEquals(actualTags.size(), 2),
                () -> assertEquals(expectedFirstTag, actualTags.get(0).getTechnicalTag().getTagName().getValue()),
                () -> assertEquals(expectedSecondTag, actualTags.get(1).getTechnicalTag().getTagName().getValue())
        );
    }
}
