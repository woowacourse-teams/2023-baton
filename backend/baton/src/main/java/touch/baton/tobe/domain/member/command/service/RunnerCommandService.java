package touch.baton.tobe.domain.member.command.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import touch.baton.domain.common.vo.Introduction;
import touch.baton.domain.common.vo.TagName;
import touch.baton.domain.technicaltag.RunnerTechnicalTag;
import touch.baton.domain.technicaltag.TechnicalTag;
import touch.baton.domain.technicaltag.repository.RunnerTechnicalTagRepository;
import touch.baton.domain.technicaltag.repository.TechnicalTagRepository;
import touch.baton.tobe.domain.member.command.Runner;
import touch.baton.tobe.domain.member.command.service.dto.RunnerUpdateRequest;
import touch.baton.tobe.domain.member.command.vo.Company;
import touch.baton.tobe.domain.member.command.vo.MemberName;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class RunnerCommandService {

    private final RunnerTechnicalTagRepository runnerTechnicalTagRepository;
    private final TechnicalTagRepository technicalTagRepository;

    public void updateRunner(Runner runner, RunnerUpdateRequest runnerUpdateRequest) {
        runner.updateMemberName(new MemberName(runnerUpdateRequest.name()));
        runner.updateCompany(new Company(runnerUpdateRequest.company()));
        runner.updateIntroduction(new Introduction(runnerUpdateRequest.introduction()));
        updateTechnicalTags(runner, runnerUpdateRequest.technicalTags());
    }

    private void updateTechnicalTags(final Runner runner, final List<String> technicalTags) {
        runnerTechnicalTagRepository.deleteByRunner(runner);
        createRunnerTechnicalTags(runner, technicalTags);
    }

    private List<RunnerTechnicalTag> createRunnerTechnicalTags(final Runner runner, final List<String> technicalTags) {
        return technicalTags.stream()
                .map(tagName -> createRunnerTechnicalTag(runner, new TagName(tagName)))
                .toList();
    }

    private RunnerTechnicalTag createRunnerTechnicalTag(final Runner runner, final TagName tagName) {
        final TechnicalTag technicalTag = findTechnicalTagIfExistElseCreate(tagName);
        return createRunnerTechnicalTagAndSave(runner, technicalTag);
    }

    private TechnicalTag findTechnicalTagIfExistElseCreate(final TagName tagName) {
        return technicalTagRepository.findByTagName(tagName)
                .orElseGet(() -> technicalTagRepository.save(
                        TechnicalTag.builder()
                                .tagName(tagName)
                                .build())
                );
    }

    private RunnerTechnicalTag createRunnerTechnicalTagAndSave(final Runner runner, final TechnicalTag technicalTag) {
        RunnerTechnicalTag runnerTechnicalTag = RunnerTechnicalTag.builder()
                .runner(runner)
                .technicalTag(technicalTag)
                .build();
        return runnerTechnicalTagRepository.save(runnerTechnicalTag);
    }
}
