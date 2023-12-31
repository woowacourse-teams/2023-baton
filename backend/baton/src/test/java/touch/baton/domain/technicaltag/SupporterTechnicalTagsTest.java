package touch.baton.domain.technicaltag;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import touch.baton.domain.member.command.Member;
import touch.baton.domain.member.command.Supporter;
import touch.baton.domain.technicaltag.command.SupporterTechnicalTag;
import touch.baton.domain.technicaltag.command.SupporterTechnicalTags;
import touch.baton.domain.technicaltag.command.TechnicalTag;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.SupporterFixture;
import touch.baton.fixture.domain.SupporterTechnicalTagFixture;
import touch.baton.fixture.domain.TechnicalTagFixture;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SupporterTechnicalTagsTest {

    private Supporter supporter;
    private SupporterTechnicalTag supporterTechnicalTag;

    @BeforeEach
    void setUp() {
        final Member member = MemberFixture.createDitoo();
        supporter = SupporterFixture.create(member);
        final TechnicalTag technicalTag = TechnicalTagFixture.createJava();
        supporterTechnicalTag = SupporterTechnicalTagFixture.create(supporter, technicalTag);
    }

    @DisplayName("다중 덧셈 테스트")
    @Test
    void addAll() {
        // given
        final SupporterTechnicalTags supporterTechnicalTags = new SupporterTechnicalTags(new ArrayList<>());

        // when
        supporterTechnicalTags.addAll(List.of(supporterTechnicalTag));

        // then
        assertThat(supporterTechnicalTags.getSupporterTechnicalTags()).containsExactly(supporterTechnicalTag);
    }
}
