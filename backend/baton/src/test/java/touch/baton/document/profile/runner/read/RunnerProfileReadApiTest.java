package touch.baton.document.profile.runner.read;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import touch.baton.domain.runner.controller.RunnerProfileController;
import touch.baton.domain.runner.service.RunnerService;
import touch.baton.domain.runnerpost.service.RunnerPostService;

@WebMvcTest(RunnerProfileController.class)
public class RunnerProfileReadApiTest {

    @MockBean
    private RunnerPostService runnerPostService;

    @MockBean
    private RunnerService runnerService;

    @DisplayName("러너 프로필 조회 API")
    @Test
    void read() throws Exception {
        // TODO: 로그인 들어오면 작성하겠삼.
//        // given
//        final Runner runner = RunnerFixture.createRunner(MemberFixture.createHyena());
//        final Deadline deadline = deadline(LocalDateTime.now().plusHours(100));
//        final Tag javaTag = TagFixture.create(tagName("자바"), tagCount(10));
//        final RunnerPost runnerPost = RunnerPostFixture.create(runner, deadline, List.of(javaTag));
//        final RunnerPost spyRunnerPost = spy(runnerPost);
//
//        // when
//
//        // then
    }
}
