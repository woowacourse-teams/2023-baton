package touch.baton.domain.runnerpost.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import touch.baton.domain.common.vo.Title;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.repository.RunnerPostRepository;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RunnerPostService {

    private final RunnerPostRepository runnerPostRepository;

    public List<RunnerPost> readAllRunnerPosts() {
        return runnerPostRepository.findAll();
    }

    public List<RunnerPost> readRunnerPostsByRunnerId(Long runnerId) {
        return runnerPostRepository.readByRunnerId(runnerId);
    }

    public List<RunnerPost> readRunnerPostsBySupporterId(Long supporterId) {
        return runnerPostRepository.readBySupporterId(supporterId);
    }

    //문맥이 조금 어색하지만 메세지 가장 앞에 title임을 보여주기 위함
    public RunnerPost readRunnerPostByTitle(String title) {
        return runnerPostRepository.readByTitle(new Title(title))
                .orElseThrow(() -> new IllegalArgumentException("title이 제목인 post가 존재하지 않습니다."));
    }
}
