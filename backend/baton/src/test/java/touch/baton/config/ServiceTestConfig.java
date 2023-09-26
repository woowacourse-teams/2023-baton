package touch.baton.config;

import org.springframework.beans.factory.annotation.Autowired;
import touch.baton.domain.feedback.repository.SupporterFeedbackRepository;
import touch.baton.domain.tag.repository.RunnerPostTagQueryRepository;
import touch.baton.domain.tag.repository.TagQueryRepository;
import touch.baton.domain.technicaltag.repository.RunnerTechnicalTagRepository;
import touch.baton.domain.technicaltag.repository.SupporterTechnicalTagRepository;
import touch.baton.domain.technicaltag.repository.TechnicalTagRepository;
import touch.baton.tobe.domain.member.command.repository.MemberCommandRepository;
import touch.baton.tobe.domain.member.command.repository.SupporterCommandRepository;
import touch.baton.tobe.domain.member.command.repository.SupporterRunnerPostCommandRepository;
import touch.baton.tobe.domain.member.query.repository.RunnerQueryRepository;
import touch.baton.tobe.domain.member.query.repository.SupporterQueryRepository;
import touch.baton.tobe.domain.member.query.repository.SupporterRunnerPostQueryRepository;
import touch.baton.tobe.domain.runnerpost.command.repository.RunnerPostCommandRepository;
import touch.baton.tobe.domain.runnerpost.query.repository.RunnerPostQueryRepository;
import touch.baton.tobe.domain.tag.command.repository.TagCommandRepository;

public abstract class ServiceTestConfig extends RepositoryTestConfig {

    @Autowired
    protected MemberCommandRepository memberCommandRepository;

    @Autowired
    protected RunnerQueryRepository runnerQueryRepository;

    @Autowired
    protected SupporterQueryRepository supporterQueryRepository;

    @Autowired
    protected RunnerPostQueryRepository runnerPostQueryRepository;

    @Autowired
    protected SupporterRunnerPostQueryRepository supporterRunnerPostQueryRepository;

    @Autowired
    protected RunnerPostTagQueryRepository runnerPostTagQueryRepository;

    @Autowired
    protected TagQueryRepository tagQueryRepository;

    @Autowired
    protected SupporterFeedbackRepository supporterFeedbackRepository;

    @Autowired
    protected TechnicalTagRepository technicalTagRepository;

    @Autowired
    protected RunnerTechnicalTagRepository runnerTechnicalTagRepository;

    @Autowired
    protected SupporterTechnicalTagRepository supporterTechnicalTagRepository;

    @Autowired
    protected RunnerPostCommandRepository runnerPostCommandRepository;

    @Autowired
    protected TagCommandRepository tagCommandRepository;

    @Autowired
    protected SupporterCommandRepository supporterCommandRepository;

    @Autowired
    protected SupporterRunnerPostCommandRepository supporterRunnerPostCommandRepository;
}
