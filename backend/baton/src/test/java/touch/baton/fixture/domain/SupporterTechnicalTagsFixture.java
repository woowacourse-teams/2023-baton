package touch.baton.fixture.domain;

import touch.baton.domain.technicaltag.command.SupporterTechnicalTag;
import touch.baton.domain.technicaltag.command.SupporterTechnicalTags;

import java.util.List;

public abstract class SupporterTechnicalTagsFixture {

    private SupporterTechnicalTagsFixture() {
    }

    public static SupporterTechnicalTags create(final List<SupporterTechnicalTag> supporterTechnicalTags) {
        return new SupporterTechnicalTags(supporterTechnicalTags);
    }
}
