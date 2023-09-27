package touch.baton.domain.tag.query.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import touch.baton.domain.tag.command.Tag;
import touch.baton.domain.tag.command.vo.TagReducedName;
import touch.baton.domain.tag.query.repository.TagQueryRepository;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TagQueryService {

    private final TagQueryRepository tagQueryRepository;

    public List<Tag> readTagsByReducedName(final String tagName) {
        return tagQueryRepository.readTagsByReducedName(TagReducedName.from(tagName));
    }
}
