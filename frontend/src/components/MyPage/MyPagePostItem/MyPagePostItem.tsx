import Button from '@/components/common/Button/Button';
import Label from '@/components/common/Label/Label';
import { REVIEW_STATUS_LABEL_TEXT } from '@/constants';
import { usePageRouter } from '@/hooks/usePageRouter';
import React from 'react';
import styled from 'styled-components';
import eyeIcon from '@/assets/eye-icon.svg';
import applicantIcon from '@/assets/applicant-icon.svg';
import { MyPagePost } from '@/types/myPage';

interface Props extends MyPagePost {
  isRunner?: boolean;
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
}: Props) => {
  const handleClickSelectButton = (e: React.MouseEvent<HTMLButtonElement>) => {
    e.stopPropagation();
    alert('준비중인 서포터 선택 기능입니다');
  };

  const handleClickFeedbackButton = (e: React.MouseEvent<HTMLButtonElement>) => {
    e.stopPropagation();
    alert('준비중인 후기 작성 기능입니다');
  };

  const { goToRunnerPostPage } = usePageRouter();

  const handlePostClick = () => {
    goToRunnerPostPage(runnerPostId);
  };

  return (
    <S.RunnerPostItemContainer onClick={handlePostClick}>
      <S.LeftSideContainer>
        <S.PostTitle>{title}</S.PostTitle>
        <S.DeadLineContainer>
          <S.DeadLine>{deadline} 까지</S.DeadLine>
          <Label colorTheme={reviewStatus === 'DONE' ? 'GRAY' : reviewStatus === 'IN_PROGRESS' ? 'RED' : 'WHITE'}>
            {REVIEW_STATUS_LABEL_TEXT[reviewStatus]}
          </Label>
        </S.DeadLineContainer>
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
        {isRunner ? (
          reviewStatus === 'NOT_STARTED' ? (
            <Button colorTheme="WHITE" fontWeight={700} width="180px" height="40px" onClick={handleClickSelectButton}>
              서포터 선택하기
            </Button>
          ) : reviewStatus === 'IN_PROGRESS' ? (
            <Button colorTheme="WHITE" fontWeight={700} width="180px" height="40px" onClick={handleClickSelectButton}>
              코드 보러가기
            </Button>
          ) : (
            <Button colorTheme="WHITE" fontWeight={700} width="180px" height="40px" onClick={handleClickFeedbackButton}>
              서포터 후기 작성
            </Button>
          )
        ) : reviewStatus === 'NOT_STARTED' ? (
          <Button colorTheme="WHITE" fontWeight={700} width="180px" height="40px" onClick={handleClickSelectButton}>
            제안 취소하기
          </Button>
        ) : reviewStatus === 'IN_PROGRESS' ? (
          <Button colorTheme="WHITE" fontWeight={700} width="180px" height="40px" onClick={handleClickSelectButton}>
            코드 보러가기
          </Button>
        ) : (
          <Button colorTheme="WHITE" fontWeight={700} width="180px" height="40px" onClick={handleClickFeedbackButton}>
            러너 후기 작성
          </Button>
        )}
      </S.RightSideContainer>
    </S.RunnerPostItemContainer>
  );
};

export default MyPagePostItem;

const S = {
  RunnerPostItemContainer: styled.li`
    display: flex;
    justify-content: space-between;

    width: 1200px;
    height: 206px;
    padding: 35px 40px;

    border: 0.5px solid var(--gray-500);
    border-radius: 12px;
    box-shadow: 1px 2px 3px rgba(0, 0, 0, 0.2);

    cursor: pointer;
  `,

  PostTitle: styled.p`
    margin-bottom: 15px;

    font-size: 28px;
    font-weight: 700;
  `,

  DeadLineContainer: styled.div`
    display: flex;
    align-items: baseline;
    gap: 10px;
  `,

  DeadLine: styled.p`
    margin-bottom: 60px;

    color: var(--gray-600);
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