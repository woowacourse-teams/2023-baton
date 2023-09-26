package touch.baton.domain.feedback.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import touch.baton.domain.feedback.SupporterFeedback;
import touch.baton.domain.feedback.exception.FeedbackBusinessException;
import touch.baton.domain.feedback.repository.SupporterFeedbackRepository;
import touch.baton.domain.feedback.vo.Description;
import touch.baton.domain.feedback.vo.ReviewType;
import touch.baton.tobe.domain.member.command.Runner;
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
    private final RunnerPostRepository runnerPostRepository;
    private final SupporterRepository supporterRepository;

    @Transactional
    public Long createSupporterFeedback(final Runner runner, final SupporterFeedBackCreateRequest request) {
        final Supporter foundSupporter = supporterRepository.findById(request.supporterId())
                .orElseThrow(() -> new FeedbackBusinessException("서포터를 찾을 수 없습니다."));
        final RunnerPost foundRunnerPost = runnerPostRepository.findById(request.runnerPostId())
                .orElseThrow(() -> new FeedbackBusinessException("러너 게시글을 찾을 수 없습니다."));

        if (supporterFeedbackRepository.existsByRunnerPostIdAndSupporterId(foundRunnerPost.getId(), foundSupporter.getId())) {
            throw new FeedbackBusinessException("서포터에 대한 피드백을 작성했으면 추가적인 피드백을 남길 수 없습니다.");
        }
        if (foundRunnerPost.isNotOwner(runner)) {
            throw new FeedbackBusinessException("리뷰 글을 작성한 주인만 글을 작성할 수 있습니다.");
        }
        if (foundRunnerPost.isDifferentSupporter(foundSupporter)) {
            throw new FeedbackBusinessException("리뷰를 작성한 서포터에 대해서만 피드백을 작성할 수 있습니다.");
        }

        foundRunnerPost.finishFeedback();
        final SupporterFeedback supporterFeedback = SupporterFeedback.builder()
                .reviewType(ReviewType.valueOf(request.reviewType()))
                .description(new Description(String.join(DELIMITER, request.descriptions())))
                .runner(runner)
                .supporter(foundSupporter)
                .runnerPost(foundRunnerPost)
                .build();

        return supporterFeedbackRepository.save(supporterFeedback).getId();
    }
}
