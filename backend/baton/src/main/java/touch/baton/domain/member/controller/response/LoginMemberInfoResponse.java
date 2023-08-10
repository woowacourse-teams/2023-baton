package touch.baton.domain.member.controller.response;

import touch.baton.domain.member.Member;

public record LoginMemberInfoResponse(String name, String imageUrl) {

    public static LoginMemberInfoResponse from(final Member member) {
        return new LoginMemberInfoResponse(member.getMemberName().getValue(), member.getImageUrl().getValue());
    }
}
