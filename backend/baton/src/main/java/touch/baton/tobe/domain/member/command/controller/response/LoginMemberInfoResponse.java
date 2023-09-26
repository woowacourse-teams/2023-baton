package touch.baton.tobe.domain.member.command.controller.response;

import touch.baton.tobe.domain.member.command.Member;

public record LoginMemberInfoResponse(String name, String imageUrl) {

    public static LoginMemberInfoResponse from(final Member member) {
        return new LoginMemberInfoResponse(member.getMemberName().getValue(), member.getImageUrl().getValue());
    }
}
