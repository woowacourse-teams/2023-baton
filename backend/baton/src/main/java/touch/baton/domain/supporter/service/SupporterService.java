package touch.baton.domain.supporter.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import touch.baton.domain.common.vo.Introduction;
import touch.baton.domain.common.vo.TagName;
import touch.baton.tobe.domain.member.command.vo.Company;
import touch.baton.tobe.domain.member.command.vo.MemberName;
import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.supporter.exception.SupporterBusinessException;
import touch.baton.domain.supporter.repository.SupporterRepository;
import touch.baton.domain.supporter.service.dto.SupporterUpdateRequest;
import touch.baton.domain.technicaltag.SupporterTechnicalTag;
import touch.baton.domain.technicaltag.TechnicalTag;
import touch.baton.domain.technicaltag.repository.SupporterTechnicalTagRepository;
import touch.baton.domain.technicaltag.repository.TechnicalTagRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SupporterService {

    private final SupporterRepository supporterRepository;
    private final TechnicalTagRepository technicalTagRepository;
    private final SupporterTechnicalTagRepository supporterTechnicalTagRepository;

    public List<Supporter> readAllSupporters() {
        return supporterRepository.findAll();
    }

    public Supporter readBySupporterId(final Long supporterId) {
        return supporterRepository.joinMemberBySupporterId(supporterId)
                .orElseThrow(() -> new SupporterBusinessException("존재하지 않는 서포터 식별자값으로 조회할 수 없습니다."));
    }

    @Transactional
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
