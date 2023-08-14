package touch.baton.assure.runnerpost;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import touch.baton.config.AssuredTestConfig;
import touch.baton.domain.common.response.PageResponse;
import touch.baton.domain.member.Member;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.controller.response.RunnerPostResponse;
import touch.baton.domain.runnerpost.vo.ReviewStatus;
import touch.baton.domain.supporter.Supporter;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.RunnerPostFixture;
import touch.baton.fixture.domain.SupporterFixture;
import touch.baton.fixture.domain.SupporterRunnerPostFixture;

import java.time.LocalDateTime;
import java.util.List;

import static touch.baton.assure.runnerpost.RunnerPostAssuredSupport.서포터가_리뷰_완료한_러너_게시글_응답;
import static touch.baton.fixture.vo.DeadlineFixture.deadline;

@SuppressWarnings("NonAsciiCharacters")
public class RunnerPostReadWithLoginedSupporterAssuredTest extends AssuredTestConfig {

    private Supporter 로그인된_서포터;
    private String 로그인용_토큰;
    private final PageRequest 페이징_정보 = PageRequest.of(1, 10);
    private RunnerPost 대기중인_게시글;
    private RunnerPost 리뷰중인_게시글;
    private RunnerPost 완료된_게시글;

    @BeforeEach
    void setUp() {
        final Member 로그인된_사용자 = memberRepository.save(MemberFixture.createDitoo());
        로그인된_서포터 = supporterRepository.save(SupporterFixture.create(로그인된_사용자));
        로그인용_토큰 = login(로그인된_사용자.getSocialId().getValue());
        로그인된_서포터의_러너_게시글을_모든_리뷰_상태로_저장한다(로그인된_서포터);
    }

    private void 로그인된_서포터의_러너_게시글을_모든_리뷰_상태로_저장한다(final Supporter 로그인된_서포터) {
        final Member 사용자_누군가 = memberRepository.save(MemberFixture.createEthan());
        final Runner 러너_누군가 = runnerRepository.save(RunnerFixture.createRunner(사용자_누군가));
        대기중인_게시글 = runnerPostRepository.save(RunnerPostFixture.create(
                러너_누군가,
                null,
                deadline(LocalDateTime.now().plusHours(100)),
                ReviewStatus.NOT_STARTED
        ));
        supporterRunnerPostRepository.save(SupporterRunnerPostFixture.create(대기중인_게시글, 로그인된_서포터));

        리뷰중인_게시글 = runnerPostRepository.save(RunnerPostFixture.create(
                러너_누군가,
                로그인된_서포터,
                deadline(LocalDateTime.now().plusHours(100)),
                ReviewStatus.IN_PROGRESS
        ));
        supporterRunnerPostRepository.save(SupporterRunnerPostFixture.create(리뷰중인_게시글, 로그인된_서포터));

        완료된_게시글 = runnerPostRepository.save(RunnerPostFixture.create(
                러너_누군가,
                로그인된_서포터,
                deadline(LocalDateTime.now().plusHours(100)),
                ReviewStatus.DONE
        ));
        supporterRunnerPostRepository.save(SupporterRunnerPostFixture.create(완료된_게시글, 로그인된_서포터));
    }

    @Test
    void 로그인된_서포터의_대기중인_러너_게시글_목록_조회에_성공한다() {
        final RunnerPostResponse.ReferencedBySupporter 서포터가_지원한_러너_게시글_응답
                = RunnerPostResponse.ReferencedBySupporter.of(대기중인_게시글, 1);
        final PageResponse<RunnerPostResponse.ReferencedBySupporter> 페이징된_서포터가_지원한_러너_게시글_응답
                = 서포터가_리뷰_완료한_러너_게시글_응답(페이징_정보, List.of(서포터가_지원한_러너_게시글_응답));

        RunnerPostAssuredSupport
                .클라이언트_요청()
                .토큰으로_로그인한다(로그인용_토큰)
                .로그인한_서포터의_러너_게시글_페이징을_조회한다(ReviewStatus.NOT_STARTED, 페이징_정보)

                .서버_응답()
                .서포터와_연관된_러너_게시글_페이징_조회_성공을_검증한다(페이징된_서포터가_지원한_러너_게시글_응답);
    }

    @Test
    void 로그인된_서포터의_진행중인_러너_게시글_목록_조회에_성공한다() {
        final RunnerPostResponse.ReferencedBySupporter 서포터가_리뷰중인_러너_게시글_응답
                = RunnerPostResponse.ReferencedBySupporter.of(리뷰중인_게시글, 1);
        final PageResponse<RunnerPostResponse.ReferencedBySupporter> 페이징된_서포터가_리뷰중인_러너_게시글_응답
                = 서포터가_리뷰_완료한_러너_게시글_응답(페이징_정보, List.of(서포터가_리뷰중인_러너_게시글_응답));

        RunnerPostAssuredSupport
                .클라이언트_요청()
                .토큰으로_로그인한다(로그인용_토큰)
                .로그인한_서포터의_러너_게시글_페이징을_조회한다(ReviewStatus.IN_PROGRESS, 페이징_정보)

                .서버_응답()
                .서포터와_연관된_러너_게시글_페이징_조회_성공을_검증한다(페이징된_서포터가_리뷰중인_러너_게시글_응답);
    }

    @Test
    void 로그인된_서포터의_완료된_러너_게시글_목록_조회에_성공한다() {
        final RunnerPostResponse.ReferencedBySupporter 서포터가_리뷰_완료한_러너_게시글_응답
                = RunnerPostResponse.ReferencedBySupporter.of(완료된_게시글, 1);
        final PageResponse<RunnerPostResponse.ReferencedBySupporter> 페이징된_서포터가_리뷰_완료한_러너_게시글_응답
                = 서포터가_리뷰_완료한_러너_게시글_응답(페이징_정보, List.of(서포터가_리뷰_완료한_러너_게시글_응답));

        RunnerPostAssuredSupport
                .클라이언트_요청()
                .토큰으로_로그인한다(로그인용_토큰)
                .로그인한_서포터의_러너_게시글_페이징을_조회한다(ReviewStatus.DONE, 페이징_정보)

                .서버_응답()
                .서포터와_연관된_러너_게시글_페이징_조회_성공을_검증한다(페이징된_서포터가_리뷰_완료한_러너_게시글_응답);
    }
}
