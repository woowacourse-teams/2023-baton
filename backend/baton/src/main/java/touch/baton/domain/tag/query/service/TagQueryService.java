package touch.baton.domain.tag.query.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import touch.baton.domain.tag.command.Tag;
import touch.baton.domain.tag.command.vo.TagReducedName;
import touch.baton.domain.tag.query.repository.TagQuerydslRepository;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TagQueryService {

    private final TagQuerydslRepository tagQuerydslRepository;

    public List<Tag> readTagsByReducedName(final TagReducedName tagReducedName, final int limit) {
        if (tagReducedName == null || tagReducedName.getValue().isBlank()) {
            return Collections.emptyList();
        }

        return tagQuerydslRepository.findByTagReducedName(tagReducedName, limit);
    }
}
