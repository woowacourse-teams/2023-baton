package touch.baton.assure.tag;

import org.junit.jupiter.api.Test;
import touch.baton.config.AssuredTestConfig;
import touch.baton.domain.tag.Tag;
import touch.baton.fixture.domain.TagFixture;
import touch.baton.fixture.vo.TagNameFixture;

import java.util.List;

import static touch.baton.assure.tag.TagAssuredSupport.태그_검색_Detail_응답;

@SuppressWarnings("NonAsciiCharacters")
class TagReadAssuredTest extends AssuredTestConfig {

    @Test
    void 태그_검색에_성공한다() {
        // given
        tagRepository.save(TagFixture.create(TagNameFixture.tagName("java")));
        tagRepository.save(TagFixture.create(TagNameFixture.tagName("javascript")));
        tagRepository.save(TagFixture.create(TagNameFixture.tagName("script")));

        final List<Tag> 검색된_태그_목록 = tagRepository.findTop10ByTagReducedNameValueContainingOrderByTagReducedNameValueAsc("ja");

        // when, then
        TagAssuredSupport
                .클라이언트_요청()
                .태그_이름을_오름차순으로_10개_검색한다("ja")

                .서버_응답()
                .태그_검색_성공을_검증한다(
                        태그_검색_Detail_응답(검색된_태그_목록)
                );
    }
}
