package touch.baton.domain.oauth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import touch.baton.domain.member.Member;
import touch.baton.domain.member.vo.Company;
import touch.baton.domain.oauth.AccessToken;
import touch.baton.domain.oauth.OauthInformation;
import touch.baton.domain.oauth.OauthType;
import touch.baton.domain.oauth.RefreshToken;
import touch.baton.domain.oauth.Token;
import touch.baton.domain.oauth.Tokens;
import touch.baton.domain.oauth.authcode.AuthCodeRequestUrlProviderComposite;
import touch.baton.domain.oauth.client.OauthInformationClientComposite;
import touch.baton.domain.oauth.repository.OauthMemberRepository;
import touch.baton.domain.oauth.repository.OauthRunnerRepository;
import touch.baton.domain.oauth.repository.OauthSupporterRepository;
import touch.baton.domain.oauth.repository.RefreshTokenRepository;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.supporter.vo.ReviewCount;
import touch.baton.domain.technicaltag.SupporterTechnicalTags;
import touch.baton.infra.auth.jwt.JwtEncoder;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class OauthService {

    private final AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite;
    private final OauthInformationClientComposite oauthInformationClientComposite;
    private final OauthMemberRepository oauthMemberRepository;
    private final OauthRunnerRepository oauthRunnerRepository;
    private final OauthSupporterRepository oauthSupporterRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtEncoder jwtEncoder;

    public String readAuthCodeRedirect(final OauthType oauthType) {
        return authCodeRequestUrlProviderComposite.findRequestUrl(oauthType);
    }

    public Tokens login(final OauthType oauthType, final String code) {
        final OauthInformation oauthInformation = oauthInformationClientComposite.fetchInformation(oauthType, code);

        final Optional<Member> maybeMember = oauthMemberRepository.findMemberByOauthId(oauthInformation.getOauthId());
        if (maybeMember.isEmpty()) {
            final Member savedMember = signUpMember(oauthInformation);
            saveNewRunner(savedMember);
            saveNewSupporter(savedMember);
            return createTokens(oauthInformation, savedMember);
        }

        return createTokens(oauthInformation, maybeMember.get());
    }

    private Member signUpMember(final OauthInformation oauthInformation) {
        final Member newMember = Member.builder()
                .memberName(oauthInformation.getMemberName())
                .socialId(oauthInformation.getSocialId())
                .oauthId(oauthInformation.getOauthId())
                .githubUrl(oauthInformation.getGithubUrl())
                .company(new Company(""))
                .imageUrl(oauthInformation.getImageUrl())
                .build();

        return oauthMemberRepository.save(newMember);
    }

    private Runner saveNewRunner(final Member member) {
        final Runner newRunner = Runner.builder()
                .member(member)
                .build();

        return oauthRunnerRepository.save(newRunner);
    }

    private Supporter saveNewSupporter(final Member member) {
        final Supporter newSupporter = Supporter.builder()
                .reviewCount(new ReviewCount(0))
                .member(member)
                .supporterTechnicalTags(new SupporterTechnicalTags(new ArrayList<>()))
                .build();

        return oauthSupporterRepository.save(newSupporter);
    }

    private Tokens createTokens(final OauthInformation oauthInformation, final Member member) {
        final String jwtToken = jwtEncoder.jwtToken(Map.of(
                "socialId", oauthInformation.getSocialId().getValue())
        );
        final AccessToken accessToken = new AccessToken(jwtToken);

        final String randomTokens = UUID.randomUUID().toString();
        final Token token = new Token(randomTokens);
        final RefreshToken refreshToken = RefreshToken.builder()
                .member(member)
                .token(token)
                .build();

        return new Tokens(accessToken, refreshToken);
    }
}
