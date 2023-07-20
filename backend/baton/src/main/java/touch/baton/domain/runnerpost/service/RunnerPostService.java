package touch.baton.domain.runnerpost.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import touch.baton.domain.common.vo.Contents;
import touch.baton.domain.common.vo.Title;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.exception.RunnerPostBusinessException;
import touch.baton.domain.runnerpost.repository.RunnerPostRepository;
import touch.baton.domain.runnerpost.service.dto.RunnerPostCreateRequest;
import touch.baton.domain.runnerpost.service.dto.RunnerPostUpdateRequest;
import touch.baton.domain.runnerpost.vo.Deadline;
import touch.baton.domain.runnerpost.vo.PullRequestUrl;
import touch.baton.domain.tag.RunnerPostTag;
import touch.baton.domain.tag.Tag;
import touch.baton.domain.tag.repository.RunnerPostTagRepository;
import touch.baton.domain.tag.repository.TagRepository;
import touch.baton.domain.tag.vo.TagName;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RunnerPostService {

    private final RunnerPostRepository runnerPostRepository;
    private final RunnerPostTagRepository runnerPostTagRepository;
    private final TagRepository tagRepository;

    @Transactional
    public Long createRunnerPost(final Runner runner, final RunnerPostCreateRequest request) {
        RunnerPost runnerPost = toDomain(runner, request);

        final List<Tag> tags = request.tags().stream()
                .map(Tag::newInstance)
                .toList();

        for (final Tag tag : tags) {
            final Optional<Tag> maybeTag = tagRepository.findByTagName(tag.getTagName());

            if (maybeTag.isEmpty()) {
                tagRepository.save(tag);
                continue;
            }

            final Tag presentTag = maybeTag.get();
            presentTag.increaseCount();
        }

        runnerPostRepository.save(runnerPost);

        final List<RunnerPostTag> postTags = tags.stream()
                .map(tag -> RunnerPostTag.builder()
                        .tag(tag)
                        .runnerPost(runnerPost)
                        .build())
                .toList();

        runnerPost.addAllRunnerPostTags(postTags);
        return runnerPost.getId();
    }

    public RunnerPost readByRunnerPostId(final Long runnerPostId) {
        final List<RunnerPostTag> findRunnerPostTags = runnerPostTagRepository.joinTagByRunnerPostId(runnerPostId);
        final RunnerPost findRunnerPost = runnerPostRepository.joinMemberByRunnerPostId(runnerPostId)
                .orElseThrow(() -> new RunnerPostBusinessException.NotFound("러너 게시글 식별자값으로 러너 게시글을 조회할 수 없습니다."));

        findRunnerPost.addAllRunnerPostTags(findRunnerPostTags);

        return findRunnerPost;
    }

    @Transactional
    public void deleteByRunnerPostId(final Long runnerPostId) {
        final Optional<RunnerPost> maybeRunnerPost = runnerPostRepository.findById(runnerPostId);
        if (maybeRunnerPost.isEmpty()) {
            throw new RunnerPostBusinessException.NotFound("러너 게시글 식별자값으로 삭제할 러너 게시글이 존재하지 않습니다.");
        }

        runnerPostTagRepository.joinTagByRunnerPostId(runnerPostId)
                .stream()
                .map(RunnerPostTag::getTag)
                .forEach(Tag::decreaseCount);

        runnerPostRepository.deleteById(runnerPostId);
    }

    private RunnerPost toDomain(final Runner runner, final RunnerPostCreateRequest request) {
        return RunnerPost.newInstance(request.title(),
                request.contents(),
                request.pullRequestUrl(),
                request.deadline(),
                runner);
    }

    @Transactional
    public Long updateRunnerPost(final Long runnerPostId, final RunnerPostUpdateRequest request) {
        // TODO: 메소드 분리
        final RunnerPost runnerPost = runnerPostRepository.findById(runnerPostId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 runnerPostId 입니다."));
        runnerPost.updateTitle(new Title(request.getTitle()));
        runnerPost.updateContents(new Contents(request.getContents()));
        runnerPost.updatePullRequestUrl(new PullRequestUrl(request.getPullRequestUrl()));
        runnerPost.updateDeadLine(new Deadline(request.getDeadline()));

        final List<RunnerPostTag> presentRunnerPostTags =
                runnerPostTagRepository.joinTagByRunnerPostId(runnerPost.getId());
        // TODO: tag 개수 차감 메소드 분리
        final List<touch.baton.domain.tag.Tag> presentTags = presentRunnerPostTags.stream()
                .map(RunnerPostTag::getTag)
                .toList();
        presentTags.forEach(Tag::decreaseCount);

        // TODO: 새로운 tag 로 교체 메소드 분리
        final List<RunnerPostTag> removedRunnerPostTags = new ArrayList<>(presentRunnerPostTags);
        for (String tagName : request.getTags()) {
            final Optional<RunnerPostTag> existRunnerPostTag = presentRunnerPostTags.stream()
                    .filter(presentRunnerPostTag -> presentRunnerPostTag.isSameTagName(tagName))
                    .findFirst();
            if (existRunnerPostTag.isPresent()) {
                removedRunnerPostTags.remove(existRunnerPostTag.get());
                existRunnerPostTag.get().getTag().increaseCount();
            }
            if (existRunnerPostTag.isEmpty()) {
                // TODO: tag 찾기 메소드 분리
                final Optional<Tag> tag = tagRepository.findByTagName(new TagName(tagName));
                if (tag.isEmpty()) {
                    final Tag newTag = tagRepository.save(Tag.newInstance(tagName));
                    final RunnerPostTag newRunnerPostTag = runnerPostTagRepository.save(RunnerPostTag.builder()
                            .runnerPost(runnerPost)
                            .tag(newTag)
                            .build());
                    runnerPost.appendRunnerPostTag(newRunnerPostTag);
                }
                if (tag.isPresent()) {
                    tag.get().increaseCount();
                    final RunnerPostTag newRunnerPostTag = runnerPostTagRepository.save(RunnerPostTag.builder()
                            .runnerPost(runnerPost)
                            .tag(tag.get())
                            .build());
                    runnerPost.appendRunnerPostTag(newRunnerPostTag);
                }
            }
        }
        runnerPostTagRepository.deleteAll(removedRunnerPostTags);

        return runnerPost.getId();
    }
    
    public List<RunnerPost> readAllRunnerPosts() {
        return runnerPostRepository.findAll();
    }
}
