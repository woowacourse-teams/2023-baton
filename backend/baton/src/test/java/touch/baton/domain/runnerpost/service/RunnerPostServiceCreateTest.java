package touch.baton.domain.runnerpost.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import touch.baton.domain.common.vo.ChattingRoomCount;
import touch.baton.domain.common.vo.Contents;
import touch.baton.domain.common.vo.Title;
import touch.baton.domain.common.vo.WatchedCount;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.repository.RunnerPostRepository;
import touch.baton.domain.runnerpost.service.dto.RunnerPostCreateRequest;
import touch.baton.domain.runnerpost.vo.Deadline;
import touch.baton.domain.runnerpost.vo.PullRequestUrl;
import touch.baton.domain.tag.repository.TagRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class RunnerPostServiceCreateTest extends RunnerFixture {

    private static final String TITLE = "코드 리뷰 해주세요.";
    private static final String TAG = "Java";
    private static final String OTHER_TAG = "Spring";
    private static final String PULL_REQUEST_URL = "https://github.com/cookienc";
    private static final LocalDateTime DEADLINE = LocalDateTime.of(2099, 12, 12, 0, 0);
    private static final String CONTENTS = "싸게 부탁드려요.";

    private RunnerPostService runnerPostService;

    @Autowired
    private RunnerPostRepository runnerPostRepository;

    @Autowired
    private TagRepository tagRepository;

    @BeforeEach
    void setUp() {
        runnerPostService = new RunnerPostService(runnerPostRepository, tagRepository);
    }

    @DisplayName("Runner post 저장에 성공한다.")
    @Test
    void success() {
        // given
        final RunnerPostCreateRequest request = new RunnerPostCreateRequest(TITLE,
                List.of(TAG, OTHER_TAG),
                PULL_REQUEST_URL,
                DEADLINE,
                CONTENTS);

        // when
        final Long savedId = runnerPostService.create(runner, request);

        // then
        assertThat(savedId).isNotNull();
        RunnerPost actual = runnerPostRepository.findById(savedId).get();
        assertAll(
                () -> assertThat(actual.getTitle()).isEqualTo(new Title(TITLE)),
                () -> assertThat(actual.getContents()).isEqualTo(new Contents(CONTENTS)),
                () -> assertThat(actual.getPullRequestUrl()).isEqualTo(new PullRequestUrl(PULL_REQUEST_URL)),
                () -> assertThat(actual.getDeadline()).isEqualTo(new Deadline(DEADLINE)),
                () -> assertThat(actual.getWatchedCount()).isEqualTo(new WatchedCount(0)),
                () -> assertThat(actual.getChattingRoomCount()).isEqualTo(new ChattingRoomCount(0)),
                () -> assertThat(actual.getRunner()).isEqualTo(runner)
        );
    }
}
