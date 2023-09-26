package touch.baton.config;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import touch.baton.tobe.domain.member.command.Member;
import touch.baton.tobe.domain.member.command.Runner;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.tobe.domain.member.command.Supporter;
import touch.baton.tobe.domain.member.command.SupporterRunnerPost;
import touch.baton.domain.tag.RunnerPostTag;
import touch.baton.domain.tag.Tag;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.RunnerPostFixture;
import touch.baton.fixture.domain.RunnerPostTagFixture;
import touch.baton.fixture.domain.SupporterFixture;
import touch.baton.fixture.domain.SupporterRunnerPostFixture;
import touch.baton.fixture.domain.TagFixture;

import java.time.LocalDateTime;

import static touch.baton.fixture.vo.DeadlineFixture.deadline;
import static touch.baton.fixture.vo.TagNameFixture.tagName;

@Import({JpaConfig.class, QuerydslConfig.class})
@DataJpaTest
public abstract class RepositoryTestConfig {

    @Autowired
    protected EntityManager em;

    protected Runner persistRunner(final Member member) {
        em.persist(member);
        final Runner runner = RunnerFixture.createRunner(member);
        em.persist(runner);
        return runner;
    }

    protected Supporter persistSupporter(final Member member) {
        em.persist(member);
        final Supporter supporter = SupporterFixture.create(member);
        em.persist(supporter);
        return supporter;
    }

    protected RunnerPost persistRunnerPost(final Runner runner) {
        final RunnerPost runnerPost = RunnerPostFixture.create(runner, deadline(LocalDateTime.now().plusHours(100)));
        em.persist(runnerPost);
        return runnerPost;
    }

    protected SupporterRunnerPost persistApplicant(final Supporter supporter, final RunnerPost runnerPost) {
        final SupporterRunnerPost applicant = SupporterRunnerPostFixture.create(runnerPost, supporter);
        em.persist(applicant);
        return applicant;
    }

    protected Tag persistTag(final String tagName) {
        final Tag tag = TagFixture.create(tagName(tagName));
        em.persist(tag);
        return tag;
    }

    protected RunnerPostTag persistRunnerPostTag(final RunnerPost runnerPost, final Tag tag) {
        final RunnerPostTag runnerPostTag = RunnerPostTagFixture.create(runnerPost, tag);
        em.persist(runnerPostTag);
        return runnerPostTag;
    }
}
