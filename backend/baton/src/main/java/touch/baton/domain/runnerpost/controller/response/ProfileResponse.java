package touch.baton.domain.runnerpost.controller.response;

import touch.baton.domain.member.Member;

public record ProfileResponse(Long memberId,
                              String name,
                              String company,
                              String imageUrl,
                              Boolean isOwner
) {

    public static ProfileResponse from(final Member member) {
        return new ProfileResponse(
                member.getId(),
                member.getMemberName().getValue(),
                member.getCompany().getValue(),
                member.getImageUrl().getValue(),
                true
        );
    }
}
