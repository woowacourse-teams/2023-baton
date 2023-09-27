package touch.baton.config;

import org.springframework.beans.factory.annotation.Autowired;
import touch.baton.domain.feedback.command.repository.SupporterFeedbackCommandRepository;
import touch.baton.domain.member.command.repository.MemberCommandRepository;
import touch.baton.domain.member.command.repository.SupporterCommandRepository;
import touch.baton.domain.member.command.repository.SupporterRunnerPostCommandRepository;
import touch.baton.domain.member.query.repository.RunnerQueryRepository;
import touch.baton.domain.member.query.repository.SupporterQueryRepository;
import touch.baton.domain.member.query.repository.SupporterRunnerPostQueryRepository;
import touch.baton.domain.runnerpost.command.repository.RunnerPostCommandRepository;
import touch.baton.domain.runnerpost.query.repository.RunnerPostQueryRepository;
import touch.baton.domain.tag.command.repository.TagCommandRepository;
import touch.baton.domain.tag.query.repository.RunnerPostTagQueryRepository;
import touch.baton.domain.tag.query.repository.TagQueryRepository;
import touch.baton.domain.technicaltag.command.repository.RunnerTechnicalTagCommandRepository;
import touch.baton.domain.technicaltag.command.repository.SupporterTechnicalTagCommandRepository;
import touch.baton.domain.technicaltag.query.repository.TechnicalTagQueryRepository;

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
    protected SupporterFeedbackCommandRepository supporterFeedbackCommandRepository;

    @Autowired
    protected TechnicalTagQueryRepository technicalTagQueryRepository;

    @Autowired
    protected RunnerTechnicalTagCommandRepository runnerTechnicalTagCommandRepository;

    @Autowired
    protected SupporterTechnicalTagCommandRepository supporterTechnicalTagCommandRepository;

    @Autowired
    protected RunnerPostCommandRepository runnerPostCommandRepository;

    @Autowired
    protected TagCommandRepository tagCommandRepository;

    @Autowired
    protected SupporterCommandRepository supporterCommandRepository;

    @Autowired
    protected SupporterRunnerPostCommandRepository supporterRunnerPostCommandRepository;
}
