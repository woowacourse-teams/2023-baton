package touch.baton.tobe.domain.feedback.command.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import touch.baton.tobe.domain.feedback.command.SupporterFeedback;
import touch.baton.tobe.domain.feedback.command.repository.SupporterFeedbackCommandRepository;
import touch.baton.tobe.domain.feedback.command.service.dto.SupporterFeedBackCreateRequest;
import touch.baton.tobe.domain.feedback.command.vo.Description;
import touch.baton.tobe.domain.feedback.command.vo.ReviewType;
import touch.baton.tobe.domain.feedback.exception.FeedbackBusinessException;
import touch.baton.tobe.domain.member.command.Runner;
import touch.baton.tobe.domain.member.command.Supporter;
import touch.baton.tobe.domain.member.command.repository.SupporterCommandRepository;
import touch.baton.tobe.domain.runnerpost.command.RunnerPost;
import touch.baton.tobe.domain.runnerpost.command.repository.RunnerPostCommandRepository;

@RequiredArgsConstructor
@Transactional
@Service
public class FeedbackCommandService {

    private static final String DELIMITER = "|";

    private final SupporterFeedbackCommandRepository supporterFeedbackCommandRepository;
    private final RunnerPostCommandRepository runnerPostCommandRepository;
    private final SupporterCommandRepository supporterCommandRepository;

    public Long createSupporterFeedback(final Runner runner, final SupporterFeedBackCreateRequest request) {
        final Supporter foundSupporter = supporterCommandRepository.findById(request.supporterId())
                .orElseThrow(() -> new FeedbackBusinessException("서포터를 찾을 수 없습니다."));
        final RunnerPost foundRunnerPost = runnerPostCommandRepository.findById(request.runnerPostId())
                .orElseThrow(() -> new FeedbackBusinessException("러너 게시글을 찾을 수 없습니다."));

        if (supporterFeedbackCommandRepository.existsByRunnerPostIdAndSupporterId(foundRunnerPost.getId(), foundSupporter.getId())) {
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

        return supporterFeedbackCommandRepository.save(supporterFeedback).getId();
    }
}
