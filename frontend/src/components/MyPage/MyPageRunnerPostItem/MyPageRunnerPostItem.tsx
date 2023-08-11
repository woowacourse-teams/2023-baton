import Button from '@/components/common/Button/Button';
import Label from '@/components/common/Label/Label';
import { REVIEW_STATUS_LABEL_TEXT } from '@/constants';
import { usePageRouter } from '@/hooks/usePageRouter';
import { MyPageRunnerPost } from '@/types/myPage';
import React from 'react';
import styled from 'styled-components';
import eyeIcon from '@/assets/eye-icon.svg';
import chattingIcon from '@/assets/chatting-icon.svg';

interface Props extends MyPageRunnerPost {}

const MyPageRunnerPostItem = ({
  runnerPostId,
  title,
  deadline,
  reviewStatus,
  tags,
  watchedCount,
  applicantCount,
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
            <span key={index}>#{tag}</span>
          ))}
        </S.TagContainer>
      </S.LeftSideContainer>
      <S.RightSideContainer>
        <S.ChatViewContainer>
          <S.statisticsContainer>
            <S.statisticsImage src={eyeIcon} />
            <S.statisticsText>{watchedCount}</S.statisticsText>
            <S.statisticsImage src={chattingIcon} />
            <S.statisticsText>{applicantCount}</S.statisticsText>
          </S.statisticsContainer>
        </S.ChatViewContainer>
        {reviewStatus === 'NOT_STARTED' ? (
          <Button colorTheme="WHITE" fontWeight={700} width="180px" height="40px" onClick={handleClickSelectButton}>
            서포터 선택하기
          </Button>
        ) : null}
        {reviewStatus === 'IN_PROGRESS' ? (
          <Button colorTheme="WHITE" fontWeight={700} width="180px" height="40px" onClick={handleClickSelectButton}>
            코드 보러가기
          </Button>
        ) : null}
        {reviewStatus === 'DONE' ? (
          <Button colorTheme="WHITE" fontWeight={700} width="180px" height="40px" onClick={handleClickFeedbackButton}>
            후기 작성
          </Button>
        ) : null}
      </S.RightSideContainer>
    </S.RunnerPostItemContainer>
  );
};

export default MyPageRunnerPostItem;

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

    /* &:hover {
      transition: all 0.3s ease;
      transform: scale(1.015);
      outline: 1.5px solid var(--baton-red);
    } */
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
