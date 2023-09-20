package touch.baton.domain.runnerpost.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.repository.RunnerPostReadRepository;
import touch.baton.domain.runnerpost.repository.dto.ApplicantCountMappingDto;
import touch.baton.domain.runnerpost.vo.ReviewStatus;
import touch.baton.domain.tag.vo.TagReducedName;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RunnerPostReadService {

    private final RunnerPostReadRepository runnerPostReadRepository;

    public Page<RunnerPost> readRunnerPostByTagNameAndReviewStatus(final Pageable pageable,
                                                                   final String tagName,
                                                                   final ReviewStatus reviewStatus
    ) {
        final TagReducedName tagReducedName = TagReducedName.from(tagName);

        return runnerPostReadRepository.findByTagReducedNameAndReviewStatus(pageable, tagReducedName, reviewStatus);
    }

    public ApplicantCountMappingDto readApplicantCountMappingByRunnerPostIds(final List<Long> runnerPostIds) {
        return runnerPostReadRepository.findApplicantCountMappingByRunnerPostIds(runnerPostIds);
    }
}
