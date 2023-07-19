package touch.baton.domain.runnerpost.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.exception.RunnerPostBusinessException;
import touch.baton.domain.runnerpost.repository.RunnerPostRepository;
import touch.baton.domain.tag.RunnerPostTag;
import touch.baton.domain.tag.Tag;
import touch.baton.domain.tag.repository.RunnerPostTagRepository;
import touch.baton.domain.tag.repository.TagRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RunnerPostService {

    private final RunnerPostRepository runnerPostRepository;
    private final RunnerPostTagRepository runnerPostTagRepository;
    private final TagRepository tagRepository;

    public RunnerPost readByRunnerPostId(final Long runnerPostId) {
        final List<RunnerPostTag> findRunnerPostTags = runnerPostTagRepository.joinTagsByRunnerPostId(runnerPostId);
        final RunnerPost findRunnerPost = runnerPostRepository.joinMemberByRunnerPostId(runnerPostId)
                .orElseThrow(() -> new RunnerPostBusinessException.NotFound("러너 게시글 식별자값으로 러너 게시글을 조회할 수 없습니다."));

        findRunnerPost.addRunnerPostTags(findRunnerPostTags);
        return findRunnerPost;
    }

    @Transactional
    public void deleteByRunnerPostId(final Long runnerPostId) {
        final Optional<RunnerPost> maybeRunnerPost = runnerPostRepository.findById(runnerPostId);
        if (maybeRunnerPost.isEmpty()) {
            throw new RunnerPostBusinessException.NotFound("러너 게시글 식별자값으로 삭제할 러너 게시글이 존재하지 않습니다.");
        }

        runnerPostTagRepository.joinTagsByRunnerPostId(runnerPostId)
                .stream()
                .map(RunnerPostTag::getTag)
                .forEach(Tag::decreaseCount);

        runnerPostRepository.deleteById(runnerPostId);
    }
}
