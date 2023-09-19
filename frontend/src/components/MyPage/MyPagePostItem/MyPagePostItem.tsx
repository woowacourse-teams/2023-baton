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
      {isMobile && (
        <>
          <S.PostTitle>{title}</S.PostTitle>
          <S.DeadLineContainer>
            <S.DeadLine>{deadline.replace('T', ' ')} 까지</S.DeadLine>
            <Label
              colorTheme={reviewStatus === 'NOT_STARTED' ? 'WHITE' : reviewStatus === 'IN_PROGRESS' ? 'RED' : 'GRAY'}
              fontSize={isMobile ? '10px' : ''}
            >
              {REVIEW_STATUS_LABEL_TEXT[reviewStatus]}
            </Label>
          </S.DeadLineContainer>
        </>
      )}
      <S.SideContainer>
        <S.LeftSideContainer>
          {!isMobile && (
            <>
              <S.PostTitle>{title}</S.PostTitle>
              <S.DeadLineContainer>
                <S.DeadLine>{deadline.replace('T', ' ')} 까지</S.DeadLine>
                <Label
                  colorTheme={
                    reviewStatus === 'NOT_STARTED' ? 'WHITE' : reviewStatus === 'IN_PROGRESS' ? 'RED' : 'GRAY'
                  }
                  fontSize={isMobile ? '10px' : ''}
                >
                  {REVIEW_STATUS_LABEL_TEXT[reviewStatus]}
                </Label>
              </S.DeadLineContainer>
            </>
          )}
          <S.TagContainer>
            {tags.map((tag, index) => (
              <S.Tag key={index}>#{tag}</S.Tag>
            ))}
          </S.TagContainer>
        </S.LeftSideContainer>
        <S.RightSideContainer>
          <S.ChatViewContainer>
            <S.statisticsContainer>
              <S.statisticsImage src={eyeIcon} />
              <S.statisticsText>{watchedCount}</S.statisticsText>
              <S.statisticsImage src={applicantIcon} />
              <S.statisticsText>{applicantCount}</S.statisticsText>
            </S.statisticsContainer>
          </S.ChatViewContainer>
          {!isMobile && (
            <S.PostButtonWrapper>
              <MyPagePostButton
                runnerPostId={runnerPostId}
                isRunner={isRunner}
                reviewStatus={reviewStatus}
                supporterId={supporterId}
              />
            </S.PostButtonWrapper>
          )}
        </S.RightSideContainer>
      </S.SideContainer>
      {isMobile && (
        <MyPagePostButton
          runnerPostId={runnerPostId}
          isRunner={isRunner}
          reviewStatus={reviewStatus}
          supporterId={supporterId}
        />
      )}
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

    & button:hover {
      transition: all 0.3s ease;
      background-color: var(--baton-red);
      color: var(--white-color);
    }
  `,

  SideContainer: styled.div`
    display: flex;
    justify-content: space-between;
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
    margin-bottom: 60px;

    color: var(--gray-600);

    @media (max-width: 768px) {
      margin-bottom: 15px;

      font-size: 12px;
    }
  `,

  TagContainer: styled.div`
    & span {
      margin-right: 10px;

      font-size: 14px;
      color: var(--gray-600);
    }
  `,
  Tag: styled.span``,

  LeftSideContainer: styled.div``,

  RightSideContainer: styled.div`
    display: flex;
    flex-direction: column;
    justify-content: end;
    gap: 15px;

    & button:hover {
      transition: all 0.3s ease;
      background-color: var(--baton-red);
      color: var(--white-color);
    }
  `,

  PostButtonWrapper: styled.div`
    padding-left: auto;
    width: 140px;
  `,

  ChatViewContainer: styled.div`
    display: flex;
    justify-content: end;

    gap: 10px;

    font-size: 12px;
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
  `,

  statisticsText: styled.p`
    font-size: 14px;
  `,
};
