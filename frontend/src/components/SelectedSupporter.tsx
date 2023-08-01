import Avatar from '@/components/common/Avatar';
import Button from '@/components/common/Button';
import { SupporterCard } from '@/types/supporterCard';
import React from 'react';
import styled from 'styled-components';
import githubIcon from '@/assets/github-icon.svg';

interface Props extends SupporterCard {}

const SelectedSupporter = ({
  supporterId,
  name,
  company,
  reviewCount,
  totalRating,
  githubUrl,
  introduction,
}: Props) => {
  return (
    <S.SelectedSupporterContainer>
      <Avatar width="60px" height="60px" imageUrl={'https://via.placeholder.com/150'} />
      <S.DescriptionContainer>
        <S.Name>{name}</S.Name>
        <S.Company>{company}</S.Company>
        <S.CompletedReviewContainer>
          <S.CompletedReview>완료한 리뷰</S.CompletedReview>
          <S.CompletedReviewCount>{reviewCount}</S.CompletedReviewCount>
        </S.CompletedReviewContainer>
      </S.DescriptionContainer>
    </S.SelectedSupporterContainer>
  );
};

export default SelectedSupporter;

const S = {
  SelectedSupporterContainer: styled.li`
    display: flex;
    align-items: center;
    gap: 20px;

    width: 300px;
    height: 105px;
    padding: 35px 40px;

    border: 0.5px solid var(--gray-500);
    border-radius: 12px;
    box-shadow: 1px 2px 3px rgba(0, 0, 0, 0.2);
  `,

  DescriptionContainer: styled.div`
    display: flex;
    flex-direction: column;
    gap: 5px;

    width: 300px;
  `,

  Name: styled.div`
    font-size: 18px;
  `,

  Company: styled.div`
    font-size: 14px;

    white-space: no-wrap;
    overflow: hidden;
    text-overflow: ellipsis;
  `,

  CompletedReviewContainer: styled.div`
    display: flex;
    align-items: center;
    gap: 7px;

    &:nth-child(3) {
      margin-top: 10px;
    }
  `,

  CompletedReview: styled.p`
    font-size: 14px;
  `,

  CompletedReviewCount: styled.p`
    font-size: 20px;
    font-weight: 700;
  `,
};
