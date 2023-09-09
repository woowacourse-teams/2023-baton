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
import touch.baton.domain.oauth.controller.resolver.AuthMemberPrincipalArgumentResolver;
import touch.baton.domain.oauth.controller.resolver.AuthRunnerPrincipalArgumentResolver;
import touch.baton.domain.oauth.controller.resolver.AuthSupporterPrincipalArgumentResolver;
import touch.baton.domain.oauth.repository.OauthMemberRepository;
import touch.baton.domain.oauth.repository.OauthRunnerRepository;
import touch.baton.domain.oauth.repository.OauthSupporterRepository;
import touch.baton.domain.tag.repository.TagRepository;
import touch.baton.infra.auth.jwt.JwtDecoder;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

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
    protected OauthMemberRepository oauthMemberRepository;

    @MockBean
    protected OauthRunnerRepository oauthRunnerRepository;

    @MockBean
    protected OauthSupporterRepository oauthSupporterRepository;

    @MockBean
    protected TagRepository tagRepository;

    @Autowired
    protected MappingJackson2HttpMessageConverter jackson2HttpMessageConverter;

    @Autowired
    protected Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder;

    protected void restdocsSetUp(final Object controller) {
        authMemberPrincipalArgumentResolver = new AuthMemberPrincipalArgumentResolver(jwtDecoder, oauthMemberRepository);
        authRunnerPrincipalArgumentResolver = new AuthRunnerPrincipalArgumentResolver(jwtDecoder, oauthRunnerRepository);
        authSupporterPrincipalArgumentResolver = new AuthSupporterPrincipalArgumentResolver(jwtDecoder, oauthSupporterRepository);

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

        when(jwtDecoder.parseJwtToken(any())).thenReturn(claims);

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
