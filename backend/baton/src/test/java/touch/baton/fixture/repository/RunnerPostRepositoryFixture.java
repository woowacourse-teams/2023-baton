package touch.baton.fixture.repository;

import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.repository.RunnerPostRepository;

public class RunnerPostRepositoryFixture {

    private final RunnerPostRepository runnerPostRepository;

    public RunnerPostRepositoryFixture(final RunnerPostRepository runnerPostRepository) {
        this.runnerPostRepository = runnerPostRepository;
    }

    public RunnerPost save(final RunnerPost runnerPost) {
        return runnerPostRepository.save(runnerPost);
    }
}
