package touch.baton.domain.supporter.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.supporter.repository.SupporterRepository;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SupporterService {

    private final SupporterRepository supporterRepository;

    public List<Supporter> readAllSupporters() {
        return supporterRepository.findAll();
    }
}
