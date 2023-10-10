package touch.baton.document.runnerpost.read;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import touch.baton.config.RestdocsConfig;
import touch.baton.domain.tag.command.Tag;
import touch.baton.domain.tag.command.vo.TagReducedName;
import touch.baton.fixture.domain.TagFixture;

import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static touch.baton.fixture.vo.TagNameFixture.tagName;

class TagReadApiTest extends RestdocsConfig {

    @DisplayName("태그 검색 API")
    @Test
    void readTagsByReducedName() throws Exception {
        // given
        final Tag javaTag = TagFixture.create(tagName("java"));
        final Tag javascriptTag = TagFixture.create(tagName("javascript"));
        final Tag javaTagSpy = spy(javaTag);
        final Tag javascriptTagSpy = spy(javascriptTag);

        // when
        when(tagQueryService.readTagsByReducedName(TagReducedName.nullableInstance("java"), 10))
                .thenReturn(List.of(javaTagSpy, javascriptTagSpy));
        when(javaTagSpy.getId())
                .thenReturn(1L);
        when(javascriptTagSpy.getId())
                .thenReturn(2L);

        // then
        mockMvc.perform(get("/api/v1/tags/search")
                        .characterEncoding(UTF_8)
                        .queryParam("tagName", "java"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andDo(restDocs.document(
                        queryParameters(
                                parameterWithName("tagName").description("태그 이름")
                        ),
                        responseFields(
                                fieldWithPath("data.[].id").type(NUMBER).description("태그 식별자값(id)"),
                                fieldWithPath("data.[].tagName").type(STRING).optional().description("태그 이름")
                        ))
                );
    }
}
