package touch.baton.domain.runnerpost.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import touch.baton.domain.common.vo.Contents;
import touch.baton.domain.common.vo.TagName;
import touch.baton.domain.common.vo.Title;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.exception.RunnerPostBusinessException;
import touch.baton.domain.runnerpost.repository.RunnerPostRepository;
import touch.baton.domain.runnerpost.service.dto.RunnerPostCreateRequest;
import touch.baton.domain.runnerpost.service.dto.RunnerPostCreateTestRequest;
import touch.baton.domain.runnerpost.service.dto.RunnerPostUpdateRequest;
import touch.baton.domain.runnerpost.vo.Deadline;
import touch.baton.domain.runnerpost.vo.PullRequestUrl;
import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.supporter.repository.SupporterRepository;
import touch.baton.domain.tag.RunnerPostTag;
import touch.baton.domain.tag.Tag;
import touch.baton.domain.tag.repository.RunnerPostTagRepository;
import touch.baton.domain.tag.repository.TagRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RunnerPostService {

    private final RunnerPostRepository runnerPostRepository;
    private final RunnerPostTagRepository runnerPostTagRepository;
    private final TagRepository tagRepository;
    private final SupporterRepository supporterRepository;

    @Transactional
    public Long createRunnerPost(final Runner runner, final RunnerPostCreateRequest request) {
        final RunnerPost runnerPost = toDomain(runner, request);
        runnerPostRepository.save(runnerPost);

        List<Tag> toSaveTags = new ArrayList<>();
        for (final String tagName : request.tags()) {
            final Optional<Tag> maybeTag = tagRepository.findByTagName(new TagName(tagName));

            if (maybeTag.isEmpty()) {
                final Tag savedTag = tagRepository.save(Tag.newInstance(tagName));
                toSaveTags.add(savedTag);
                continue;
            }

            final Tag presentTag = maybeTag.get();
            toSaveTags.add(presentTag);
        }

        final List<RunnerPostTag> postTags = toSaveTags.stream()
                .map(tag -> RunnerPostTag.builder()
                        .tag(tag)
                        .runnerPost(runnerPost).build())
                .toList();

        runnerPost.addAllRunnerPostTags(postTags);
        return runnerPost.getId();
    }

    private RunnerPost toDomain(final Runner runner, final RunnerPostCreateRequest request) {
        return RunnerPost.newInstance(request.title(),
                request.contents(),
                request.pullRequestUrl(),
                request.deadline(),
                runner);
    }

    @Transactional
    public Long createRunnerPostTest(final Runner runner, final RunnerPostCreateTestRequest request) {
        final RunnerPost runnerPost = RunnerPost.newInstance(request.title(),
                request.contents(),
                request.pullRequestUrl(),
                request.deadline(),
                runner);

        if (Objects.nonNull(request.supporterId())) {
            final Supporter supporter = supporterRepository.findById(request.supporterId())
                    .orElseThrow(() -> new RunnerPostBusinessException("RunnerPost 의 서포터가 존재하지 않습니다."));
            runnerPost.assignSupporter(supporter);
        }

        runnerPostRepository.save(runnerPost);

        List<Tag> toSaveTags = new ArrayList<>();
        for (final String tagName : request.tags()) {
            final Optional<Tag> maybeTag = tagRepository.findByTagName(new TagName(tagName));

            if (maybeTag.isEmpty()) {
                final Tag savedTag = tagRepository.save(Tag.newInstance(tagName));
                toSaveTags.add(savedTag);
                continue;
            }

            final Tag presentTag = maybeTag.get();
            toSaveTags.add(presentTag);
        }

        final List<RunnerPostTag> postTags = toSaveTags.stream()
                .map(tag -> RunnerPostTag.builder()
                        .tag(tag)
                        .runnerPost(runnerPost)
                        .build())
                .toList();

        runnerPost.addAllRunnerPostTags(postTags);
        return runnerPost.getId();
    }

    public RunnerPost readByRunnerPostId(final Long runnerPostId) {
        runnerPostTagRepository.joinTagByRunnerPostId(runnerPostId);
        return runnerPostRepository.joinMemberByRunnerPostId(runnerPostId)
                .orElseThrow(() -> new RunnerPostBusinessException("RunnerPost 의 식별자값으로 러너 게시글을 조회할 수 없습니다."));
    }

    @Transactional
    public void increaseWatchedCount(final RunnerPost runnerPost) {
        runnerPost.increaseWatchedCount();
    }

    @Transactional
    public void deleteByRunnerPostId(final Long runnerPostId, final Runner runner) {
        // FIXME: 2023/08/03 삭제 시 본인인지 확인하는 로직 넣기
        final Optional<RunnerPost> maybeRunnerPost = runnerPostRepository.findById(runnerPostId);
        if (maybeRunnerPost.isEmpty()) {
            throw new RunnerPostBusinessException("RunnerPost 의 식별자값으로 삭제할 러너 게시글이 존재하지 않습니다.");
        }

        runnerPostRepository.deleteById(runnerPostId);
    }

    @Transactional
    public Long updateRunnerPost(final Long runnerPostId, final Runner runner, final RunnerPostUpdateRequest request) {
        // TODO: 메소드 분리
        // FIXME: 2023/08/03 주인 확인 로직 넣기
        final RunnerPost runnerPost = runnerPostRepository.findById(runnerPostId)
                .orElseThrow(() -> new IllegalArgumentException("해당 runnerPostId 로 러너 게시글을 찾을 수 없습니다. runnerPostId를 다시 확인해주세요"));
        runnerPost.updateTitle(new Title(request.title()));
        runnerPost.updateContents(new Contents(request.contents()));
        runnerPost.updatePullRequestUrl(new PullRequestUrl(request.pullRequestUrl()));
        runnerPost.updateDeadLine(new Deadline(request.deadline()));

        final List<RunnerPostTag> presentRunnerPostTags = runnerPostTagRepository.joinTagByRunnerPostId(runnerPost.getId());
        // TODO: tag 개수 차감 메소드 분리
        final List<touch.baton.domain.tag.Tag> presentTags = presentRunnerPostTags.stream()
                .map(RunnerPostTag::getTag)
                .toList();

        // TODO: 새로운 tag 로 교체 메소드 분리
        final List<RunnerPostTag> removedRunnerPostTags = new ArrayList<>(presentRunnerPostTags);
        for (String tagName : request.tags()) {
            final Optional<RunnerPostTag> existRunnerPostTag = presentRunnerPostTags.stream()
                    .filter(presentRunnerPostTag -> presentRunnerPostTag.isSameTagName(tagName))
                    .findFirst();
            if (existRunnerPostTag.isPresent()) {
                removedRunnerPostTags.remove(existRunnerPostTag.get());
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
        return runnerPostRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<RunnerPost> readRunnerPostsByRunnerId(final Long runnerId) {
        return runnerPostRepository.findByRunnerId(runnerId);
    }
}
