package touch.baton.domain.tag.query.service;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import touch.baton.domain.tag.command.Tag;
import touch.baton.domain.tag.command.vo.TagReducedName;
import touch.baton.domain.tag.query.repository.TagQuerydslRepository;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TagQueryService {

    private final TagQuerydslRepository tagQuerydslRepository;

    public List<Tag> readTagsByReducedName(@Nullable final TagReducedName tagReducedName, final int limit) {
        return tagQuerydslRepository.findByTagReducedName(tagReducedName, limit);
    }
}
