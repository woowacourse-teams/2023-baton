package touch.baton.fixture.domain;

import touch.baton.domain.member.command.Supporter;
import touch.baton.domain.technicaltag.command.SupporterTechnicalTag;
import touch.baton.domain.technicaltag.command.TechnicalTag;

public abstract class SupporterTechnicalTagFixture {

    private SupporterTechnicalTagFixture() {
    }

    public static SupporterTechnicalTag create(final Supporter supporter, final TechnicalTag technicalTag) {
        return SupporterTechnicalTag.builder()
                .supporter(supporter)
                .technicalTag(technicalTag)
                .build();
    }
}
