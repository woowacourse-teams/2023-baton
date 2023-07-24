package touch.baton.fixture.repository;

import touch.baton.domain.tag.Tag;
import touch.baton.domain.tag.repository.TagRepository;

public class TagRepositoryFixture {

    private final TagRepository tagRepository;

    public TagRepositoryFixture(final TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Tag save(final Tag tag) {
        return tagRepository.save(tag);
    }
}
