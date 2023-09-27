package touch.baton.domain.technicaltag;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.SupporterFixture;
import touch.baton.fixture.domain.TechnicalTagFixture;
import touch.baton.tobe.domain.member.command.Member;
import touch.baton.tobe.domain.member.command.Supporter;
import touch.baton.tobe.domain.member.command.vo.ReviewCount;
import touch.baton.tobe.domain.tag.exception.SupporterTechnicalTagDomainException;
import touch.baton.tobe.domain.technicaltag.command.SupporterTechnicalTag;
import touch.baton.tobe.domain.technicaltag.command.TechnicalTag;

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
