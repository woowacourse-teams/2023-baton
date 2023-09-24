package touch.baton.domain.tag.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import touch.baton.config.RepositoryTestConfig;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.tag.RunnerPostTag;
import touch.baton.domain.tag.Tag;
import touch.baton.fixture.domain.MemberFixture;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RunnerPostTagRepositoryTest extends RepositoryTestConfig {

    @Autowired
    private RunnerPostTagRepository runnerPostTagRepository;

    @DisplayName("RunnerPostTag 의 식별자값 목록으로 Tag 목록을 조회한다.")
    @Test
    void success_joinTagByRunnerPostId() {
        // given
        final Runner runner = persistRunner(MemberFixture.createDitoo());
        final RunnerPost runnerPost = persistRunnerPost(runner);
        final Tag tag = persistTag("java");
        final RunnerPostTag runnerPostTag = persistRunnerPostTag(runnerPost, tag);

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
        final Runner runner = persistRunner(MemberFixture.createDitoo());
        final RunnerPost runnerPost = persistRunnerPost(runner);

        em.flush();
        em.close();

        // when
        final List<RunnerPostTag> joinRunnerPostTags = runnerPostTagRepository.joinTagByRunnerPostId(runnerPost.getId());

        // then
        assertThat(joinRunnerPostTags).isEmpty();
    }
}
