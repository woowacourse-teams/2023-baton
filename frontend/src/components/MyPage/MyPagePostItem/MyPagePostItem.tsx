import Label from '@/components/common/Label/Label';
import { REVIEW_STATUS_LABEL_TEXT } from '@/constants';
import { usePageRouter } from '@/hooks/usePageRouter';
import React from 'react';
import styled from 'styled-components';
import eyeIcon from '@/assets/eye-icon.svg';
import applicantIcon from '@/assets/applicant-icon.svg';
import { MyPagePost } from '@/types/myPage';
import MyPagePostButton from '../MyPagePostButton/MyPagePostButton';
import useViewport from '@/hooks/useViewport';

interface Props extends MyPagePost {
  isRunner: boolean;
}

const MyPagePostItem = ({
  runnerPostId,
  title,
  deadline,
  reviewStatus,
  tags,
  watchedCount,
  applicantCount,
  isRunner,
  supporterId,
}: Props) => {
  const { goToRunnerPostPage } = usePageRouter();

  const { isMobile } = useViewport();

  const handlePostClick = () => {
    goToRunnerPostPage(runnerPostId);
  };

  return (
    <S.RunnerPostItemContainer onClick={handlePostClick}>
      <S.SideContainer>
        <S.PostTitle>{title}</S.PostTitle>
        <S.statisticsContainer>
          <S.statisticsImage alt="조회수" src={eyeIcon} />
          <S.statisticsText aria-label="조회수">{watchedCount}</S.statisticsText>
          <S.statisticsImage alt="지원한 서포터 수" src={applicantIcon} />
          <S.statisticsText aria-label="지원한 서포터 수">{applicantCount}</S.statisticsText>
        </S.statisticsContainer>
      </S.SideContainer>
      <S.DeadLineContainer>
        <S.DeadLine>{deadline.replace('T', ' ')} 까지</S.DeadLine>
        <Label
          height={isMobile ? '18px' : '22px'}
          colorTheme={reviewStatus === 'NOT_STARTED' ? 'WHITE' : reviewStatus === 'IN_PROGRESS' ? 'RED' : 'GRAY'}
          fontSize={isMobile ? '10px' : ''}
        >
          {REVIEW_STATUS_LABEL_TEXT[reviewStatus]}
        </Label>
      </S.DeadLineContainer>

      <S.BottomContainer>
        <S.TagContainer>
          {tags.map((tag, index) => (
            <S.Tag key={index}>#{tag}</S.Tag>
          ))}
        </S.TagContainer>

        <MyPagePostButton
          applicantCount={applicantCount}
          runnerPostId={runnerPostId}
          isRunner={isRunner}
          reviewStatus={reviewStatus}
          supporterId={supporterId}
        />
      </S.BottomContainer>
    </S.RunnerPostItemContainer>
  );
};

export default MyPagePostItem;

const S = {
  RunnerPostItemContainer: styled.li`
    display: flex;
    flex-direction: column;
    gap: 20px;

    min-width: 340px;
    width: 100%;
    padding: 35px 40px;

    border: 0.5px solid var(--gray-500);
    border-radius: 12px;
    box-shadow: 1px 2px 3px rgba(0, 0, 0, 0.2);

    cursor: pointer;

    @media (max-width: 768px) {
      padding: 25px 27px;
      gap: 12px;
    }

    & button:hover:enabled {
      transition: all 0.3s ease;
      background-color: var(--baton-red);
      color: var(--white);
    }
  `,

  SideContainer: styled.div`
    display: flex;
    justify-content: space-between;
    align-items: end;

    gap: 12px;
  `,

  PostTitle: styled.p`
    margin-bottom: 15px;

    font-size: 28px;
    font-weight: 700;

    @media (max-width: 768px) {
      margin-bottom: 0;

      font-size: 16px;
    }
  `,

  DeadLineContainer: styled.div`
    display: flex;
    align-items: baseline;
    gap: 10px;
  `,

  DeadLine: styled.p`
    margin-bottom: 40px;

    color: var(--gray-600);

    @media (max-width: 768px) {
      margin-bottom: 15px;

      font-size: 12px;
    }
  `,

  TagContainer: styled.div``,

  Tag: styled.span`
    margin-right: 10px;

    font-size: 14px;
    color: var(--gray-600);

    @media (max-width: 768px) {
      font-size: 14px;
    }
  `,

  BottomContainer: styled.div`
    display: flex;
    justify-content: space-between;
    align-items: end;

    @media (max-width: 768px) {
      flex-direction: column;
      align-items: start;
    }
  `,

  statisticsContainer: styled.div`
    display: flex;
    align-items: center;
    gap: 5px;

    margin-bottom: auto;

    & > p {
      color: #a4a4a4;
    }
  `,

  statisticsImage: styled.img`
    width: 20px;

    margin-left: 8px;

    @media (max-width: 768px) {
      width: 15px;
      margin-left: 4px;
    }
  `,

  statisticsText: styled.p`
    font-size: 14px;

    @media (max-width: 768px) {
      font-size: 12px;
    }
  `,
};
