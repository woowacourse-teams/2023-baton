package touch.baton.document.tag.read;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import touch.baton.config.RestdocsConfig;
import touch.baton.domain.tag.Tag;
import touch.baton.domain.tag.controller.TagController;
import touch.baton.domain.tag.service.TagService;
import touch.baton.fixture.domain.TagFixture;

import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.mockito.ArgumentMatchers.any;
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

@WebMvcTest(TagController.class)
class TagReadApiTest extends RestdocsConfig {

    @MockBean
    private TagService tagService;

    @BeforeEach
    void setUp() {
        final TagController tagController = new TagController(tagService);
        restdocsSetUp(tagController);
    }


    @DisplayName("태그 검색 API")
    @Test
    void readTags() throws Exception {
        // given
        final Tag javaTag = TagFixture.create(tagName("java"));
        final Tag javascriptTag = TagFixture.create(tagName("javascript"));
        final Tag javaTagSpy = spy(javaTag);
        final Tag javascriptTagSpy = spy(javascriptTag);

        // when
        when(tagService.readTagsByReducedName("java"))
                .thenReturn(List.of(javaTagSpy, javascriptTagSpy));
        when(javaTagSpy.getId())
                .thenReturn(1L);
        when(javascriptTagSpy.getId())
                .thenReturn(2L);

        // then
        mockMvc.perform(get("/api/v1/tags/search")
                        .characterEncoding(UTF_8)
                        .accept(APPLICATION_JSON)
                        .queryParam("name", "java"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andDo(restDocs.document(
                        queryParameters(
                                parameterWithName("name").description("태그 이름")
                        ),
                        responseFields(
                                fieldWithPath("data.[].id").type(NUMBER).description("태그 식별자값(id)"),
                                fieldWithPath("data.[].tagName").type(STRING).optional().description("태그 이름")
                        ))
                );
    }
}
