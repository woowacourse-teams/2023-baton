package touch.baton.domain.runnerpost.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import touch.baton.domain.common.vo.Contents;
import touch.baton.domain.common.vo.Title;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.repository.RunnerPostRepository;
import touch.baton.domain.runnerpost.service.dto.RunnerPostCreateRequest;
import touch.baton.domain.runnerpost.vo.Deadline;
import touch.baton.domain.runnerpost.vo.PullRequestUrl;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RunnerPostService {

    private final RunnerPostRepository runnerPostRepository;

    public void update(final Long runnerPostId, final RunnerPostCreateRequest request) {
        // TODO: 메소드 분리
        RunnerPost presentRunnerPost = runnerPostRepository.findById(runnerPostId).get();

        // TODO: 현재 로그인한 Runner 인지 확인
        RunnerPost updatedRunnerPost = RunnerPost.builder()
                .title(new Title(request.getTitle()))
                .contents(new Contents(request.getContents()))
                .pullRequestUrl(new PullRequestUrl(request.getPullRequestUrl()))
                .deadline(Deadline.from(request.getDeadline()))
                .watchedCount(presentRunnerPost.getWatchedCount())
                .chattingRoomCount(presentRunnerPost.getChattingRoomCount())
                .runnerPostTags(presentRunnerPost.getRunnerPostTags())
                .runner(presentRunnerPost.getRunner())
                .supporter(presentRunnerPost.getSupporter())
                .build();

        runnerPostRepository.save(updatedRunnerPost);
    }
}
