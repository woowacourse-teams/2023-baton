package touch.baton.domain.feedback.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import touch.baton.config.RepositoryTestConfig;

class SupporterFeedbackRepositoryTest extends RepositoryTestConfig {

    @Autowired
    private EntityManager em;
    @Autowired
    private SupporterFeedbackRepository supporterFeedbackRepository;

    // FIXME: 2023/09/19 테스트 작성 좀요.
    @Disabled
    @DisplayName("러너 게시글 아이디와 서포터 아이디로 서포터 피드백 존재 유무를 확인할 수 있다.")
    @Test
    void existsByRunnerPostIdAndSupporterId() {
        // given
    }
}
