package touch.baton.domain.runnerpost.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import touch.baton.domain.common.vo.Contents;
import touch.baton.domain.common.vo.Title;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.repository.RunnerPostRepository;
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
    public Long update(final Long runnerPostId, final RunnerPostUpdateRequest request) {
        // TODO: 메소드 분리
        final RunnerPost runnerPost = runnerPostRepository.findById(runnerPostId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 runnerPostId 입니다."));
        runnerPost.updateTitle(new Title(request.getTitle()));
        runnerPost.updateContents(new Contents(request.getContents()));
        runnerPost.updatePullRequestUrl(new PullRequestUrl(request.getPullRequestUrl()));
        runnerPost.updateDeadLine(new Deadline(request.getDeadline()));

        final List<RunnerPostTag> presentRunnerPostTags =
                runnerPostTagRepository.joinTagsByRunnerPostId(runnerPost.getId());
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
}
