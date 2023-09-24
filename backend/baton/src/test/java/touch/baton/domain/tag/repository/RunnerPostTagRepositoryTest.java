package touch.baton.domain.tag.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import touch.baton.config.RepositoryTestConfig;
import touch.baton.domain.member.Member;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.vo.Deadline;
import touch.baton.domain.tag.RunnerPostTag;
import touch.baton.domain.tag.Tag;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.RunnerPostFixture;
import touch.baton.fixture.domain.RunnerPostTagFixture;
import touch.baton.fixture.domain.TagFixture;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RunnerPostTagRepositoryTest extends RepositoryTestConfig {

    @Autowired
    private EntityManager em;

    @Autowired
    private RunnerPostTagRepository runnerPostTagRepository;

    @DisplayName("RunnerPostTag 의 식별자값 목록으로 Tag 목록을 조회한다.")
    @Test
    void success_joinTagByRunnerPostId() {
        // given
        final Runner runner = persistRunner();
        final RunnerPost runnerPost = persistRunnerPost(runner);
        final RunnerPostTag runnerPostTag = persistRunnerPostTag(runnerPost);

        em.flush();
        em.close();

        // when
        final List<RunnerPostTag> joinRunnerPostTags = runnerPostTagRepository.joinTagByRunnerPostId(runnerPost.getId());

        // then
        assertThat(joinRunnerPostTags).containsExactly(runnerPostTag);
    }

    @DisplayName("RunnerPostTag 의 식별자값 목록이 비어있을 때 빈 컬렉션을 반환한다.")
    @Test
    void success_joinTagByRunnerPostIds_if_tag_is_empty() {
        // given
        final Runner runner = persistRunner();
        final RunnerPost runnerPost = persistRunnerPost(runner);

        em.flush();
        em.close();

        // when
        final List<RunnerPostTag> joinRunnerPostTags = runnerPostTagRepository.joinTagByRunnerPostId(runnerPost.getId());

        // then
        assertThat(joinRunnerPostTags).isEmpty();
    }
    
    @DisplayName("RunnerPost 목록으로 RunnerPostTag 목록을 조회한다.")
    @Test
    void joinTagByRunnerPosts() {
        // given
        final Tag tagReact = TagFixture.createReact();
        em.persist(tagReact);
        final Tag tagJava = TagFixture.createJava();
        em.persist(tagJava);

        final List<RunnerPost> runnerPosts = new ArrayList<>();
        final List<RunnerPostTag> expected = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            final Runner runner = persistRunner();
            final RunnerPost runnerPost = persistRunnerPost(runner);
            final RunnerPostTag runnerPostTagReact = persistRunnerPostTag(runnerPost, tagReact);
            final RunnerPostTag runnerPostTagJava = persistRunnerPostTag(runnerPost, tagJava);
            runnerPosts.add(runnerPost);
            expected.addAll(List.of(runnerPostTagReact, runnerPostTagJava));
        }

        em.flush();
        em.close();

        // when
        final List<RunnerPostTag> actual = runnerPostTagRepository.joinTagByRunnerPosts(runnerPosts);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    private Runner persistRunner() {
        final Member member = MemberFixture.createDitoo();
        em.persist(member);
        final Runner runner = RunnerFixture.createRunner(member);
        em.persist(runner);
        return runner;
    }

    private RunnerPost persistRunnerPost(final Runner runner) {
        final RunnerPost runnerPost = RunnerPostFixture.create(runner, new Deadline(LocalDateTime.now().plusHours(10)));
        em.persist(runnerPost);
        return runnerPost;
    }

    private RunnerPostTag persistRunnerPostTag(final RunnerPost runnerPost) {
        final Tag tag = TagFixture.createJava();
        em.persist(tag);
        final RunnerPostTag runnerPostTag = RunnerPostTagFixture.create(runnerPost, tag);
        runnerPost.addAllRunnerPostTags(List.of(runnerPostTag));
        return runnerPostTag;
    }

    private RunnerPostTag persistRunnerPostTag(final RunnerPost runnerPost, final Tag tag) {
        final RunnerPostTag runnerPostTag = RunnerPostTagFixture.create(runnerPost, tag);
        runnerPost.addAllRunnerPostTags(List.of(runnerPostTag));
        return runnerPostTag;
    }
}
