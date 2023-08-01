package touch.baton.assure.runnerpost;

import org.junit.jupiter.api.Test;
import touch.baton.assure.fixture.RunnerFixture;
import touch.baton.assure.fixture.RunnerPostFixture;
import touch.baton.config.AssuredTestConfig;
import touch.baton.domain.common.vo.Grade;
import touch.baton.domain.member.Member;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.tag.RunnerPostTags;
import touch.baton.fixture.domain.MemberFixture;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static touch.baton.fixture.vo.TotalRatingFixture.totalRating;

class RunnerPostAssuredDeleteTest extends AssuredTestConfig {

    @Test
    void 러너의_게시글_식별자값으로_러너_게시글_상세_정보_삭제에_성공한다() {
        final Member member = MemberFixture.createHyena();
        memberRepository.save(member);

        final Runner runner = RunnerFixture.from(member, totalRating(0), Grade.BARE_FOOT);
        runnerRepository.save(runner);

        final RunnerPost runnerPost = RunnerPostFixture.from(runner,
                null,
                "제 코드를 리뷰해주세요",
                "제 코드의 내용은 이렇습니다.",
                "https://",
                LocalDateTime.now(),
                0,
                0,
                new RunnerPostTags(Collections.emptyList())
        );
        runnerPostRepository.save(runnerPost);

        RunnerPostAssuredSupport
                .클라이언트_요청().러너_게시글_식별자값으로_러너_게시글을_삭제한다(runnerPost.getId())
                .서버_응답().러너_게시글_삭제_성공을_검증한다(NO_CONTENT);
    }
}
