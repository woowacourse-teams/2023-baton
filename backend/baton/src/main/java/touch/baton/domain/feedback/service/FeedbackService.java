package touch.baton.domain.feedback.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import touch.baton.domain.feedback.SupporterFeedback;
import touch.baton.domain.feedback.exception.FeedbackBusinessException;
import touch.baton.domain.feedback.repository.SupporterFeedbackRepository;
import touch.baton.domain.feedback.vo.Description;
import touch.baton.domain.feedback.vo.ReviewType;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runner.repository.RunnerRepository;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.repository.RunnerPostRepository;
import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.supporter.repository.SupporterRepository;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FeedbackService {

    private static final String DELIMITER = "|";

    private final SupporterFeedbackRepository supporterFeedbackRepository;
    private final RunnerRepository runnerRepository;
    private final RunnerPostRepository runnerPostRepository;
    private final SupporterRepository supporterRepository;

    public Long createSupporterFeedback(final Runner runner, final SupporterFeedBackCreateRequest request) {
        final Runner foundRunner = runnerRepository.findById(runner.getId())
                .orElseThrow(() -> new FeedbackBusinessException("러너를 찾을 수 없습니다."));
        final Supporter foundSupporter = supporterRepository.findById(request.supporterId())
                .orElseThrow(() -> new FeedbackBusinessException("서포터를 찾을 수 없습니다."));
        final RunnerPost foundRunnerPost = runnerPostRepository.findById(request.runnerPostId())
                .orElseThrow(() -> new FeedbackBusinessException("러너 게시글을 찾을 수 없습니다."));

        if (foundRunnerPost.isNotOwner(foundRunner)) {
            throw new FeedbackBusinessException("리뷰 글을 작성한 주인만 글을 작성할 수 있습니다.");
        }

        if (foundRunnerPost.isDifferentSupporter(foundSupporter)) {
            throw new FeedbackBusinessException("리뷰를 작성한 서포터에 대해서만 피드백을 작성할 수 있습니다.");
        }

        final SupporterFeedback supporterFeedback = SupporterFeedback.builder()
                .reviewType(ReviewType.valueOf(request.reviewType()))
                .description(new Description(String.join(DELIMITER, request.descriptions())))
                .runner(foundRunner)
                .supporter(foundSupporter)
                .runnerPost(foundRunnerPost)
                .build();

        return supporterFeedbackRepository.save(supporterFeedback).getId();
    }
}
