package touch.baton.fixture.repository;

import touch.baton.domain.runner.Runner;
import touch.baton.domain.runner.repository.RunnerRepository;

public class RunnerRepositoryFixture {

    private final RunnerRepository runnerRepository;

    public RunnerRepositoryFixture(final RunnerRepository runnerRepository) {
        this.runnerRepository = runnerRepository;
    }

    public Runner save(final Runner runner) {
        return runnerRepository.save(runner);
    }
}
