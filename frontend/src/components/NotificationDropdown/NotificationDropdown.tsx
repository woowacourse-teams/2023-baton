import React from 'react';
import styled from 'styled-components';

const mockData = [
  {
    id: 1,
    title: '서포터의 제안이 왔습니다.',
    contents: '관련 게시글 - 보스 잡기 미션 에이든 미션 제출합니다.',
    time: '2023-09-25 17:45',
  },
  {
    id: 2,
    title: '코드 리뷰 매칭이 완료되었습니다.',
    contents: '관련 게시글 - 보스 잡기 미션 에이든 미션 제출합니다.',
    time: '2023-09-25 17:45',
  },
  {
    id: 3,
    title: '서포터의 제안이 왔습니다.',
    contents: '관련 게시글 - 보스 잡기 미션 에이든 미션 제출합니다.',
    time: '2023-09-25 17:45',
  },
  {
    id: 4,
    title: '서포터의 제안이 왔습니다.',
    contents: '관련 게시글 - 보스 잡기 미션 에이든 미션 제출합니다.',
    time: '2023-09-25 17:45',
  },
  {
    id: 6,
    title: '서포터의 제안이 왔습니다.',
    contents: '관련 게시글 - 보스 잡기 미션 에이든 미션 제출합니다.',
    time: '2023-09-25 17:45',
  },
];

const NotificationDropdown = () => {
  return (
    <S.DropdownContainer>
      {mockData.map((noti) => {
        return (
          <S.DropdownList key={noti.id}>
            <S.NotificationTitleContainer>
              <S.NotificationTitle>{noti.title}</S.NotificationTitle>
              <S.CloseButton>삭제</S.CloseButton>
            </S.NotificationTitleContainer>
            <S.NotificationContents>{noti.contents}</S.NotificationContents>
            <S.NotificationTime>{noti.time}</S.NotificationTime>
          </S.DropdownList>
        );
      })}
    </S.DropdownContainer>
  );
};

export default NotificationDropdown;

const S = {
  DropdownContainer: styled.ul`
    width: 414px;
    height: 427px;

    overflow-y: scroll;

    & > li {
      border-bottom: 1px solid var(--gray-400);
    }

    & > li:last-child {
      border-bottom: none;
      border-radius: 0 0 10px 10px;
    }

    @media (max-width: 768px) {
      width: 290px;
    }
  `,

  DropdownList: styled.li`
    display: flex;
    flex-direction: column;
    gap: 12px;
    padding: 20px 35px;

    cursor: pointer;

    &:hover {
      background-color: var(--gray-100);
    }
  `,

  NotificationTitleContainer: styled.div`
    display: flex;
    justify-content: space-between;
  `,

  CloseButton: styled.button`
    font-size: 12px;

    &:hover {
      color: var(--baton-red);
    }

    @media (max-width: 768px) {
      font-size: 10px;
    }
  `,

  NotificationTitle: styled.p`
    font-size: 16px;
    font-weight: 700;
    position: relative;

    &::before {
      content: '';
      position: absolute;
      width: 4px;
      height: 4px;
      left: -10px;
      top: 50%;
      transform: translateY(-50%);

      background-color: var(--baton-red);
      border-radius: 50%;
    }

    @media (max-width: 768px) {
      font-size: 14px;
    }
  `,

  NotificationContents: styled.p`
    font-size: 14px;
    color: var(--gray-700);

    @media (max-width: 768px) {
      font-size: 12px;
    }
  `,

  NotificationTime: styled.p`
    font-size: 12px;
    color: var(--gray-700);

    text-align: end;

    @media (max-width: 768px) {
      font-size: 10px;
    }
  `,
};
