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

    //response가 json형식인지 test
    //mockMvcTest 이용해서 ObjectMapper. json -> string으로 변환해서 맞는지 테스트
    public List<RunnerPost> read() {
        return runnerPostRepository.findAll();
    }

    public List<RunnerPost> findByRunnerId(Long runnerId) {
        return runnerPostRepository.findByRunnerId(runnerId);
    }

    public List<RunnerPost> findBySupporterId(Long supporterId) {
        return runnerPostRepository.findBySupporterId(supporterId);
    }

    //문맥이 조금 어색하지만 메세지 가장 앞에 title임을 보여주기 위함
    public RunnerPost findByTitle(String title) {
        return runnerPostRepository.findByTitle(new Title(title))
                .orElseThrow(() -> new IllegalArgumentException("title이 제목인 post가 존재하지 않습니다."));
    }
}
