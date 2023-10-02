import React from 'react';
import styled from 'styled-components';

const NotificationDropdown = () => {
  return (
    <S.DropdownContainer>
      <S.DropdownList>
        <S.NotificationTitle>서포터의 제안이 왔습니다.</S.NotificationTitle>
        <S.NotificationContents>관련 게시글 - 보스 잡기 미션 에이든 미션 제출합니다.</S.NotificationContents>
        <S.NotificationTime>2023-09-25 17:45</S.NotificationTime>
      </S.DropdownList>
      <S.DropdownList>
        <S.NotificationTitle>서포터의 제안이 왔습니다.</S.NotificationTitle>
        <S.NotificationContents>관련 게시글 - 보스 잡기 미션 에이든 미션 제출합니다.</S.NotificationContents>
        <S.NotificationTime>2023-09-25 17:45</S.NotificationTime>
      </S.DropdownList>
      <S.DropdownList>
        <S.NotificationTitle>서포터의 제안이 왔습니다.</S.NotificationTitle>
        <S.NotificationContents>관련 게시글 - 보스 잡기 미션 에이든 미션 제출합니다.</S.NotificationContents>
        <S.NotificationTime>2023-09-25 17:45</S.NotificationTime>
      </S.DropdownList>
    </S.DropdownContainer>
  );
};

export default NotificationDropdown;

const S = {
  DropdownContainer: styled.ul`
    width: 414px;
    height: max-content;

    & > li {
      border-bottom: 1px solid var(--gray-400);
    }

    & > li:last-child {
      border-bottom: none;
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
  `,

  NotificationContents: styled.p`
    font-size: 14px;
    color: var(--gray-700);
  `,

  NotificationTime: styled.p`
    font-size: 12px;
    color: var(--gray-700);

    text-align: end;
  `,
};
