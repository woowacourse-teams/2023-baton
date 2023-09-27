package touch.baton.tobe.domain.member.command.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import touch.baton.domain.common.vo.Introduction;
import touch.baton.domain.common.vo.TagName;
import touch.baton.tobe.domain.member.command.Supporter;
import touch.baton.tobe.domain.member.command.service.dto.SupporterUpdateRequest;
import touch.baton.tobe.domain.member.command.vo.Company;
import touch.baton.tobe.domain.member.command.vo.MemberName;
import touch.baton.tobe.domain.technicaltag.command.SupporterTechnicalTag;
import touch.baton.tobe.domain.technicaltag.command.TechnicalTag;
import touch.baton.tobe.domain.technicaltag.command.repository.SupporterTechnicalTagCommandRepository;
import touch.baton.tobe.domain.technicaltag.query.repository.TechnicalTagQueryRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class SupporterCommandService {

    private final TechnicalTagQueryRepository technicalTagQueryRepository;
    private final SupporterTechnicalTagCommandRepository supporterTechnicalTagCommandRepository;

    public void updateSupporter(final Supporter supporter, final SupporterUpdateRequest supporterUpdateRequest) {
        supporter.updateMemberName(new MemberName(supporterUpdateRequest.name()));
        supporter.updateCompany(new Company(supporterUpdateRequest.company()));
        supporter.updateIntroduction(new Introduction(supporterUpdateRequest.introduction()));
        supporterTechnicalTagCommandRepository.deleteBySupporter(supporter);
        supporterUpdateRequest.technicalTags()
                .forEach(tagName -> createSupporterTechnicalTag(supporter, new TagName(tagName)));
    }

    private SupporterTechnicalTag createSupporterTechnicalTag(final Supporter supporter, final TagName tagName) {
        final TechnicalTag technicalTag = findTechnicalTagIfExistElseCreate(tagName);
        return supporterTechnicalTagCommandRepository.save(SupporterTechnicalTag.builder()
                .supporter(supporter)
                .technicalTag(technicalTag)
                .build()
        );
    }

    private TechnicalTag findTechnicalTagIfExistElseCreate(final TagName tagName) {
        final Optional<TechnicalTag> maybeTechnicalTag = technicalTagQueryRepository.findByTagName(tagName);
        return maybeTechnicalTag.orElseGet(() ->
                technicalTagQueryRepository.save(TechnicalTag.builder()
                        .tagName(tagName)
                        .build()
                ));
    }
}
