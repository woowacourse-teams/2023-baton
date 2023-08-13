package touch.baton.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestExecutionListeners;
import touch.baton.config.converter.ConverterConfig;
import touch.baton.domain.member.repository.MemberRepository;
import touch.baton.domain.runner.repository.RunnerRepository;
import touch.baton.domain.runnerpost.repository.RunnerPostRepository;
import touch.baton.domain.supporter.repository.SupporterRepository;
import touch.baton.domain.supporter.repository.SupporterRunnerPostRepository;
import touch.baton.domain.technicaltag.repository.TechnicalTagRepository;
import touch.baton.infra.auth.jwt.JwtDecoder;

import java.util.UUID;

import static org.mockito.BDDMockito.when;

@Import({JpaConfig.class, ConverterConfig.class, PageableTestConfig.class})
@TestExecutionListeners(value = AssuredTestExecutionListener.class, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
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

    @Autowired
    protected SupporterRunnerPostRepository supporterRunnerPostRepository;

    @MockBean
    private JwtDecoder jwtDecoder;

    @Autowired
    protected TechnicalTagRepository technicalTagRepository;

    @BeforeEach
    void assuredTestSetUp(@LocalServerPort int port) {
        RestAssured.port = port;
    }

    public String login(final String socialId) {
        final String token = UUID.randomUUID().toString();
        final Claims claims = Jwts.claims();
        claims.put("socialId", socialId);

        when(jwtDecoder.parseJwtToken(token)).thenReturn(claims);

        return token;
    }
}
