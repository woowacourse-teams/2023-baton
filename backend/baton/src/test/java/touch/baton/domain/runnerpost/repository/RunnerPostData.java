package touch.baton.domain.runnerpost.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import touch.baton.config.JpaConfig;
import touch.baton.domain.common.vo.ChattingCount;
import touch.baton.domain.common.vo.Contents;
import touch.baton.domain.common.vo.Grade;
import touch.baton.domain.common.vo.Title;
import touch.baton.domain.common.vo.TotalRating;
import touch.baton.domain.common.vo.WatchedCount;
import touch.baton.domain.member.Member;
import touch.baton.domain.member.repository.MemberRepository;
import touch.baton.domain.member.vo.Company;
import touch.baton.domain.member.vo.Email;
import touch.baton.domain.member.vo.GithubUrl;
import touch.baton.domain.member.vo.ImageUrl;
import touch.baton.domain.member.vo.MemberName;
import touch.baton.domain.member.vo.OauthId;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runner.repository.RunnerRepository;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.vo.Deadline;
import touch.baton.domain.runnerpost.vo.PullRequestUrl;
import touch.baton.domain.runnerpost.vo.ReviewStatus;
import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.supporter.repository.SupporterRepository;
import touch.baton.domain.supporter.vo.ReviewCount;
import touch.baton.domain.supporter.vo.StarCount;
import touch.baton.domain.tag.RunnerPostTag;
import touch.baton.domain.tag.RunnerPostTags;
import touch.baton.domain.tag.Tag;
import touch.baton.domain.tag.repository.RunnerPostTagRepository;
import touch.baton.domain.tag.repository.TagRepository;
import touch.baton.domain.technicaltag.SupporterTechnicalTags;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Import(JpaConfig.class)
@DataJpaTest
public class RunnerPostData {

    @Autowired
    private RunnerPostTagRepository runnerPostTagRepository;
    @Autowired
    private RunnerPostRepository runnerPostRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private RunnerRepository runnerRepository;
    @Autowired
    private SupporterRepository supporterRepository;

    protected Member runnerMember;
    protected Member supporterMember;
    protected Runner runner;
    protected Supporter supporter;
    protected RunnerPost runnerPost;
    protected Tag tagJava;
    protected Tag tagSpring;
    protected RunnerPostTag runnerPostTagJava;
    protected RunnerPostTag runnerPostTagSpring;

    private Member setRunnerMember() {
        return Member.builder()
                .memberName(new MemberName("러너 사용자"))
                .email(new Email("test@test.co.kr"))
                .oauthId(new OauthId("ads7821iuqjkrhadsioh1f1r4efsoi3bc31j"))
                .githubUrl(new GithubUrl("github.com/hyena0608"))
                .company(new Company("우아한테크코스"))
                .imageUrl(new ImageUrl("김석호"))
                .build();
    }

    private Member setSupporterMember() {
        return Member.builder()
                .memberName(new MemberName("서포터 사용자"))
                .email(new Email("test@test.co.kr"))
                .oauthId(new OauthId("dsigjh98gh230gn2oinv913bcuo23nqovbvu93b12voi3bc31j"))
                .githubUrl(new GithubUrl("github.com/pobi"))
                .company(new Company("우아한형제들"))
                .imageUrl(new ImageUrl("김석호"))
                .build();
    }

    private Runner setRunner() {
        return Runner.builder()
                .totalRating(new TotalRating(100))
                .grade(Grade.BARE_FOOT)
                .member(runnerMember)
                .build();
    }

    private Supporter setSupporter() {
        return Supporter.builder()
                .reviewCount(new ReviewCount(10))
                .starCount(new StarCount(10))
                .totalRating(new TotalRating(100))
                .grade(Grade.BARE_FOOT)
                .member(supporterMember)
                .supporterTechnicalTags(new SupporterTechnicalTags(new ArrayList<>()))
                .build();
    }

    private Tag setTag(final String name) {
        return Tag.newInstance(name);
    }

    private RunnerPost setRunnerPost() {
        return RunnerPost.builder()
                .title(new Title("JPA 정복"))
                .contents(new Contents("김영한 짱짱맨"))
                .pullRequestUrl(new PullRequestUrl("https://github.com/woowacourse-teams/2023-baton/pull/17"))
                .deadline(new Deadline(LocalDateTime.of(2023, 9, 1, 10, 10)))
                .watchedCount(new WatchedCount(0))
                .chattingCount(new ChattingCount(0))
                .reviewStatus(ReviewStatus.NOT_STARTED)
                .runner(runner)
                .supporter(supporter)
                .runnerPostTags(new RunnerPostTags(new ArrayList<>()))
                .build();
    }

    private RunnerPostTag setRunnerPostTag(final RunnerPost runnerPost, final Tag tag) {
        return RunnerPostTag.builder()
                .runnerPost(runnerPost)
                .tag(tag)
                .build();
    }

    protected void setData() {
        runnerMember = memberRepository.save(setRunnerMember());
        supporterMember = memberRepository.save(setSupporterMember());
        runner = runnerRepository.save(setRunner());
        supporter = supporterRepository.save(setSupporter());
        runnerPost = runnerPostRepository.save(setRunnerPost());
        tagJava = tagRepository.save(setTag("java"));
        tagSpring = tagRepository.save(setTag("spring"));

        runnerPostTagJava = runnerPostTagRepository.save(setRunnerPostTag(runnerPost, tagJava));
        runnerPostTagSpring = runnerPostTagRepository.save(setRunnerPostTag(runnerPost, tagSpring));
        runnerPost.appendRunnerPostTag(runnerPostTagJava);
        runnerPost.appendRunnerPostTag(runnerPostTagSpring);
        runnerPostRepository.saveAndFlush(runnerPost);
    }
}
