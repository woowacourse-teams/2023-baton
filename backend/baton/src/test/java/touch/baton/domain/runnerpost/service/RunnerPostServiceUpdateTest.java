package touch.baton.domain.runnerpost.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import touch.baton.domain.common.vo.Contents;
import touch.baton.domain.common.vo.Title;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.repository.RunnerPostData;
import touch.baton.domain.runnerpost.repository.RunnerPostRepository;
import touch.baton.domain.runnerpost.service.dto.RunnerPostUpdateRequest;
import touch.baton.domain.runnerpost.vo.Deadline;
import touch.baton.domain.runnerpost.vo.PullRequestUrl;
import touch.baton.domain.tag.RunnerPostTag;
import touch.baton.domain.tag.repository.RunnerPostTagRepository;
import touch.baton.domain.tag.repository.TagRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class RunnerPostServiceUpdateTest extends RunnerPostData {

    private static final String TITLE = "코드 리뷰 해주세요.";
    private static final String TAG = "java";
    private static final String OTHER_TAG = "spring";
    private static final String PULL_REQUEST_URL = "https://github.com/shb03323";
    private static final LocalDateTime DEADLINE = LocalDateTime.of(2023, 8, 30, 12, 10);
    private static final String CONTENTS = "싸게 부탁드려요.";

    private RunnerPostService runnerPostService;

    @Autowired
    private RunnerPostRepository runnerPostRepository;

    @Autowired
    private RunnerPostTagRepository runnerPostTagRepository;

    @Autowired
    private TagRepository tagRepository;

    @BeforeEach
    void setUp() {
        super.setData();
        runnerPostService = new RunnerPostService(runnerPostRepository, runnerPostTagRepository, tagRepository);
    }

    @DisplayName("Runner Post 수정에 성공한다.")
    @Test
    void success() {
        // given
        RunnerPostUpdateRequest request = new RunnerPostUpdateRequest(
                TITLE, List.of(TAG, OTHER_TAG), PULL_REQUEST_URL, DEADLINE, CONTENTS);

        // when
        Long savedId = runnerPostService.update(runnerPost.getId(), request);

        // then
        assertThat(savedId).isNotNull();
        RunnerPost actual = runnerPostRepository.findById(savedId).get();
        assertAll(
                () -> assertThat(actual.getTitle()).isEqualTo(new Title(TITLE)),
                () -> assertThat(actual.getContents()).isEqualTo(new Contents(CONTENTS)),
                () -> assertThat(actual.getPullRequestUrl()).isEqualTo(new PullRequestUrl(PULL_REQUEST_URL)),
                () -> assertThat(actual.getDeadline()).isEqualTo(new Deadline(request.getDeadline()))
        );

        List<RunnerPostTag> runnerPostTags = runnerPostTagRepository.joinTagsByRunnerPostId(savedId);
        assertThat(
                runnerPostTags.stream()
                        .map(runnerPostTag -> runnerPostTag.getTag().getTagName().getValue())
                        .toList()
        ).containsExactly(TAG, OTHER_TAG);
    }
}
