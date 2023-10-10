package touch.baton.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.ManualRestDocumentation;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import touch.baton.config.infra.auth.oauth.github.FakeGithubOauthConfig;
import touch.baton.domain.member.command.controller.MemberBranchController;
import touch.baton.domain.member.command.controller.RunnerCommandController;
import touch.baton.domain.member.command.controller.SupporterCommandController;
import touch.baton.domain.member.command.service.GithubBranchManageable;
import touch.baton.domain.member.command.service.RunnerCommandService;
import touch.baton.domain.member.command.service.SupporterCommandService;
import touch.baton.domain.member.query.controller.MemberQueryController;
import touch.baton.domain.member.query.controller.RunnerQueryController;
import touch.baton.domain.member.query.controller.SupporterQueryController;
import touch.baton.domain.member.query.service.RunnerQueryService;
import touch.baton.domain.member.query.service.SupporterQueryService;
import touch.baton.domain.oauth.command.controller.OauthCommandController;
import touch.baton.domain.oauth.command.repository.OauthMemberCommandRepository;
import touch.baton.domain.oauth.command.repository.OauthRunnerCommandRepository;
import touch.baton.domain.oauth.command.repository.OauthSupporterCommandRepository;
import touch.baton.domain.oauth.command.service.OauthCommandService;
import touch.baton.domain.runnerpost.command.controller.RunnerPostCommandController;
import touch.baton.domain.runnerpost.command.service.RunnerPostCommandService;
import touch.baton.domain.runnerpost.query.controller.RunnerPostQueryController;
import touch.baton.domain.runnerpost.query.service.RunnerPostQueryService;
import touch.baton.domain.tag.query.controller.TagQueryController;
import touch.baton.domain.tag.query.service.TagQueryService;
import touch.baton.infra.auth.jwt.JwtDecoder;
import touch.baton.infra.auth.oauth.github.GithubOauthConfig;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

@WebMvcTest({
        RunnerQueryController.class,
        RunnerCommandController.class,
        SupporterQueryController.class,
        SupporterCommandController.class,
        MemberQueryController.class,
        RunnerPostCommandController.class,
        RunnerPostQueryController.class,
        TagQueryController.class,
        MemberBranchController.class,
        OauthCommandController.class
})
@Import({RestDocsResultConfig.class, FakeGithubOauthConfig.class})
public abstract class RestdocsConfig {

    protected MockMvc mockMvc;

    @Autowired
    protected RestDocumentationResultHandler restDocs;

    @Autowired
    protected RestDocumentationContextProvider restDocumentation;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected GithubOauthConfig githubOauthConfig;

    @MockBean
    protected JwtDecoder jwtDecoder;

    @MockBean
    protected OauthMemberCommandRepository oauthMemberCommandRepository;

    @MockBean
    protected OauthRunnerCommandRepository oauthRunnerCommandRepository;

    @MockBean
    protected OauthSupporterCommandRepository oauthSupporterCommandRepository;

    @MockBean
    protected GithubBranchManageable githubBranchManageable;

    @MockBean
    protected OauthCommandService oauthCommandService;

    @MockBean
    protected RunnerPostQueryService runnerPostQueryService;

    @MockBean
    protected RunnerPostCommandService runnerPostCommandService;

    @MockBean
    protected RunnerQueryService runnerQueryService;

    @MockBean
    protected RunnerCommandService runnerCommandService;

    @MockBean
    protected SupporterQueryService supporterQueryService;

    @MockBean
    protected SupporterCommandService supporterCommandService;

    @MockBean
    protected TagQueryService tagQueryService;

    @BeforeEach
    void restdocsSetUp(final WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(restDocs)
                .build();
    }

    protected String getAccessTokenBySocialId(final String socialId) {
        final String token = UUID.randomUUID().toString();
        final Claims claims = Jwts.claims();
        claims.put("socialId", socialId);

        when(jwtDecoder.parseAuthorizationHeader(any())).thenReturn(claims);

        return token;
    }
}

@TestConfiguration
class RestDocsResultConfig {

    @Bean
    RestDocumentationResultHandler restDocumentationResultHandler() {
        return MockMvcRestDocumentation.document("{class-name}/{method-name}",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())
        );
    }

    @Bean
    RestDocumentationContextProvider restDocumentationContextProvider() {
        return new ManualRestDocumentation();
    }
}
