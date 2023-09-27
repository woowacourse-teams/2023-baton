package touch.baton.fixture.domain;

import touch.baton.tobe.domain.feedback.command.SupporterFeedback;
import touch.baton.tobe.domain.feedback.command.vo.Description;
import touch.baton.tobe.domain.feedback.command.vo.ReviewType;
import touch.baton.tobe.domain.member.command.Runner;
import touch.baton.tobe.domain.member.command.Supporter;
import touch.baton.tobe.domain.runnerpost.command.RunnerPost;

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
