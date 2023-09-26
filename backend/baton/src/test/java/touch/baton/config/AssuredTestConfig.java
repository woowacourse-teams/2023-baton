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
import touch.baton.assure.repository.TestMemberCommandRepository;
import touch.baton.assure.repository.TestRefreshTokenRepository;
import touch.baton.assure.repository.TestRunnerPostRepository;
import touch.baton.assure.repository.TestRunnerQueryRepository;
import touch.baton.assure.repository.TestSupporterQueryRepository;
import touch.baton.assure.repository.TestSupporterRunnerPostQueryRepository;
import touch.baton.assure.repository.TestTagRepository;
import touch.baton.config.converter.ConverterConfig;
import touch.baton.config.infra.auth.MockAuthTestConfig;
import touch.baton.config.infra.github.MockGithubBranchServiceConfig;

@ActiveProfiles("test")
@Import({JpaConfig.class, QuerydslConfig.class, ConverterConfig.class, PageableTestConfig.class, MockAuthTestConfig.class, MockGithubBranchServiceConfig.class, JwtTestManager.class})
@TestExecutionListeners(value = AssuredTestExecutionListener.class, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AssuredTestConfig {

    @Autowired
    protected TestMemberCommandRepository memberRepository;

    @Autowired
    protected TestRunnerQueryRepository runnerRepository;

    @Autowired
    protected TestSupporterQueryRepository supporterRepository;

    @Autowired
    protected TestRunnerPostRepository runnerPostRepository;

    @Autowired
    protected TestSupporterRunnerPostQueryRepository supporterRunnerPostRepository;

    @Autowired
    protected TestTagRepository tagRepository;

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
