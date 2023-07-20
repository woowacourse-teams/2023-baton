package touch.baton.domain.runnerpost.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import touch.baton.domain.runner.service.RunnerService;
import touch.baton.domain.runnerpost.service.RunnerPostService;
import touch.baton.domain.runnerpost.service.dto.RunnerPostUpdateRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RunnerPostController.class)
class RunnerPostControllerTest {

    private static final String API_URL_PREFIX = "/api/v1/posts/runner";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RunnerPostService runnerPostService;

    @MockBean
    private RunnerService runnerService;

    @DisplayName("러너 게시글 수정 - PUT /posts/runner/{runnerPostId}")
    @Nested
    class Update {

        @DisplayName("성공")
        @Test
        void  success() throws Exception {
            // given
            final Long runnerId = 1L;
            final Long runnerPostId = 1L;
            final RunnerPostUpdateRequest runnerPostUpdateRequest = new RunnerPostUpdateRequest(
                    "hello", List.of("java"), "naver.com", LocalDateTime.of(2023, 7, 21, 12, 12), "내용내용");
            final String requestBody = objectMapper.writeValueAsString(runnerPostUpdateRequest);

            // when
            when(runnerPostService.updateRunnerPost(eq(runnerPostId), any())).thenReturn(runnerPostId);

            // then
            mockMvc.perform(put(API_URL_PREFIX + "/{runnerPostId}", runnerPostId)
                            .content(requestBody)
                            .contentType(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(header().string("Location", API_URL_PREFIX + "/1"))
                    .andExpect(status().isCreated());
        }
    }
}
