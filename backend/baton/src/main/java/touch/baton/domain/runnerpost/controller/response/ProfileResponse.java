package touch.baton.domain.runnerpost.controller.response;

import touch.baton.domain.member.Member;

public record ProfileResponse() {

    public record Detail(Long memberId,
                         String name,
                         String company,
                         String imageUrl,
                         Boolean isOwner
    ) {

        public static Detail from(final Member member) {
            return new Detail(
                    member.getId(),
                    member.getMemberName().getValue(),
                    member.getCompany().getValue(),
                    member.getImageUrl().getValue(),
                    true
            );
        }
    }

    public record Simple(String name, String imageUrl) {

        public static Simple from(final Member member) {
            return new Simple(
                    member.getMemberName().getValue(),
                    member.getImageUrl().getValue()
            );
        }
    }
}
