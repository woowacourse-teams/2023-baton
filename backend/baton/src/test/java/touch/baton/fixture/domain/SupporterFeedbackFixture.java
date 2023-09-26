package touch.baton.fixture.domain;

import touch.baton.domain.feedback.SupporterFeedback;
import touch.baton.domain.feedback.vo.Description;
import touch.baton.domain.feedback.vo.ReviewType;
import touch.baton.tobe.domain.member.command.Runner;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.tobe.domain.member.command.Supporter;

import static touch.baton.fixture.vo.DescriptionFixture.description;

public abstract class SupporterFeedbackFixture {

    private SupporterFeedbackFixture() {
    }

    public static SupporterFeedback create(final Supporter supporter,
                                           final Runner runner,
                                           final RunnerPost runnerPost
    ) {
        return SupporterFeedback.builder()
                .reviewType(ReviewType.GOOD)
                .description(description("리뷰가 친절합니다."))
                .supporter(supporter)
                .runner(runner)
                .runnerPost(runnerPost)
                .build();
    }

    public static SupporterFeedback create(final ReviewType reviewType,
                                           final Description description,
                                           final Supporter supporter,
                                           final Runner runner,
                                           final RunnerPost runnerPost
    ) {
        return SupporterFeedback.builder()
                .reviewType(reviewType)
                .description(description)
                .supporter(supporter)
                .runner(runner)
                .runnerPost(runnerPost)
                .build();
    }
}
