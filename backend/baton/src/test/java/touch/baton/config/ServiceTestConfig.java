package touch.baton.config;

import org.springframework.beans.factory.annotation.Autowired;
import touch.baton.domain.member.repository.MemberRepository;
import touch.baton.domain.runner.repository.RunnerRepository;
import touch.baton.domain.runnerpost.repository.RunnerPostRepository;
import touch.baton.domain.tag.repository.RunnerPostTagRepository;
import touch.baton.domain.tag.repository.TagRepository;

public abstract class ServiceTestConfig extends RepositoryTestConfig {

    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected RunnerRepository runnerRepository;

    @Autowired
    protected RunnerPostRepository runnerPostRepository;

    @Autowired
    protected RunnerPostTagRepository runnerPostTagRepository;

    @Autowired
    protected TagRepository tagRepository;
}
