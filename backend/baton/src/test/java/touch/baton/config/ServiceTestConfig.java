package touch.baton.config;

import org.springframework.beans.factory.annotation.Autowired;
import touch.baton.domain.feedback.repository.SupporterFeedbackRepository;
import touch.baton.tobe.domain.member.command.repository.MemberCommandRepository;
import touch.baton.domain.runner.repository.RunnerRepository;
import touch.baton.domain.runnerpost.repository.RunnerPostRepository;
import touch.baton.domain.supporter.repository.SupporterRepository;
import touch.baton.domain.supporter.repository.SupporterRunnerPostRepository;
import touch.baton.domain.tag.repository.RunnerPostTagRepository;
import touch.baton.domain.tag.repository.TagRepository;
import touch.baton.domain.technicaltag.repository.RunnerTechnicalTagRepository;
import touch.baton.domain.technicaltag.repository.SupporterTechnicalTagRepository;
import touch.baton.domain.technicaltag.repository.TechnicalTagRepository;

public abstract class ServiceTestConfig extends RepositoryTestConfig {

    @Autowired
    protected MemberCommandRepository memberCommandRepository;

    @Autowired
    protected RunnerRepository runnerRepository;

    @Autowired
    protected SupporterRepository supporterRepository;

    @Autowired
    protected RunnerPostRepository runnerPostRepository;

    @Autowired
    protected SupporterRunnerPostRepository supporterRunnerPostRepository;

    @Autowired
    protected RunnerPostTagRepository runnerPostTagRepository;

    @Autowired
    protected TagRepository tagRepository;

    @Autowired
    protected SupporterFeedbackRepository supporterFeedbackRepository;

    @Autowired
    protected TechnicalTagRepository technicalTagRepository;

    @Autowired
    protected RunnerTechnicalTagRepository runnerTechnicalTagRepository;

    @Autowired
    protected SupporterTechnicalTagRepository supporterTechnicalTagRepository;
}
