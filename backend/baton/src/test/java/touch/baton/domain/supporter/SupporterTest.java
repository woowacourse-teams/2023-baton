package touch.baton.domain.supporter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import touch.baton.domain.common.vo.Introduction;
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
import touch.baton.fixture.domain.SupporterFixture;
import touch.baton.fixture.domain.SupporterTechnicalTagFixture;
import touch.baton.fixture.domain.TechnicalTagFixture;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

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

        @DisplayName("supporterTechnicalTags 가 null 이 들어갈 경우 예외가 발생한다.")
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
                .supporterTechnicalTags(new SupporterTechnicalTags(new ArrayList<>()))
                .build();

        final TechnicalTag technicalTag = TechnicalTagFixture.createJava();
        final SupporterTechnicalTag supporterTechnicalTag = SupporterTechnicalTagFixture.create(supporter, technicalTag);
        supporter.addAllSupporterTechnicalTags(List.of(supporterTechnicalTag));

        // when
        final List<SupporterTechnicalTag> actual = supporter.getSupporterTechnicalTags().getSupporterTechnicalTags();

        // then
        assertSoftly(softly -> {
           softly.assertThat(actual).hasSize(1);
           softly.assertThat(actual.get(0)).isEqualTo(supporterTechnicalTag);
        });
    }

    @DisplayName("수정 테스트")
    @Nested
    class Update {

        private Supporter supporter;

        @BeforeEach
        void setUp() {
            supporter = SupporterFixture.create(member);
        }

        @DisplayName("이름 수정에 성공한다.")
        @Test
        void name_success() {
            // given
            final MemberName UpdatedName = new MemberName("디투");

            // when
            supporter.updateMemberName(UpdatedName);

            // then
            assertThat(supporter.getMember().getMemberName()).isEqualTo(UpdatedName);
        }

        @DisplayName("소속 수정에 성공한다.")
        @Test
        void company_success() {
            // given
            final Company updatedCompany = new Company("넥슨");

            // when
            supporter.updateCompany(updatedCompany);

            // then
            assertThat(supporter.getMember().getCompany()).isEqualTo(updatedCompany);
        }

        @DisplayName("소개글 수정에 성공한다.")
        @Test
        void introduction_success() {
            // given
            final Introduction updatedIntroduction = new Introduction("디투");

            // when
            supporter.updateIntroduction(updatedIntroduction);

            // then
            assertThat(supporter.getIntroduction()).isEqualTo(updatedIntroduction);
        }

        @DisplayName("서포터 기술 태그 목록 수정에 성공한다.")
        @Test
        void supporterTechnicalTags_success() {
            // given
            final TechnicalTag updatedTechnicalTag = TechnicalTagFixture.createSpring();
            final SupporterTechnicalTag updatedSupporterTechnicalTag = SupporterTechnicalTagFixture.create(supporter, updatedTechnicalTag);

            // when
            supporter.updateSupporterTechnicalTags(List.of(updatedSupporterTechnicalTag));

            // then
            assertThat(supporter.getSupporterTechnicalTags().getSupporterTechnicalTags()).containsExactly(updatedSupporterTechnicalTag);
        }
    }
}
