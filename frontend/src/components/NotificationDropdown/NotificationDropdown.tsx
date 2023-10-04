import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import mockData from '../../mocks/data/alarm.json';
import { Alarm, GetAlarmResponse } from '@/types/alarm';
import { usePageRouter } from '@/hooks/usePageRouter';

const NotificationDropdown = () => {
  const [notificationList, setNotificationList] = useState<GetAlarmResponse | null>(null);

  const { goToRunnerPostPage } = usePageRouter();

  useEffect(() => {
    setNotificationList(mockData);
  }, []);

  const handleDeletePost = (e: React.MouseEvent, notificationId: number) => {
    e.stopPropagation();
    const deletedPostList = notificationList?.data.filter((noti: Alarm) => noti.alarmId !== notificationId);

    if (deletedPostList) {
      setNotificationList({ data: deletedPostList });
    }
  };

  const handlePostClick = (url: string) => {
    const runnerPostId = url.slice(-1);

    goToRunnerPostPage(parseInt(runnerPostId, 10));
  };

  return (
    <S.DropdownContainer>
      {notificationList?.data && notificationList?.data.length > 0 ? (
        notificationList?.data.map((noti: Alarm) => {
          return (
            <S.DropdownList key={noti.alarmId} onClick={() => handlePostClick(noti.url)}>
              <S.NotificationTitleContainer>
                <S.NotificationTitle>{noti.title}</S.NotificationTitle>
                <S.CloseButton onClick={(e) => handleDeletePost(e, noti.alarmId)}>삭제</S.CloseButton>
              </S.NotificationTitleContainer>
              <S.NotificationContents>{noti.message}</S.NotificationContents>
              <S.NotificationTime>{noti.createdAt}</S.NotificationTime>
            </S.DropdownList>
          );
        })
      ) : (
        <S.EmptyMessage>새로운 알림이 없습니다.</S.EmptyMessage>
      )}
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

  EmptyMessage: styled.p`
    height: 427px;

    display: flex;
    justify-content: center;
    align-items: center;
  `,
};
