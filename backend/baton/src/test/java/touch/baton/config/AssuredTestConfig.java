package touch.baton.config;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import touch.baton.domain.member.repository.MemberRepository;
import touch.baton.domain.runner.repository.RunnerRepository;
import touch.baton.domain.runnerpost.repository.RunnerPostRepository;
import touch.baton.domain.supporter.repository.SupporterRepository;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AssuredTestConfig {

    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected RunnerRepository runnerRepository;

    @Autowired
    protected RunnerPostRepository runnerPostRepository;

    @Autowired
    protected SupporterRepository supporterRepository;

    @BeforeEach
    void assuredTestSetUp(@LocalServerPort int port) {
        RestAssured.port = port;
    }
}
