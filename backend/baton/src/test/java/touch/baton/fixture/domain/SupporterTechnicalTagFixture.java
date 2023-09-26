package touch.baton.fixture.domain;

import touch.baton.tobe.domain.member.command.Supporter;
import touch.baton.domain.technicaltag.SupporterTechnicalTag;
import touch.baton.domain.technicaltag.TechnicalTag;

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
