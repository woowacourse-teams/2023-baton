package touch.baton.domain.runnerpost.query.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import touch.baton.config.ServiceTestConfig;
import touch.baton.domain.common.request.PageParams;
import touch.baton.domain.common.response.PageResponse;
import touch.baton.domain.common.vo.TagName;
import touch.baton.domain.member.command.Member;
import touch.baton.domain.member.command.Runner;
import touch.baton.domain.member.command.Supporter;
import touch.baton.domain.member.command.vo.Company;
import touch.baton.domain.member.command.vo.GithubUrl;
import touch.baton.domain.member.command.vo.ImageUrl;
import touch.baton.domain.member.command.vo.MemberName;
import touch.baton.domain.member.command.vo.OauthId;
import touch.baton.domain.member.command.vo.SocialId;
import touch.baton.domain.runnerpost.command.RunnerPost;
import touch.baton.domain.runnerpost.command.exception.RunnerPostBusinessException;
import touch.baton.domain.runnerpost.command.vo.CuriousContents;
import touch.baton.domain.runnerpost.command.vo.Deadline;
import touch.baton.domain.runnerpost.command.vo.ImplementedContents;
import touch.baton.domain.runnerpost.command.vo.IsReviewed;
import touch.baton.domain.runnerpost.command.vo.PostscriptContents;
import touch.baton.domain.runnerpost.command.vo.PullRequestUrl;
import touch.baton.domain.runnerpost.command.vo.ReviewStatus;
import touch.baton.domain.runnerpost.command.vo.Title;
import touch.baton.domain.runnerpost.command.vo.WatchedCount;
import touch.baton.domain.runnerpost.query.controller.response.RunnerPostResponse;
import touch.baton.domain.tag.command.RunnerPostTag;
import touch.baton.domain.tag.command.RunnerPostTags;
import touch.baton.domain.tag.command.Tag;
import touch.baton.domain.tag.command.vo.TagReducedName;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.RunnerPostFixture;
import touch.baton.fixture.domain.RunnerPostTagFixture;
import touch.baton.fixture.domain.RunnerTechnicalTagsFixture;
import touch.baton.fixture.domain.SupporterFixture;
import touch.baton.fixture.domain.SupporterRunnerPostFixture;
import touch.baton.fixture.domain.TagFixture;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static touch.baton.domain.runnerpost.command.vo.ReviewStatus.IN_PROGRESS;
import static touch.baton.domain.runnerpost.command.vo.ReviewStatus.NOT_STARTED;
import static touch.baton.fixture.vo.DeadlineFixture.deadline;
import static touch.baton.fixture.vo.TagNameFixture.tagName;

class RunnerPostQueryServiceTest extends ServiceTestConfig {

    private RunnerPostQueryService runnerPostQueryService;

    @BeforeEach
    void setUp() {
        runnerPostQueryService = new RunnerPostQueryService(
                runnerPostQueryRepository,
                runnerPostPageRepository,
                runnerPostTagQueryRepository,
                supporterRunnerPostQueryRepository);
    }

