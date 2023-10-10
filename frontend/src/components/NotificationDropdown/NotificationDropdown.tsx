import React from 'react';
import styled from 'styled-components';
import { Notification } from '@/types/notification';
import { usePageRouter } from '@/hooks/usePageRouter';
import { useNotificationDelete } from '@/hooks/query/useNotificationDelete';
import { useNotificationCheck } from '@/hooks/query/useNotificationCheck';

interface Props {
  notificationList: Notification[];
}

const NotificationDropdown = ({ notificationList }: Props) => {
  const { mutate: deleteNotification } = useNotificationDelete();
  const { mutate: patchNotificationCheck } = useNotificationCheck();
  const { goToRunnerPostPage } = usePageRouter();

  const handlePostClick = (notificationId: number, runnerPostId: number) => {
    patchNotificationCheck(notificationId);
    goToRunnerPostPage(runnerPostId);
  };

  const handleDeleteNotification = (e: React.MouseEvent, notificationId: number) => {
    e.stopPropagation();

    deleteNotification(notificationId);
  };

  return (
    <S.DropdownContainer>
      {notificationList?.length > 0 ? (
        notificationList?.map((notification) => {
          return (
            <S.DropdownList
              key={notification.notificationId}
              onClick={() => handlePostClick(notification.notificationId, notification.referencedId)}
            >
              <S.NotificationTitleContainer>
                <S.NotificationTitle isRead={notification.isRead}>{notification.title}</S.NotificationTitle>
                <S.CloseButton onClick={(e) => handleDeleteNotification(e, notification.notificationId)}>
                  삭제
                </S.CloseButton>
              </S.NotificationTitleContainer>
              <S.NotificationContents>{notification.message}</S.NotificationContents>
              <S.NotificationTime>{notification.createdAt}</S.NotificationTime>
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
    max-height: 427px;

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

  NotificationTitle: styled.p<{ isRead: boolean }>`
    font-size: 16px;
    font-weight: 700;
    position: relative;

    ${({ isRead }) =>
      isRead
        ? () => ''
        : ` &::before {
      width: 4px;
      height: 4px;
      content: '';
      position: absolute;
      left: -10px;
      top: 50%;
      transform: translateY(-50%);

      background-color: var(--baton-red);
      border-radius: 50%;
    }`};

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
