package touch.baton.fixture.repository;

import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.supporter.repository.SupporterRepository;

public class SupporterRepositoryFixture {

    private final SupporterRepository supporterRepository;

    public SupporterRepositoryFixture(final SupporterRepository supporterRepository) {
        this.supporterRepository = supporterRepository;
    }

    public Supporter save(final Supporter supporter) {
        return supporterRepository.save(supporter);
    }
}
