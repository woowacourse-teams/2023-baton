package touch.baton.tobe.domain.member.command.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import touch.baton.domain.common.vo.Introduction;
import touch.baton.domain.common.vo.TagName;
import touch.baton.tobe.domain.member.command.Runner;
import touch.baton.tobe.domain.member.command.service.dto.RunnerUpdateRequest;
import touch.baton.tobe.domain.member.command.vo.Company;
import touch.baton.tobe.domain.member.command.vo.MemberName;
import touch.baton.tobe.domain.technicaltag.command.RunnerTechnicalTag;
import touch.baton.tobe.domain.technicaltag.command.TechnicalTag;
import touch.baton.tobe.domain.technicaltag.command.repository.RunnerTechnicalTagCommandRepository;
import touch.baton.tobe.domain.technicaltag.query.repository.TechnicalTagQueryRepository;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class RunnerCommandService {

    private final RunnerTechnicalTagCommandRepository runnerTechnicalTagCommandRepository;
    private final TechnicalTagQueryRepository technicalTagQueryRepository;

    public void updateRunner(Runner runner, RunnerUpdateRequest runnerUpdateRequest) {
        runner.updateMemberName(new MemberName(runnerUpdateRequest.name()));
        runner.updateCompany(new Company(runnerUpdateRequest.company()));
        runner.updateIntroduction(new Introduction(runnerUpdateRequest.introduction()));
        updateTechnicalTags(runner, runnerUpdateRequest.technicalTags());
    }

    private void updateTechnicalTags(final Runner runner, final List<String> technicalTags) {
        runnerTechnicalTagCommandRepository.deleteByRunner(runner);
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
        return technicalTagQueryRepository.findByTagName(tagName)
                .orElseGet(() -> technicalTagQueryRepository.save(
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
        return runnerTechnicalTagCommandRepository.save(runnerTechnicalTag);
    }
}
