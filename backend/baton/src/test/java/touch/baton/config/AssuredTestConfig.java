package touch.baton.config;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import touch.baton.assure.common.JwtTestManager;
import touch.baton.assure.common.OauthLoginTestManager;
import touch.baton.assure.repository.TestMemberRepository;
import touch.baton.assure.repository.TestRefreshTokenRepository;
import touch.baton.assure.repository.TestRunnerPostReadRepository;
import touch.baton.assure.repository.TestRunnerPostRepository;
import touch.baton.assure.repository.TestRunnerRepository;
import touch.baton.assure.repository.TestSupporterRepository;
import touch.baton.assure.repository.TestSupporterRunnerPostRepository;
import touch.baton.assure.repository.TestTechnicalTagRepository;
import touch.baton.config.converter.ConverterConfig;
import touch.baton.config.infra.auth.MockAuthTestConfig;
import touch.baton.config.infra.github.MockGithubBranchServiceConfig;

@ActiveProfiles("test")
@Import({JpaConfig.class, ConverterConfig.class, PageableTestConfig.class, MockAuthTestConfig.class, MockGithubBranchServiceConfig.class, JwtTestManager.class})
@TestExecutionListeners(value = AssuredTestExecutionListener.class, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AssuredTestConfig {

    @Autowired
    protected TestMemberRepository memberRepository;

    @Autowired
    protected TestRunnerRepository runnerRepository;

    @Autowired
    protected TestSupporterRepository supporterRepository;

    @Autowired
    protected TestRunnerPostRepository runnerPostRepository;

    @Autowired
    protected TestRunnerPostReadRepository runnerPostReadRepository;

    @Autowired
    protected TestSupporterRunnerPostRepository supporterRunnerPostRepository;

    @Autowired
    protected TestTechnicalTagRepository technicalTagRepository;

    @Autowired
    protected TestRefreshTokenRepository refreshTokenRepository;

    @Autowired
    protected JwtTestManager jwtTestManager;

    protected OauthLoginTestManager oauthLoginTestManager = new OauthLoginTestManager();

    @BeforeEach
    void assuredTestSetUp(@LocalServerPort final int port) {
        RestAssured.port = port;
    }
}