    @DisplayName("태그 이름과 리뷰 상태를 조건으로 러너 게시글 첫 페이지 조회에 성공한다.")
    @Test
    void readRunnerPostByPageInfoAndTagNameAndReviewStatus_firstPage() {
        // given
        final Member hyenaMember = memberCommandRepository.save(MemberFixture.createHyena());
        final Runner hyenaRunner = runnerQueryRepository.save(RunnerFixture.createRunner(hyenaMember));

        final Tag javaTag = tagCommandRepository.save(TagFixture.create(tagName("자바")));
        final Tag springTag = tagCommandRepository.save(TagFixture.create(tagName("스프링")));
        final int totalJavaTagPost = 3;

        final RunnerPost expectedRunnerPostOne = runnerPostQueryRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(LocalDateTime.now().plusHours(100)),
                List.of(javaTag, springTag),
                NOT_STARTED
        ));

        runnerPostQueryRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(LocalDateTime.now().minusHours(100)),
                List.of(javaTag),
                ReviewStatus.OVERDUE
        ));

        final RunnerPost expectedRunnerPostTwo = runnerPostQueryRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(LocalDateTime.now().plusHours(100)),
                List.of(javaTag),
                NOT_STARTED
        ));

        runnerPostQueryRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(LocalDateTime.now().plusHours(100)),
                List.of(springTag),
                NOT_STARTED
        ));

        // when
        final PageParams pageParams = new PageParams(null, 10);
        final PageResponse<RunnerPostResponse.Simple> actual = runnerPostQueryService.pageRunnerPostByTagNameAndReviewStatus(
                javaTag.getTagName().getValue(),
                pageParams,
                NOT_STARTED
        );

        final List<RunnerPostTag> runnerPostTags = List.of(
                RunnerPostTagFixture.create(expectedRunnerPostOne, javaTag),
                RunnerPostTagFixture.create(expectedRunnerPostOne, springTag),
                RunnerPostTagFixture.create(expectedRunnerPostTwo, javaTag)
        );
        final PageResponse<RunnerPostResponse.Simple> expected = PageResponse.of(
                List.of(RunnerPostResponse.Simple.of(expectedRunnerPostTwo, 0L, runnerPostTags),
                        RunnerPostResponse.Simple.of(expectedRunnerPostOne, 0L, runnerPostTags)),
                pageParams,
                totalJavaTagPost
        );

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("태그 이름과 리뷰 상태를 조건으로 러너 게시글 중간 페이지 조회에 성공한다.")
    @Test
    void readRunnerPostByPageInfoAndTagNameAndReviewStatus_middlePage() {
        // given
        final Member hyenaMember = memberCommandRepository.save(MemberFixture.createHyena());
        final Runner hyenaRunner = runnerQueryRepository.save(RunnerFixture.createRunner(hyenaMember));

        final Tag javaTag = tagCommandRepository.save(TagFixture.create(tagName("자바")));
        final Tag springTag = tagCommandRepository.save(TagFixture.create(tagName("스프링")));
        final int totalJavaTagPost = 4;

        final RunnerPost expectedRunnerPostOne = runnerPostQueryRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(LocalDateTime.now().plusHours(100)),
                List.of(javaTag, springTag),
                NOT_STARTED
        ));

        runnerPostQueryRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(LocalDateTime.now().minusHours(100)),
                List.of(javaTag, springTag),
                ReviewStatus.OVERDUE
        ));

        final RunnerPost expectedRunnerPostTwo = runnerPostQueryRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(LocalDateTime.now().plusHours(100)),
                List.of(javaTag),
                NOT_STARTED
        ));

        final RunnerPost previousRunnerPost = runnerPostQueryRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(now().plusHours(100)),
                List.of(javaTag),
                NOT_STARTED
        ));

        runnerPostQueryRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(now().plusHours(100)),
                List.of(springTag),
                NOT_STARTED
        ));

        // when
        final PageParams pageParams = new PageParams(previousRunnerPost.getId(), 10);
        final PageResponse<RunnerPostResponse.Simple> actual = runnerPostQueryService.pageRunnerPostByTagNameAndReviewStatus(
                javaTag.getTagName().getValue(),
                pageParams,
                NOT_STARTED
        );

        final List<RunnerPostTag> runnerPostTags = List.of(
                RunnerPostTagFixture.create(expectedRunnerPostOne, javaTag),
                RunnerPostTagFixture.create(expectedRunnerPostOne, springTag),
                RunnerPostTagFixture.create(expectedRunnerPostTwo, javaTag)
        );
        final PageResponse<RunnerPostResponse.Simple> expected = PageResponse.of(
                List.of(RunnerPostResponse.Simple.of(expectedRunnerPostTwo, 0L, runnerPostTags),
                        RunnerPostResponse.Simple.of(expectedRunnerPostOne, 0L, runnerPostTags)),
                pageParams,
                totalJavaTagPost
        );

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("리뷰 상태를 조건으로 러너 게시글 첫 페이지 조회에 성공한다.")
    @Test
    void readRunnerPostByPageInfoAndReviewStatus_firstPage() {
        // given
        final Member hyenaMember = memberCommandRepository.save(MemberFixture.createHyena());
        final Runner hyenaRunner = runnerQueryRepository.save(RunnerFixture.createRunner(hyenaMember));

        final Tag javaTag = tagCommandRepository.save(TagFixture.create(tagName("자바")));
        final Tag springTag = tagCommandRepository.save(TagFixture.create(tagName("스프링")));
        final int totalPost = 4;

        final RunnerPost expectedRunnerPostOne = runnerPostQueryRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(LocalDateTime.now().plusHours(100)),
                List.of(javaTag, springTag),
                NOT_STARTED
        ));

        final RunnerPost expectedRunnerPostTwo = runnerPostQueryRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(LocalDateTime.now().plusHours(100)),
                List.of(javaTag),
                NOT_STARTED
        ));

        final RunnerPost expectedRunnerPostThree = runnerPostQueryRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(now().plusHours(100)),
                List.of(springTag),
                NOT_STARTED
        ));

        runnerPostQueryRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(now().minusHours(100)),
                List.of(springTag),
                ReviewStatus.OVERDUE
        ));

        // when
        final PageParams pageParams = new PageParams(null, 10);
        final PageResponse<RunnerPostResponse.Simple> actual = runnerPostQueryService.pageRunnerPostByTagNameAndReviewStatus(
                null,
                pageParams,
                NOT_STARTED
        );

        final List<RunnerPostTag> runnerPostTags = List.of(
                RunnerPostTagFixture.create(expectedRunnerPostOne, javaTag),
                RunnerPostTagFixture.create(expectedRunnerPostOne, springTag),
                RunnerPostTagFixture.create(expectedRunnerPostTwo, javaTag),
                RunnerPostTagFixture.create(expectedRunnerPostThree, springTag)
        );

        final PageResponse<RunnerPostResponse.Simple> expected = PageResponse.of(
                List.of(RunnerPostResponse.Simple.of(expectedRunnerPostThree, 0L, runnerPostTags),
                        RunnerPostResponse.Simple.of(expectedRunnerPostTwo, 0L, runnerPostTags),
                        RunnerPostResponse.Simple.of(expectedRunnerPostOne, 0L, runnerPostTags)),
                pageParams,
                totalPost
        );

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("리뷰 상태를 조건으로 러너 게시글 중간 페이지 조회에 성공한다.")
    @Test
    void readRunnerPostByPageInfoAndReviewStatus_middlePage() {
        // given
        final Member hyenaMember = memberCommandRepository.save(MemberFixture.createHyena());
        final Runner hyenaRunner = runnerQueryRepository.save(RunnerFixture.createRunner(hyenaMember));

        final Tag javaTag = tagCommandRepository.save(TagFixture.create(tagName("자바")));
        final Tag springTag = tagCommandRepository.save(TagFixture.create(tagName("스프링")));
        final int totalPost = 5;

        final RunnerPost expectedRunnerPostOne = runnerPostQueryRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(LocalDateTime.now().plusHours(100)),
                List.of(javaTag, springTag),
                NOT_STARTED
        ));

        final RunnerPost expectedRunnerPostTwo = runnerPostQueryRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(LocalDateTime.now().plusHours(100)),
                List.of(javaTag),
                NOT_STARTED
        ));

        final RunnerPost expectedRunnerPostThree = runnerPostQueryRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(now().plusHours(100)),
                List.of(springTag),
                NOT_STARTED
        ));

        runnerPostQueryRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(now().minusHours(100)),
                List.of(springTag),
                ReviewStatus.OVERDUE
        ));

        final RunnerPost previousRunnerPost = runnerPostQueryRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(now().plusHours(100)),
                List.of(springTag),
                NOT_STARTED
        ));

        // when
        final PageParams pageParams = new PageParams(previousRunnerPost.getId(), 10);
        final PageResponse<RunnerPostResponse.Simple> actual = runnerPostQueryService.pageRunnerPostByTagNameAndReviewStatus(
                null,
                pageParams,
                NOT_STARTED
        );

        final List<RunnerPostTag> runnerPostTags = List.of(
                RunnerPostTagFixture.create(expectedRunnerPostOne, javaTag),
                RunnerPostTagFixture.create(expectedRunnerPostOne, springTag),
                RunnerPostTagFixture.create(expectedRunnerPostTwo, javaTag),
                RunnerPostTagFixture.create(expectedRunnerPostThree, springTag)
        );
        final PageResponse<RunnerPostResponse.Simple> expected = PageResponse.of(
                List.of(RunnerPostResponse.Simple.of(expectedRunnerPostThree, 0L, runnerPostTags),
                        RunnerPostResponse.Simple.of(expectedRunnerPostTwo, 0L, runnerPostTags),
                        RunnerPostResponse.Simple.of(expectedRunnerPostOne, 0L, runnerPostTags)),
                pageParams,
                totalPost
        );

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("태그 이름을 조건으로 러너 게시글 첫 페이지 조회에 성공한다.")
    @Test
    void readRunnerPostByPageInfoAndTagName_firstPage() {
        // given
        final Member hyenaMember = memberCommandRepository.save(MemberFixture.createHyena());
        final Runner hyenaRunner = runnerQueryRepository.save(RunnerFixture.createRunner(hyenaMember));

        final Tag javaTag = tagCommandRepository.save(TagFixture.create(tagName("자바")));
        final Tag springTag = tagCommandRepository.save(TagFixture.create(tagName("스프링")));

        final RunnerPost expectedRunnerPostOne = runnerPostQueryRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(LocalDateTime.now()),
                List.of(javaTag, springTag),
                ReviewStatus.OVERDUE
        ));

        final RunnerPost expectedRunnerPostTwo = runnerPostQueryRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(LocalDateTime.now().plusHours(100)),
                List.of(javaTag),
                NOT_STARTED
        ));

        runnerPostQueryRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(now().plusHours(100)),
                List.of(springTag),
                ReviewStatus.IN_PROGRESS
        ));

        // when
        final PageParams pageParams = new PageParams(null, 10);
        final PageResponse<RunnerPostResponse.Simple> actual = runnerPostQueryService.pageRunnerPostByTagNameAndReviewStatus(
                javaTag.getTagName().getValue(),
                pageParams,
                null
        );

        final List<RunnerPostTag> runnerPostTags = List.of(
                RunnerPostTagFixture.create(expectedRunnerPostOne, javaTag),
                RunnerPostTagFixture.create(expectedRunnerPostOne, springTag),
                RunnerPostTagFixture.create(expectedRunnerPostTwo, javaTag)
        );
        final int total = 2;
        final PageResponse<RunnerPostResponse.Simple> expected = PageResponse.of(
                List.of(RunnerPostResponse.Simple.of(expectedRunnerPostTwo, 0L, runnerPostTags),
                        RunnerPostResponse.Simple.of(expectedRunnerPostOne, 0L, runnerPostTags)),
                pageParams,
                total
        );

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("태그 이름을 조건으로 러너 게시글 중간 페이지 조회에 성공한다.")
    @Test
    void readRunnerPostByPageInfoAndTagName_middlePage() {
        // given
        final Member hyenaMember = memberCommandRepository.save(MemberFixture.createHyena());
        final Runner hyenaRunner = runnerQueryRepository.save(RunnerFixture.createRunner(hyenaMember));

        final Tag javaTag = tagCommandRepository.save(TagFixture.create(tagName("자바")));
        final Tag springTag = tagCommandRepository.save(TagFixture.create(tagName("스프링")));
        final int totalJavaTagPost = 3;

        final RunnerPost expectedRunnerPostOne = runnerPostQueryRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(LocalDateTime.now().plusHours(100)),
                List.of(javaTag, springTag),
                NOT_STARTED
        ));

        final RunnerPost expectedRunnerPostTwo = runnerPostQueryRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(LocalDateTime.now()),
                List.of(javaTag),
                ReviewStatus.OVERDUE
        ));

        final RunnerPost previousRunnerPost = runnerPostQueryRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(now().plusHours(100)),
                List.of(javaTag),
                NOT_STARTED
        ));

        runnerPostQueryRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(now().plusHours(100)),
                List.of(springTag),
                ReviewStatus.IN_PROGRESS
        ));

        // when
        final PageParams pageParams = new PageParams(previousRunnerPost.getId(), 10);
        final PageResponse<RunnerPostResponse.Simple> actual = runnerPostQueryService.pageRunnerPostByTagNameAndReviewStatus(
                javaTag.getTagName().getValue(),
                pageParams,
                null
        );

        final List<RunnerPostTag> runnerPostTags = List.of(
                RunnerPostTagFixture.create(expectedRunnerPostOne, javaTag),
                RunnerPostTagFixture.create(expectedRunnerPostOne, springTag),
                RunnerPostTagFixture.create(expectedRunnerPostTwo, javaTag)
        );
        final PageResponse<RunnerPostResponse.Simple> expected = PageResponse.of(
                List.of(RunnerPostResponse.Simple.of(expectedRunnerPostTwo, 0L, runnerPostTags),
                        RunnerPostResponse.Simple.of(expectedRunnerPostOne, 0L, runnerPostTags)),
                pageParams,
                totalJavaTagPost
        );

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("조건 없이 러너 게시글 첫 페이지 조회에 성공한다.")
    @Test
    void readRunnerPostByPageInfo_firstPage() {
        // given
        final Member hyenaMember = memberCommandRepository.save(MemberFixture.createHyena());
        final Runner hyenaRunner = runnerQueryRepository.save(RunnerFixture.createRunner(hyenaMember));

        final Tag javaTag = tagCommandRepository.save(TagFixture.create(tagName("자바")));
        final Tag springTag = tagCommandRepository.save(TagFixture.create(tagName("스프링")));

        final RunnerPost expectedRunnerPostOne = runnerPostQueryRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(LocalDateTime.now()),
                List.of(javaTag, springTag),
                ReviewStatus.OVERDUE
        ));

        final RunnerPost expectedRunnerPostTwo = runnerPostQueryRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(LocalDateTime.now().plusHours(100)),
                List.of(javaTag),
                NOT_STARTED
        ));

        final RunnerPost expectedRunnerPostThree = runnerPostQueryRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(now().plusHours(100)),
                List.of(springTag),
                IN_PROGRESS
        ));

        // when
        final PageParams pageParams = new PageParams(null, 10);
        final PageResponse<RunnerPostResponse.Simple> actual = runnerPostQueryService.pageRunnerPostByTagNameAndReviewStatus(
                null,
                pageParams,
                null
        );

        final List<RunnerPostTag> runnerPostTags = List.of(
                RunnerPostTagFixture.create(expectedRunnerPostOne, javaTag),
                RunnerPostTagFixture.create(expectedRunnerPostOne, springTag),
                RunnerPostTagFixture.create(expectedRunnerPostTwo, javaTag),
                RunnerPostTagFixture.create(expectedRunnerPostThree, springTag)
        );
        final int total = 3;
        final PageResponse<RunnerPostResponse.Simple> expected = PageResponse.of(
                List.of(RunnerPostResponse.Simple.of(expectedRunnerPostThree, 0L, runnerPostTags),
                        RunnerPostResponse.Simple.of(expectedRunnerPostTwo, 0L, runnerPostTags),
                        RunnerPostResponse.Simple.of(expectedRunnerPostOne, 0L, runnerPostTags)),
                pageParams,
                total
        );

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("조건 없이 러너 게시글 중간 페이지 조회에 성공한다.")
    @Test
    void readRunnerPostByPageInfo_middlePage() {
        // given
        final Member hyenaMember = memberCommandRepository.save(MemberFixture.createHyena());
        final Runner hyenaRunner = runnerQueryRepository.save(RunnerFixture.createRunner(hyenaMember));

        final Tag javaTag = tagCommandRepository.save(TagFixture.create(tagName("자바")));
        final Tag springTag = tagCommandRepository.save(TagFixture.create(tagName("스프링")));
        final int totalPost = 4;

        final RunnerPost expectedRunnerPostOne = runnerPostQueryRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(LocalDateTime.now().plusHours(100)),
                List.of(javaTag, springTag),
                NOT_STARTED
        ));

        final RunnerPost expectedRunnerPostTwo = runnerPostQueryRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(LocalDateTime.now()),
                List.of(javaTag),
                ReviewStatus.OVERDUE
        ));

        final RunnerPost expectedRunnerPostThree = runnerPostQueryRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(now().plusHours(100)),
                List.of(springTag),
                IN_PROGRESS
        ));

        final RunnerPost previousRunnerPost = runnerPostQueryRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(now().plusHours(100)),
                List.of(javaTag),
                NOT_STARTED
        ));

        // when
        final PageParams pageParams = new PageParams(previousRunnerPost.getId(), 10);
        final PageResponse<RunnerPostResponse.Simple> actual = runnerPostQueryService.pageRunnerPostByTagNameAndReviewStatus(
                null,
                pageParams,
                null
        );

        final List<RunnerPostTag> runnerPostTags = List.of(
                RunnerPostTagFixture.create(expectedRunnerPostOne, javaTag),
                RunnerPostTagFixture.create(expectedRunnerPostOne, springTag),
                RunnerPostTagFixture.create(expectedRunnerPostTwo, javaTag),
                RunnerPostTagFixture.create(expectedRunnerPostThree, springTag)
        );
        final PageResponse<RunnerPostResponse.Simple> expected = PageResponse.of(
                List.of(RunnerPostResponse.Simple.of(expectedRunnerPostThree, 0L, runnerPostTags),
                        RunnerPostResponse.Simple.of(expectedRunnerPostTwo, 0L, runnerPostTags),
                        RunnerPostResponse.Simple.of(expectedRunnerPostOne, 0L, runnerPostTags)),
                pageParams,
                totalPost
        );

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("RunnerPost 식별자로 RunnerPost 를 조회한다.")
    @Test
    void success_findByRunnerPostId() {
        // given
        final Member member = Member.builder()
                .memberName(new MemberName("헤에디주"))
                .socialId(new SocialId("testSocialId"))
                .oauthId(new OauthId("dsigjh98gh230gn2oinv913bcuo23nqovbvu93b12voi3bc31j"))
                .githubUrl(new GithubUrl("github.com/hyena0608"))
                .company(new Company("우아한형제들"))
                .imageUrl(new ImageUrl("홍혁준"))
                .build();
        memberCommandRepository.save(member);

        final Runner runner = Runner.builder()
                .member(member)
                .runnerTechnicalTags(RunnerTechnicalTagsFixture.create(new ArrayList<>()))
                .build();
        runnerQueryRepository.save(runner);

        final LocalDateTime deadline = now();
        final RunnerPost runnerPost = RunnerPost.builder()
                .title(new Title("제 코드 리뷰 좀 해주세요!!"))
                .implementedContents(new ImplementedContents("제 코드는 클린코드가 맞을까요?"))
                .curiousContents(new CuriousContents("궁금해요."))
                .postscriptContents(new PostscriptContents("잘 부탁드립니다."))
                .deadline(new Deadline(deadline))
                .pullRequestUrl(new PullRequestUrl("https://"))
                .watchedCount(new WatchedCount(0))
                .runnerPostTags(new RunnerPostTags(new ArrayList<>()))
                .reviewStatus(NOT_STARTED)
                .isReviewed(IsReviewed.notReviewed())
                .runner(runner)
                .supporter(null)
                .build();
        runnerPostQueryRepository.save(runnerPost);

        final Tag tag = Tag.builder()
                .tagName(new TagName("자바"))
                .tagReducedName(TagReducedName.from("자바"))
                .build();
        tagCommandRepository.save(tag);

        final RunnerPostTag runnerPostTag = RunnerPostTag.builder()
                .runnerPost(runnerPost)
                .tag(tag)
                .build();
        runnerPost.addAllRunnerPostTags(List.of(runnerPostTag));

        // when
        final RunnerPost findRunnerPost = runnerPostQueryService.readByRunnerPostId(runnerPost.getId());

        // then
        assertThat(findRunnerPost)
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(runnerPost);
    }

    @DisplayName("RunnerPost 식별자로 존재하지 않는 RunnerPost 를 조회할 경우 예외가 발생한다.")
    @Test
    void fail_findByRunnerPostId_if_runner_post_is_null() {
        assertThatThrownBy(() -> runnerPostQueryService.readByRunnerPostId(0L))
                .isInstanceOf(RunnerPostBusinessException.class)
                .hasMessage("RunnerPost 의 식별자값으로 러너 게시글을 조회할 수 없습니다.");
    }

    @DisplayName("Runner 식별자값으로 RunnerPost 를 조회한다.")
    @Test
    void success_findByRunnerId() {
        // given
        final Member ditoo = MemberFixture.createDitoo();
        memberCommandRepository.save(ditoo);
        final Runner runner = RunnerFixture.createRunner(ditoo);
        runnerQueryRepository.save(runner);
        final RunnerPost expected = RunnerPostFixture.create(runner, new Deadline(now().plusHours(100)));
        runnerPostQueryRepository.save(expected);

        // when
        final List<RunnerPost> actual = runnerPostQueryService.readRunnerPostsByRunnerId(runner.getId());

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual).hasSize(1);
            softly.assertThat(actual.get(0)).isEqualTo(expected);
        });
    }

    @DisplayName("Supporter 외래키와 ReviewStatus 로 러너 게시글을 조회한다. (NOT_STARTED 제외)")
    @Test
    void readRunnerPostsBySupporterIdAndReviewStatus() {
        // given
        final Member savedMemberEthan = memberCommandRepository.save(MemberFixture.createEthan());
        final Runner savedRunnerEthan = runnerQueryRepository.save(RunnerFixture.createRunner(savedMemberEthan));

        final Member savedMemberHyena = memberCommandRepository.save(MemberFixture.createHyena());
        final Supporter savedSupporterHyena = supporterQueryRepository.save(SupporterFixture.create(savedMemberHyena));

        final RunnerPost runnerPost = RunnerPostFixture.create(savedRunnerEthan, deadline(now().plusHours(100)));
        final RunnerPost savedRunnerPost = runnerPostQueryRepository.save(runnerPost);
        savedRunnerPost.assignSupporter(savedSupporterHyena);

        supporterRunnerPostQueryRepository.save(SupporterRunnerPostFixture.create(runnerPost, savedSupporterHyena));

        // when
        final PageParams pageParams = new PageParams(null, 10);
        final PageResponse<RunnerPostResponse.Simple> actual
                = runnerPostQueryService.pageRunnerPostBySupporterIdAndReviewStatus(pageParams, savedSupporterHyena.getId(), IN_PROGRESS);
        final PageResponse<RunnerPostResponse.Simple> expected = PageResponse.of(
                List.of(RunnerPostResponse.Simple.of(savedRunnerPost, 1L, Collections.emptyList())),
                pageParams
        );

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("Supporter 외래키와 ReviewStatus 로 러너 게시글을 조회한다. (NOT_STARTED 인 경우)")
    @Test
    void readRunnerPostsBySupporterIdAndReviewStatusIs_NOT_STARTED() {
        // given
        final Member member = memberCommandRepository.save(MemberFixture.createEthan());
        final Runner runner = runnerQueryRepository.save(RunnerFixture.createRunner(member));

        final Member supporterMember = memberCommandRepository.save(MemberFixture.createHyena());
        final Supporter supporter = supporterQueryRepository.save(SupporterFixture.create(supporterMember));

        final RunnerPost runnerPost = RunnerPostFixture.create(runner, deadline(now().plusHours(100)));
        final RunnerPost savedRunnerPost = runnerPostQueryRepository.save(runnerPost);

        supporterRunnerPostQueryRepository.save(SupporterRunnerPostFixture.create(runnerPost, supporter));

        // when
        final PageParams pageParams = new PageParams(null, 10);
        final PageResponse<RunnerPostResponse.Simple> actual
                = runnerPostQueryService.pageRunnerPostBySupporterIdAndReviewStatus(pageParams, supporter.getId(), NOT_STARTED);
        final PageResponse<RunnerPostResponse.Simple> expected = PageResponse.of(
                List.of(RunnerPostResponse.Simple.of(savedRunnerPost, 1L, Collections.emptyList())),
                pageParams
        );

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("Runner 외래키와 ReviewStatus 로 러너 게시글을 조회한다.")
    @Test
    void readRunnerPostsByRunnerIdAndReviewStatus() {
        // given
        final Member member = memberCommandRepository.save(MemberFixture.createEthan());
        final Runner runner = runnerQueryRepository.save(RunnerFixture.createRunner(member));

        final RunnerPost runnerPost = RunnerPostFixture.create(runner, deadline(now().plusHours(100)));
        final RunnerPost savedRunnerPost = runnerPostQueryRepository.save(runnerPost);

        // when
        final PageParams pageParams = new PageParams(null, 10);
        final PageResponse<RunnerPostResponse.SimpleByRunner> actual
                = runnerPostQueryService.pageRunnerPostByRunnerIdAndReviewStatus(pageParams, runner.getId(), NOT_STARTED);
        final PageResponse<RunnerPostResponse.SimpleByRunner> expected = PageResponse.of(
            List.of(RunnerPostResponse.SimpleByRunner.of(savedRunnerPost, 0L, Collections.emptyList())),
            pageParams
        );

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("Member 가 RunnerPost 에 지원한 이력이 있을 경우 true 를 반환한다.")
    @Test
    void existsRunnerPostApplicantByRunnerPostIdAndMemberId() {
        // given
        final Member savedMemberDitoo = memberCommandRepository.save(MemberFixture.createDitoo());
        final Runner savedRunnerDitoo = runnerQueryRepository.save(RunnerFixture.createRunner(savedMemberDitoo));

        final Member savedMemberHyena = memberCommandRepository.save(MemberFixture.createHyena());
        final Supporter savedSupporterHyena = supporterQueryRepository.save(SupporterFixture.create(savedMemberHyena));

        final RunnerPost savedRunnerPost = runnerPostQueryRepository.save(RunnerPostFixture.create(savedRunnerDitoo, new Deadline(now().plusHours(100))));

        supporterRunnerPostQueryRepository.save(SupporterRunnerPostFixture.create(savedRunnerPost, savedSupporterHyena));

        // when
        final boolean isApplicantHistoryExist = runnerPostQueryService.existsRunnerPostApplicantByRunnerPostIdAndMemberId(
                savedRunnerPost.getId(),
                savedMemberHyena.getId()
        );

        // then
        assertThat(isApplicantHistoryExist).isTrue();
    }

    @DisplayName("Member 가 RunnerPost 에 지원한 이력을 조회할 때 RunnerPost 자체가 없으면 false 를 반환한다.")
    @Test
    void existsRunnerPostApplicantByRunnerPostIdAndMemberId_if_runnerPost_is_not_exist_then_return_false() {
        // given
        final Member savedMemberHyena = memberCommandRepository.save(MemberFixture.createHyena());

        // when
        final Long notExistRunnerPostId = -1L;
        final boolean isApplicantHistoryExist = runnerPostQueryService.existsRunnerPostApplicantByRunnerPostIdAndMemberId(
                notExistRunnerPostId,
                savedMemberHyena.getId()
        );

        // then
        assertThat(isApplicantHistoryExist).isFalse();
    }

    @DisplayName("Member 가 RunnerPost 에 지원한 이력이 없을 경우 false 를 반환한다.")
    @Test
    void existsRunnerPostApplicantByRunnerPostIdAndMemberId_if_member_is_not_exist_then_return_false() {
        // given
        final Member savedMemberDitoo = memberCommandRepository.save(MemberFixture.createDitoo());
        final Runner savedRunnerDitoo = runnerQueryRepository.save(RunnerFixture.createRunner(savedMemberDitoo));

        final RunnerPost savedRunnerPost = runnerPostQueryRepository.save(RunnerPostFixture.create(savedRunnerDitoo, new Deadline(now().plusHours(100))));

        // when
        final Long notExistMemberId = -1L;
        final boolean isApplicantHistoryExist = runnerPostQueryService.existsRunnerPostApplicantByRunnerPostIdAndMemberId(
                savedRunnerPost.getId(),
                notExistMemberId
        );

        // then
        assertThat(isApplicantHistoryExist).isFalse();
    }

    @DisplayName("RunnerId 와 ReviewStatus 로 러너 게시글 개수를 조회한다.")
    @Test
    void countRunnerPostByRunnerIdAndReviewStatus() {
        // given
        final Member member = memberCommandRepository.save(MemberFixture.createDitoo());
        final Runner runner = runnerQueryRepository.save(RunnerFixture.createRunner(member));

        final long expected = 3L;
        for (long i = 0; i < expected; i++) {
            runnerPostCommandRepository.save(RunnerPostFixture.create(runner, new Deadline(now().plusHours(100))));
        }

        // when
        final long actual = runnerPostQueryService.countRunnerPostByRunnerIdAndReviewStatus(runner.getId(), NOT_STARTED);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("SupporterId 로 ReviewStatus 가 NOT_STARTED 인 러너 게시글 개수를 조회한다.")
    @Test
    void countRunnerPostBySupporterIdAndReviewStatus_NOT_STARTED() {
        // given
        final Member member = memberCommandRepository.save(MemberFixture.createDitoo());
        final Runner runner = runnerQueryRepository.save(RunnerFixture.createRunner(member));
        final RunnerPost runnerPost = runnerPostCommandRepository.save(RunnerPostFixture.create(runner, new Deadline(now().plusHours(100))));

        final Member supporterMember = memberCommandRepository.save(MemberFixture.createDitoo());
        final Supporter supporter = supporterQueryRepository.save(SupporterFixture.create(supporterMember));

        supporterRunnerPostCommandRepository.save(SupporterRunnerPostFixture.create(runnerPost, supporter));

        // when
        final long expected = 1L;
        final long actual = runnerPostQueryService.countRunnerPostBySupporterIdAndReviewStatus(supporter.getId(), NOT_STARTED);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("SupporterId 로 ReviewStatus 가 NOT_STARTED 이 아닌 러너 게시글 개수를 조회한다.")
    @Test
    void countRunnerPostBySupporterIdAndReviewStatus_except_NOT_STARTED() {
        // given
        final Member member = memberCommandRepository.save(MemberFixture.createDitoo());
        final Runner runner = runnerQueryRepository.save(RunnerFixture.createRunner(member));
        final RunnerPost runnerPost = runnerPostCommandRepository.save(RunnerPostFixture.create(runner, new Deadline(now().plusHours(100))));

        final Member supporterMember = memberCommandRepository.save(MemberFixture.createDitoo());
        final Supporter supporter = supporterQueryRepository.save(SupporterFixture.create(supporterMember));

        supporterRunnerPostCommandRepository.save(SupporterRunnerPostFixture.create(runnerPost, supporter));
        runnerPost.assignSupporter(supporter);

        // when
        final long expected = 1L;
        final long actual = runnerPostQueryService.countRunnerPostBySupporterIdAndReviewStatus(supporter.getId(), IN_PROGRESS);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
