import ConfirmModal from '@/components/ConfirmModal/ConfirmModal';
import TechLabel from '@/components/TechLabel/TechLabel';
import Avatar from '@/components/common/Avatar/Avatar';
import Button from '@/components/common/Button/Button';
import { TOAST_COMPLETION_MESSAGE } from '@/constants/message';
import { ToastContext } from '@/contexts/ToastContext';
import { useFetch } from '@/hooks/useFetch';
import { usePageRouter } from '@/hooks/usePageRouter';
import useViewport from '@/hooks/useViewport';
import { Candidate } from '@/types/supporterCandidate';
import React, { useContext, useState } from 'react';
import { useParams } from 'react-router-dom';
import { styled } from 'styled-components';

interface Props {
  supporter: Candidate;
}

const SupporterCardItem = ({ supporter }: Props) => {
  const { runnerPostId } = useParams();

  const { goToMyPage, goToSupporterProfilePage } = usePageRouter();
  const { patchRequestWithAuth } = useFetch();

  const { showCompletionToast } = useContext(ToastContext);

  const [isModalOpen, setIsModalOpen] = useState<boolean>(false);

  const { isMobile } = useViewport();

  const openModal = () => {
    setIsModalOpen(true);
  };

  const closeModal = () => {
    setIsModalOpen(false);
  };

  const viewProfile = () => {
    goToSupporterProfilePage(supporter.supporterId);
  };

  const selectSupporter = () => {
    const body = JSON.stringify({ supporterId: supporter.supporterId });

    patchRequestWithAuth(
      `/posts/runner/${runnerPostId}/supporters`,
      async (response) => {
        showCompletionToast(TOAST_COMPLETION_MESSAGE.SUPPORTER_SELECT);

        goToMyPage();
      },
      body,
    );
  };

  return (
    <S.SupporterCardItemContainer>
      <S.ReviewCountContainer>
        완료된 리뷰
        <S.ReviewCount> {supporter.reviewCount}</S.ReviewCount>
      </S.ReviewCountContainer>
      <S.ProfileContainer>
        <Avatar imageUrl={supporter.imageUrl} width={isMobile ? '60px' : '80px'} height={isMobile ? '60px' : '80px'} />
        <S.InfoContainer>
          <S.Name>{supporter.name}</S.Name>
          <S.Company>{supporter.company}</S.Company>
          {!isMobile && (
            <S.TechStackContainer>
              {supporter.technicalTags.map((tag) => (
                <TechLabel key={tag} tag={tag} />
              ))}
            </S.TechStackContainer>
          )}
        </S.InfoContainer>
      </S.ProfileContainer>
      {isMobile && (
        <S.TechStackContainer>
          {supporter.technicalTags.map((tag) => (
            <TechLabel key={tag} tag={tag} />
          ))}
        </S.TechStackContainer>
      )}
      <S.MessageContainer>
        📮 남긴 메시지
        <S.Message> {supporter.message}</S.Message>
      </S.MessageContainer>
      <S.ButtonContainer>
        <Button colorTheme="BLACK" width="94px" height="35px" fontSize="14px" fontWeight={700} onClick={viewProfile}>
          프로필 보기
        </Button>
        <Button colorTheme="WHITE" width="94px" height="35px" fontSize="14px" fontWeight={700} onClick={openModal}>
          선택하기
        </Button>
      </S.ButtonContainer>
      {isModalOpen && (
        <ConfirmModal
          contents={`정말 ${supporter.name}님을 서포터로 선택하시겠습니까?`}
          closeModal={closeModal}
          handleClickConfirmButton={selectSupporter}
        />
      )}
    </S.SupporterCardItemContainer>
  );
};

export default SupporterCardItem;

const S = {
  SupporterCardItemContainer: styled.div`
    display: flex;
    flex-direction: column;

    padding: 30px 40px 40px 40px;
    border: 1px solid var(--gray-500);
    border-radius: 10px;
    box-shadow: 1px 4px 5px rgba(0, 0, 0, 0.2);

    @media (max-width: 768px) {
      height: 100%;
      padding: 30px 23px;
    }
  `,

  ProfileContainer: styled.div`
    display: flex;
    flex-wrap: wrap column;
    gap: 20px;

    height: 120px;
    margin-bottom: 30px;

    @media (max-width: 768px) {
      height: 100%;
      margin-bottom: 15px;
    }
  `,

  InfoContainer: styled.div`
    display: flex;
    flex-direction: column;
    justify-content: center;
    gap: 10px;
  `,

  Name: styled.div`
    font-size: 20px;
    font-weight: 700;

    @media (max-width: 768px) {
      font-size: 16px;
    }
  `,

  Company: styled.div`
    font-size: 18px;

    @media (max-width: 768px) {
      font-size: 14px;
    }
  `,

  ReviewCountContainer: styled.div`
    margin-left: auto;
    margin-bottom: 20px;

    @media (max-width: 768px) {
      margin-bottom: 15px;

      font-size: 14px;
    }
  `,

  ReviewCount: styled.span`
    font-weight: 700;
  `,

  TechStackContainer: styled.div`
    display: flex;
    flex-wrap: wrap;
    gap: 4px;

    @media (max-width: 768px) {
      margin-bottom: 45px;
    }
  `,

  MessageContainer: styled.div`
    display: flex;
    flex-direction: column;
    gap: 15px;

    font-size: 18px;
    margin-bottom: 35px;

    @media (max-width: 768px) {
      margin-bottom: 30px;

      font-size: 14px;
    }
  `,

  Message: styled.div`
    height: 220px;

    padding: 20px;
    border-radius: 5px;

    background-color: var(--gray-100);

    font-size: 16px;
    line-height: 26px;

    overflow-y: scroll;

    &::-webkit-scrollbar {
      display: none;
    }

    @media (max-width: 768px) {
      font-size: 14px;
    }
  `,

  ButtonContainer: styled.div`
    display: flex;
    justify-content: center;
    gap: 30px;

    margin-top: auto;
  `,
};
