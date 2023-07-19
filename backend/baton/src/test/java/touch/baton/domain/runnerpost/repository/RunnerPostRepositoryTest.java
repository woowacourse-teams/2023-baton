package touch.baton.domain.runnerpost.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import touch.baton.domain.tag.RunnerPostTag;
import touch.baton.domain.tag.repository.RunnerPostTagRepository;

import java.util.List;

class RunnerPostRepositoryTest extends RunnerPostData {

    @Autowired
    private RunnerPostTagRepository runnerPostTagRepository;

    @BeforeEach
    void setUp() {
        super.setData();
    }

    @DisplayName("id로 RunnerPostTag 목록을 조회할 때 Tag 가 있으면 조회된다.")
    @Test
    void findRunnerPostTagsById_exist() {
        // when
        List<RunnerPostTag> expected = runnerPostTagRepository.joinTagsByRunnerPostId(runnerPost.getId());

        // then
        Assertions.assertThat(expected).containsExactly(runnerPostTagJava, runnerPostTagSpring);
    }
}
