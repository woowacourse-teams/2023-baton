package touch.baton.domain.runnerpost.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.repository.RunnerPostRepository;
import touch.baton.domain.runnerpost.service.dto.RunnerPostCreateRequest;
import touch.baton.domain.tag.RunnerPostTag;
import touch.baton.domain.tag.Tag;
import touch.baton.domain.tag.repository.TagRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RunnerPostService {

    private final RunnerPostRepository runnerPostRepository;
    private final TagRepository tagRepository;

    @Transactional
    public Long create(final Runner runner, final RunnerPostCreateRequest request) {
        RunnerPost runnerPost = toDomain(runner, request);

        final List<Tag> tags = request.tags().stream()
                .map(Tag::newInstance)
                .toList();

        for (final Tag tag : tags) {
            final Optional<Tag> findTag = tagRepository.findByTagName(tag.getTagName());

            if (findTag.isEmpty()) {
                tagRepository.save(tag);
                continue;
            }

            final Tag presentTag = findTag.get();
            presentTag.increaseCount();
        }

        runnerPostRepository.save(runnerPost);

        final List<RunnerPostTag> postTags = tags.stream()
                .map(tag -> RunnerPostTag.builder()
                        .tag(tag)
                        .runnerPost(runnerPost)
                        .build())
                .toList();

        for (final RunnerPostTag postTag : postTags) {
            runnerPost.addPostTag(postTag);
        }

        return runnerPost.getId();
    }

    private RunnerPost toDomain(final Runner runner, final RunnerPostCreateRequest request) {
        return RunnerPost.newInstance(request.title(),
                request.contents(),
                request.pullRequestUrl(),
                request.deadline(),
                runner);
    }
}
