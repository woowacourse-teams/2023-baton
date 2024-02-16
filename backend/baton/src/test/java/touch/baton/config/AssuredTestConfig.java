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
import touch.baton.assure.repository.TestMemberQueryRepository;
import touch.baton.assure.repository.TestNotificationCommandRepository;
import touch.baton.assure.repository.TestRankQueryRepository;
import touch.baton.assure.repository.TestRefreshTokenRepository;
import touch.baton.assure.repository.TestRunnerPostQueryRepository;
import touch.baton.assure.repository.TestRunnerQueryRepository;
import touch.baton.assure.repository.TestSupporterQueryRepository;
import touch.baton.assure.repository.TestSupporterRunnerPostQueryRepository;
import touch.baton.assure.repository.TestTagQuerydslRepository;
import touch.baton.config.converter.ConverterConfig;
import touch.baton.config.infra.auth.MockBeanAuthTestConfig;
import touch.baton.config.infra.github.MockGithubBranchServiceConfig;

@ActiveProfiles("test")
@Import({JpaConfig.class, QuerydslConfig.class, ConverterConfig.class, PageableTestConfig.class, MockBeanAuthTestConfig.class, MockGithubBranchServiceConfig.class, JwtTestManager.class})
@TestExecutionListeners(value = AssuredTestExecutionListener.class, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AssuredTestConfig {

    @Autowired
    protected TestMemberQueryRepository memberRepository;

    @Autowired
    protected TestRunnerQueryRepository runnerRepository;

    @Autowired
    protected TestSupporterQueryRepository supporterRepository;

    @Autowired
    protected TestRunnerPostQueryRepository runnerPostRepository;

    @Autowired
    protected TestSupporterRunnerPostQueryRepository supporterRunnerPostRepository;

    @Autowired
    protected TestTagQuerydslRepository tagQueryRepository;

    @Autowired
    protected TestNotificationCommandRepository notificationCommandRepository;

    @Autowired
    protected TestRefreshTokenRepository refreshTokenRepository;

    @Autowired
    protected JwtTestManager jwtTestManager;

    @Autowired
    protected TestRankQueryRepository rankQueryRepository;

    protected OauthLoginTestManager oauthLoginTestManager = new OauthLoginTestManager();

    @BeforeEach
    void assuredTestSetUp(@LocalServerPort final int port) {
        RestAssured.port = port;
    }
}
