package touch.baton.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.restdocs.ManualRestDocumentation;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import touch.baton.config.converter.ConverterConfig;
import touch.baton.config.converter.OauthTypeConverter;
import touch.baton.config.converter.ReviewStatusConverter;
import touch.baton.infra.auth.jwt.JwtDecoder;
import touch.baton.tobe.domain.oauth.command.repository.OauthMemberCommandRepository;
import touch.baton.tobe.domain.oauth.command.repository.OauthRunnerCommandRepository;
import touch.baton.tobe.domain.oauth.command.repository.OauthSupporterCommandRepository;
import touch.baton.tobe.domain.oauth.query.controller.resolver.AuthMemberPrincipalArgumentResolver;
import touch.baton.tobe.domain.oauth.query.controller.resolver.AuthRunnerPrincipalArgumentResolver;
import touch.baton.tobe.domain.oauth.query.controller.resolver.AuthSupporterPrincipalArgumentResolver;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

@ExtendWith(RestDocumentationExtension.class)
@Import({RestDocsResultConfig.class, ConverterConfig.class})
public abstract class RestdocsConfig {

    protected MockMvc mockMvc;
    protected AuthMemberPrincipalArgumentResolver authMemberPrincipalArgumentResolver;
    protected AuthRunnerPrincipalArgumentResolver authRunnerPrincipalArgumentResolver;
    protected AuthSupporterPrincipalArgumentResolver authSupporterPrincipalArgumentResolver;

    @Autowired
    protected RestDocumentationResultHandler restDocs;

    @Autowired
    protected RestDocumentationContextProvider restDocumentation;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected JwtDecoder jwtDecoder;

    @MockBean
    protected OauthMemberCommandRepository oauthMemberCommandRepository;

    @MockBean
    protected OauthRunnerCommandRepository oauthRunnerCommandRepository;

    @MockBean
    protected OauthSupporterCommandRepository oauthSupporterCommandRepository;

    @Autowired
    protected MappingJackson2HttpMessageConverter jackson2HttpMessageConverter;

    @Autowired
    protected Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder;

    protected void restdocsSetUp(final Object controller) {
        authMemberPrincipalArgumentResolver = new AuthMemberPrincipalArgumentResolver(jwtDecoder, oauthMemberCommandRepository);
        authRunnerPrincipalArgumentResolver = new AuthRunnerPrincipalArgumentResolver(jwtDecoder, oauthRunnerCommandRepository);
        authSupporterPrincipalArgumentResolver = new AuthSupporterPrincipalArgumentResolver(jwtDecoder, oauthSupporterCommandRepository);

        final FormattingConversionService formattingConversionService = new FormattingConversionService();
        formattingConversionService.addConverter(new OauthTypeConverter());
        formattingConversionService.addConverter(new ReviewStatusConverter());

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(
                        authMemberPrincipalArgumentResolver,
                        authRunnerPrincipalArgumentResolver,
                        authSupporterPrincipalArgumentResolver,
                        new PageableHandlerMethodArgumentResolver())
                .apply(documentationConfiguration(restDocumentation))
                .setConversionService(formattingConversionService)
                .setMessageConverters(jackson2HttpMessageConverter)
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
