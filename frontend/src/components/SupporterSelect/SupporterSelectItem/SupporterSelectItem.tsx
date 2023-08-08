import Avatar from '@/components/common/Avatar/Avatar';
import Button from '@/components/common/Button/Button';
import { SupporterCard } from '@/types/supporterCard';
import React from 'react';
import styled from 'styled-components';
import githubIcon from '@/assets/github-icon.svg';
import TechLabel from '@/components/TechLabel/TechLabel';

interface Props extends SupporterCard {
  selectedSupporter: (selectedSupporter: SupporterCard) => void;
}

const SupporterSelectItem = ({
  supporterId,
  name,
  company,
  reviewCount,
  totalRating,
  githubUrl,
  introduction,
  technicalTags,
  selectedSupporter,
}: Props) => {
  const handleSelectedSupporter = (e: React.MouseEvent<HTMLButtonElement>) => {
    e.stopPropagation();

    selectedSupporter({
      supporterId,
      name,
      company,
      reviewCount,
      totalRating,
      githubUrl,
      introduction,
      technicalTags,
    });
  };

  return (
    <S.SupporterSelectItemContainer tabIndex={0} aria-label="서포터 정보">
      <S.LeftSideContainer>
        <Avatar width="60px" height="60px" imageUrl={'https://via.placeholder.com/150'} />
        <S.DescriptionContainer>
          <S.Name tabIndex={0}>{name}</S.Name>
          <S.Company tabIndex={0}>{company}</S.Company>
          <S.TechStackContainer>
            {technicalTags.map((tag) => (
              <TechLabel key={tag} tag={tag} />
            ))}
          </S.TechStackContainer>
          <S.CompletedReviewContainer>
            <S.CompletedReview tabIndex={0}>완료한 리뷰</S.CompletedReview>
            <S.CompletedReviewCount tabIndex={0}>{reviewCount}</S.CompletedReviewCount>
          </S.CompletedReviewContainer>
        </S.DescriptionContainer>
      </S.LeftSideContainer>
      <S.ButtonContainer>
        <S.GithubButton href={githubUrl} target="_blank" aria-label={`${name} github 바로가기`}>
          <S.GithubIcon src={githubIcon} />
          <S.GithubButtonText>github</S.GithubButtonText>
        </S.GithubButton>
        <Button
          colorTheme="WHITE"
          width="94px"
          height="35px"
          fontSize="12px"
          fontWeight={700}
          onClick={handleSelectedSupporter}
          ariaLabel={`${name} 선택하기`}
        >
          선택하기
        </Button>
      </S.ButtonContainer>
    </S.SupporterSelectItemContainer>
  );
};

export default SupporterSelectItem;

const S = {
  SupporterSelectItemContainer: styled.li`
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 20px;

    width: 696px;
    height: 175px;
    padding: 35px 40px;

    border: 0.5px solid var(--gray-500);
    border-radius: 12px;
    box-shadow: 1px 2px 3px rgba(0, 0, 0, 0.2);
  `,
  LeftSideContainer: styled.div`
    display: flex;
    align-items: center;
    gap: 30px;
  `,

  DescriptionContainer: styled.div`
    display: flex;
    flex-direction: column;
    gap: 5px;

    width: 400px;
  `,

  Name: styled.div`
    font-size: 18px;
  `,

  Company: styled.div`
    margin-bottom: 4px;

    font-size: 14px;

    white-space: no-wrap;
    overflow: hidden;
    text-overflow: ellipsis;
  `,
  TechStackContainer: styled.div`
    display: flex;
    gap: 4px;
  `,
  CompletedReviewContainer: styled.div`
    display: flex;
    align-items: center;
    gap: 7px;

    margin-top: 14px;

    &:nth-child(3) {
      margin-top: 10px;
    }
  `,

  CompletedReview: styled.p`
    font-size: 14px;
  `,

  CompletedReviewCount: styled.p`
    font-size: 14px;
    font-weight: 700;
  `,

  ButtonContainer: styled.div`
    display: flex;
    flex-direction: column;
    gap: 12px;
  `,

  GithubButton: styled.a`
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 6px;

    width: 94px;
    height: 35px;
    border: 1px solid var(--gray-800);
    border-radius: 5px;

    background-color: transparent;

    font-weight: 700;
    font-size: 14px;

    cursor: pointer;
  `,

  GithubIcon: styled.img``,

  GithubButtonText: styled.div``,
};
