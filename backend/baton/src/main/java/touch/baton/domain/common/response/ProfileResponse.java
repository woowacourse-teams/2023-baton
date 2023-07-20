package touch.baton.domain.common.response;

import lombok.Getter;
import touch.baton.domain.member.Member;

@Getter
public class ProfileResponse {

    private String name;

    private String imageUrl;

    public ProfileResponse(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public static ProfileResponse from(Member member) {
        return new ProfileResponse(
                member.getMemberName().getValue(),
                member.getImageUrl().getValue()
        );
    }
}
