package touch.baton.domain.runnerpost.controller;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import touch.baton.domain.common.vo.Grade;
import touch.baton.domain.common.vo.TotalRating;
import touch.baton.domain.member.Member;
import touch.baton.domain.member.repository.MemberRepository;
import touch.baton.domain.member.vo.Company;
import touch.baton.domain.member.vo.SocialId;
import touch.baton.domain.member.vo.GithubUrl;
import touch.baton.domain.member.vo.ImageUrl;
import touch.baton.domain.member.vo.MemberName;
import touch.baton.domain.member.vo.OauthId;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runner.repository.RunnerRepository;
import touch.baton.domain.runnerpost.service.dto.RunnerPostCreateRequest;

import java.time.LocalDateTime;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class RunnerPostControllerCreateTest {

    @Autowired
    private RunnerRepository runnerRepository;
    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp(@LocalServerPort int port) {
        RestAssured.port = port;

        final Member member = Member.builder()
                .memberName(new MemberName("헤에디주"))
                .socialId(new SocialId("testSocialId"))
                .oauthId(new OauthId("dsigjh98gh230gn2oinv913bcuo23nqovbvu93b12voi3bc31j"))
                .githubUrl(new GithubUrl("github.com/hyena0608"))
                .company(new Company("우아한형제들"))
                .imageUrl(new ImageUrl("홍혁준"))
                .build();
        memberRepository.save(member);
        final Runner runner = Runner.builder()
                .totalRating(new TotalRating(100))
                .grade(Grade.BARE_FOOT)
                .member(member)
                .build();
        runnerRepository.save(runner);
    }

    @Disabled
    @Test
    void 러너_게시글_등록에_성공한다() {
        // given
        final RunnerPostCreateRequest request = new RunnerPostCreateRequest("코드 리뷰 해주세요.",
                List.of("Java", "Spring"),
                "https://github.com/cookienc",
                LocalDateTime.of(2099, 12, 12, 0, 0),
                "싸게 부탁드려요."
        );

        // when
        final ExtractableResponse<Response> response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post("/api/v1/posts/runner")
                .then()
                .log().all().extract();

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header(HttpHeaders.LOCATION)).contains("/api/v1/posts/runner")
        );
    }
}
