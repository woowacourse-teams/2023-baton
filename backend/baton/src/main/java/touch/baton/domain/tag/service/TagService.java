package touch.baton.domain.tag.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import touch.baton.domain.tag.Tag;
import touch.baton.domain.tag.repository.TagRepository;
import touch.baton.domain.tag.vo.TagReducedName;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TagService {

    private final TagRepository tagRepository;

    public List<Tag> readTagsByReducedName(final String tagName) {
        final String reducedName = TagReducedName.from(tagName).getValue();
        return tagRepository.findTop10ByTagReducedNameValueContainingOrderByTagReducedNameValueAsc(reducedName);
    }
}
