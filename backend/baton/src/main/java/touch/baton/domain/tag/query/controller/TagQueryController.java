package touch.baton.domain.tag.query.controller;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import touch.baton.domain.tag.command.Tag;
import touch.baton.domain.tag.command.vo.TagReducedName;
import touch.baton.domain.tag.query.controller.response.TagSearchResponses;
import touch.baton.domain.tag.query.service.TagQueryService;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/tags")
@RestController
public class TagQueryController {

    private final TagQueryService tagQueryService;

    @GetMapping("/search")
    public ResponseEntity<TagSearchResponses.Detail> readTagsByTagName(@Nullable @RequestParam(required = false) final String tagName) {
        final TagReducedName tagReducedName = TagReducedName.nullableInstance(tagName);
        final List<Tag> foundTags = tagQueryService.readTagsByReducedName(tagReducedName, 10);

        return ResponseEntity.ok(TagSearchResponses.Detail.from(foundTags));
    }
}
