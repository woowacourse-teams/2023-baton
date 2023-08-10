import ConfirmModal from '@/components/ConfirmModal/ConfirmModal';
import TechLabel from '@/components/TechLabel/TechLabel';
import Avatar from '@/components/common/Avatar/Avatar';
import Button from '@/components/common/Button/Button';
import { BATON_BASE_URL } from '@/constants';
import { usePageRouter } from '@/hooks/usePageRouter';
import { useToken } from '@/hooks/useToken';
import { Candidate } from '@/types/supporterCandidate';
import React, { useState } from 'react';
import { useParams } from 'react-router-dom';
import { styled } from 'styled-components';

interface Props {
  supporter: Candidate;
}

const SupporterCardItem = ({ supporter }: Props) => {
  const { runnerPostId } = useParams();

  const { getToken } = useToken();
  const { goToMyPage } = usePageRouter();

  const [isModalOpen, setIsModalOpen] = useState<boolean>(false);

  const openModal = () => {
    setIsModalOpen(true);
  };

  const closeModal = () => {
    setIsModalOpen(false);
  };

  const selectSupporter = () => {
    const token = getToken()?.value;
    if (!token) throw new Error('토큰이 존재하지 않습니다');

    fetch(`${BATON_BASE_URL}/posts/runner/${runnerPostId}/supporters`, {
      method: 'PATCH',
      headers: { Authorization: `Bearer ${token}` },
      body: JSON.stringify({ supporterId: supporter.supporterId }),
    })
      .then(() => {
        alert('서포터 선택을 완료했습니다');
      })
      .catch((error) => {
        alert(error);
      })
      .finally(() => {
        goToMyPage();
      });
  };

  return (
    <S.SupporterCardItemContainer>
      <S.ProfileContainer>
        <Avatar
          imageUrl={supporter.imageUrl || 'https://api.iconify.design/material-symbols:account-circle.svg'}
          width={'80px'}
          height={'80px'}
        />
        <S.InfoContainer>
          <S.Name>{supporter.name}</S.Name>
          <S.Company>{supporter.company}</S.Company>
          <S.TechStackContainer>
            {supporter.technicalTags.map((tag) => (
              <TechLabel key={tag} tag={tag} />
            ))}
          </S.TechStackContainer>
        </S.InfoContainer>
        <S.ReviewCountContainer>
          완료된 리뷰
          <S.ReviewCount> {supporter.reviewCount}</S.ReviewCount>
        </S.ReviewCountContainer>
      </S.ProfileContainer>
      <S.MessageContainer>
        📮 남긴 메시지
        <S.Message> {supporter.message}</S.Message>
      </S.MessageContainer>
      <S.ButtonContainer>
        <Button colorTheme="BLACK" width="94px" height="35px" fontSize="14px" fontWeight={700}>
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

    height: 500px;

    padding: 30px 40px 40px 40px;
    border: 1px solid var(--gray-500);
    border-radius: 10px;
    box-shadow: 1px 4px 5px rgba(0, 0, 0, 0.2);
  `,

  ProfileContainer: styled.div`
    display: flex;
    flex-wrap: wrap column;
    gap: 20px;

    height: 100px;
  `,

  InfoContainer: styled.div`
    display: flex;
    flex-direction: column;
    gap: 10px;

    height: 100%;
  `,

  Name: styled.div`
    font-size: 20px;
    font-weight: 700;
  `,

  Company: styled.div`
    font-size: 18px;
  `,

  ReviewCountContainer: styled.div`
    margin-left: auto;
  `,

  ReviewCount: styled.span`
    font-weight: 700;
  `,

  TechStackContainer: styled.div`
    display: flex;
    gap: 4px;
  `,

  MessageContainer: styled.div`
    display: flex;
    flex-direction: column;
    gap: 15px;

    font-size: 18px;
  `,

  Message: styled.div`
    height: 220px;

    padding: 20px;
    border-radius: 5px;

    background-color: var(--gray-100);

    font-size: 16px;
    line-height: 26px;

    overflow: scroll;
  `,

  ButtonContainer: styled.div`
    display: flex;
    justify-content: center;
    gap: 30px;

    margin-top: auto;
  `,
};
