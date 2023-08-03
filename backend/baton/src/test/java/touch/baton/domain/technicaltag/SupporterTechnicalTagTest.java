package touch.baton.domain.technicaltag;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import touch.baton.domain.common.vo.Grade;
import touch.baton.domain.common.vo.TotalRating;
import touch.baton.domain.member.Member;
import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.supporter.vo.ReviewCount;
import touch.baton.domain.supporter.vo.StarCount;
import touch.baton.domain.tag.exception.SupporterTechnicalTagDomainException;
import touch.baton.domain.tag.exception.TagDomainException;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.SupporterFixture;
import touch.baton.fixture.domain.TechnicalTagFixture;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SupporterTechnicalTagTest {

    @DisplayName("생성 테스트")
    @Nested
    class Create {

        private final Member member = MemberFixture.createHyena();

        private final TechnicalTag technicalTag = TechnicalTagFixture.createJava();

        private final Supporter supporter = SupporterFixture.create(
                new ReviewCount(0),
                new StarCount(0),
                new TotalRating(10),
                Grade.BARE_FOOT,
                member,
                new ArrayList<>());

        @DisplayName("성공한다.")
        @Test
        void success() {
            assertThatCode(() -> SupporterTechnicalTag.builder()
                    .supporter(supporter)
                    .technicalTag(technicalTag)
                    .build()
            ).doesNotThrowAnyException();
        }

        @DisplayName("supporter 가 null 이 들어갈 경우 예외가 발생한다.")
        @Test
        void fail_if_supporter_is_null() {
            assertThatThrownBy(() -> SupporterTechnicalTag.builder()
                    .supporter(null)
                    .technicalTag(technicalTag)
                    .build()
            ).isInstanceOf(SupporterTechnicalTagDomainException.class)
                    .hasMessage("SupporterTechnicalTag 의 supporter 는 null 일 수 없습니다.");
        }

        @DisplayName("technical tag 가 null 이 들어갈 경우 예외가 발생한다.")
        @Test
        void fail_if_technical_tag_is_null() {
            assertThatThrownBy(() -> SupporterTechnicalTag.builder()
                    .supporter(supporter)
                    .technicalTag(null)
                    .build()
            ).isInstanceOf(SupporterTechnicalTagDomainException.class)
                    .hasMessage("SupporterTechnicalTag 의 technicalTag 는 null 일 수 없습니다.");
        }
    }
}
