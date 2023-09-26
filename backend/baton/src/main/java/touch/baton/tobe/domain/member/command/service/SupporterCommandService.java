package touch.baton.tobe.domain.member.command.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import touch.baton.domain.common.vo.Introduction;
import touch.baton.domain.common.vo.TagName;
import touch.baton.domain.technicaltag.SupporterTechnicalTag;
import touch.baton.domain.technicaltag.TechnicalTag;
import touch.baton.domain.technicaltag.repository.SupporterTechnicalTagRepository;
import touch.baton.domain.technicaltag.repository.TechnicalTagRepository;
import touch.baton.tobe.domain.member.command.Supporter;
import touch.baton.tobe.domain.member.command.service.dto.SupporterUpdateRequest;
import touch.baton.tobe.domain.member.command.vo.Company;
import touch.baton.tobe.domain.member.command.vo.MemberName;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class SupporterCommandService {

    private final TechnicalTagRepository technicalTagRepository;
    private final SupporterTechnicalTagRepository supporterTechnicalTagRepository;

    public void updateSupporter(final Supporter supporter, final SupporterUpdateRequest supporterUpdateRequest) {
        supporter.updateMemberName(new MemberName(supporterUpdateRequest.name()));
        supporter.updateCompany(new Company(supporterUpdateRequest.company()));
        supporter.updateIntroduction(new Introduction(supporterUpdateRequest.introduction()));
        supporterTechnicalTagRepository.deleteBySupporter(supporter);
        supporterUpdateRequest.technicalTags()
                .forEach(tagName -> createSupporterTechnicalTag(supporter, new TagName(tagName)));
    }

    private SupporterTechnicalTag createSupporterTechnicalTag(final Supporter supporter, final TagName tagName) {
        final TechnicalTag technicalTag = findTechnicalTagIfExistElseCreate(tagName);
        return supporterTechnicalTagRepository.save(SupporterTechnicalTag.builder()
                .supporter(supporter)
                .technicalTag(technicalTag)
                .build()
        );
    }

    private TechnicalTag findTechnicalTagIfExistElseCreate(final TagName tagName) {
        final Optional<TechnicalTag> maybeTechnicalTag = technicalTagRepository.findByTagName(tagName);
        return maybeTechnicalTag.orElseGet(() ->
                technicalTagRepository.save(TechnicalTag.builder()
                        .tagName(tagName)
                        .build()
                ));
    }
}
