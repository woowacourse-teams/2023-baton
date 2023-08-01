package touch.baton.domain.oauth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import touch.baton.domain.member.Member;
import touch.baton.domain.member.vo.OauthId;
import touch.baton.domain.oauth.OauthInformation;
import touch.baton.domain.oauth.OauthType;
import touch.baton.domain.oauth.authcode.AuthCodeRequestUrlProviderComposite;
import touch.baton.domain.oauth.client.OauthInformationClientComposite;
import touch.baton.domain.oauth.repository.OauthMemberRepository;

@RequiredArgsConstructor
@Service
public class OauthService {

    private final AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite;
    private final OauthInformationClientComposite oauthInformationClientComposite;
    private final OauthMemberRepository oauthMemberRepository;

    public String readAuthCodeRedirect(final OauthType oauthType) {
        return authCodeRequestUrlProviderComposite.findRequestUrl(oauthType);
    }

    public OauthId login(final OauthType oauthType, final String code) {
        final OauthInformation oauthInformation = oauthInformationClientComposite.fetchInformation(oauthType, code);
        final Member foundMember = oauthMemberRepository.findMemberByOauthId(oauthInformation.getOauthId())
                .orElseGet(() -> saveNewMember(oauthInformation));

        return foundMember.getOauthId();
    }

    private Member saveNewMember(final OauthInformation oauthInformation) {
        final Member newMember = Member.builder()
                .memberName(oauthInformation.getMemberName())
                .email(oauthInformation.getEmail())
                .oauthId(oauthInformation.getOauthId())
                .githubUrl(oauthInformation.getGithubUrl())
                .company(new Company(""))
                .imageUrl(oauthInformation.getImageUrl())
                .build();

        return oauthMemberRepository.save(newMember);
    }
}
