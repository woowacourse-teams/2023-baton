package touch.baton.assure.runnerpost;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import touch.baton.config.AssuredTestConfig;
import touch.baton.domain.common.response.PageResponse;
import touch.baton.domain.member.Member;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.controller.response.RunnerPostResponse;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.RunnerPostFixture;

import java.util.List;

import static java.time.LocalDateTime.now;
import static touch.baton.assure.runnerpost.RunnerPostAssuredSupport.러너_게시글_전체_조회_응답;
import static touch.baton.fixture.vo.DeadlineFixture.deadline;
import static touch.baton.fixture.vo.IntroductionFixture.introduction;

@SuppressWarnings("NonAsciiCharacters")
class RunnerPostReadAssuredTest extends AssuredTestConfig {

    @Test
    void 러너_게시글_전체_조회에_성공한다() {
        final Runner 러너_에단 = 러너를_저장한다(MemberFixture.createEthan());
        final RunnerPost 러너_에단의_게시글 = 러너_게시글을_등록한다(러너_에단);
        runnerPostRepository.save(러너_에단의_게시글);

        final String 에단_액세스_토큰 = login(러너_에단.getMember().getSocialId().getValue());

        final PageRequest 페이징_정보 = PageRequest.of(1, 10);
        final RunnerPostResponse.Simple 게시글_응답
                = RunnerPostResponse.Simple.from(러너_에단의_게시글, 0);
        final PageResponse<RunnerPostResponse.Simple> 페이징된_게시글_응답
                = 러너_게시글_전체_조회_응답(페이징_정보, List.of(게시글_응답));

        RunnerPostAssuredSupport
                .클라이언트_요청()
                .토큰으로_로그인한다(에단_액세스_토큰)
                .전체_러너_게시글_페이징을_조회한다(페이징_정보)

                .서버_응답()
                .전체_러너_게시글_페이징_조회_성공을_검증한다(페이징된_게시글_응답);
    }

    private RunnerPost 러너_게시글을_등록한다(final Runner 러너) {
        return runnerPostRepository.save(RunnerPostFixture.create(러너, deadline(now().plusHours(100))));
    }

    private Runner 러너를_저장한다(final Member member) {
        final Member 저장된_사용자 = memberRepository.save(member);

        return runnerRepository.save(RunnerFixture.createRunner(introduction("안녕하세요"), 저장된_사용자));
    }
}
