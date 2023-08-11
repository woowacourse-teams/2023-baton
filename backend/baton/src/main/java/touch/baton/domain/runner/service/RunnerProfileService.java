package touch.baton.domain.runner.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import touch.baton.domain.common.vo.Introduction;
import touch.baton.domain.common.vo.TagName;
import touch.baton.domain.member.vo.Company;
import touch.baton.domain.member.vo.MemberName;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runner.service.dto.RunnerUpdateRequest;
import touch.baton.domain.tag.repository.RunnerTechnicalTagRepository;
import touch.baton.domain.technicaltag.RunnerTechnicalTag;
import touch.baton.domain.technicaltag.TechnicalTag;
import touch.baton.domain.technicaltag.repository.TechnicalTagRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class RunnerProfileService {

    private final RunnerTechnicalTagRepository runnerTechnicalTagRepository;
    private final TechnicalTagRepository technicalTagRepository;

    public void updateRunnerProfile(Runner runner, RunnerUpdateRequest runnerUpdateRequest) {
        runner.updateMemberName(new MemberName(runnerUpdateRequest.name()));
        runner.updateCompany(new Company(runnerUpdateRequest.company()));
        runner.updateIntroduction(new Introduction(runnerUpdateRequest.introduction()));
        runner.updateRunnerTechnicalTags(convertToRunnerTechnicalTags(runner, runnerUpdateRequest));
    }

    private List<RunnerTechnicalTag> convertToRunnerTechnicalTags(final Runner runner, final RunnerUpdateRequest runnerUpdateRequest) {
        return runnerUpdateRequest.technicalTags().stream()
                .map(tagName -> {
                    TechnicalTag technicalTag = TechnicalTag.builder()
                            .tagName(new TagName(tagName))
                            .build();

                    technicalTagRepository.save(technicalTag);

                    RunnerTechnicalTag runnerTechnicalTag = RunnerTechnicalTag.builder()
                            .runner(runner)
                            .technicalTag(technicalTag)
                            .build();
                    runnerTechnicalTagRepository.save(runnerTechnicalTag);
                    return runnerTechnicalTag;
                })
                .collect(Collectors.toList());
    }
}
