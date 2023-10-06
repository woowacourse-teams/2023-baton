import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import mockData from '../../mocks/data/alarm.json';
import { Alarm, GetAlarmResponse } from '@/types/alarm';
import { usePageRouter } from '@/hooks/usePageRouter';

const AlarmDropdown = () => {
  const [AlarmList, setAlarmList] = useState<GetAlarmResponse | null>(null);

  const { goToRunnerPostPage } = usePageRouter();

  useEffect(() => {
    setAlarmList(mockData);
  }, []);

  const handleDeletePost = (e: React.MouseEvent, AlarmId: number) => {
    e.stopPropagation();

    const deletedPostList = AlarmList?.data.filter((alarm: Alarm) => alarm.alarmId !== AlarmId);
    if (deletedPostList) {
      setAlarmList({ data: deletedPostList });
    }
  };

  const handlePostClick = (runnerPostId: number) => {
    goToRunnerPostPage(runnerPostId);
  };

  return (
    <S.DropdownContainer>
      {AlarmList?.data && AlarmList?.data.length > 0 ? (
        AlarmList?.data.map((alarm: Alarm) => {
          return (
            <S.DropdownList key={alarm.alarmId} onClick={() => handlePostClick(alarm.referencedId)}>
              <S.AlarmTitleContainer>
                <S.AlarmTitle>{alarm.title}</S.AlarmTitle>
                <S.CloseButton onClick={(e) => handleDeletePost(e, alarm.alarmId)}>삭제</S.CloseButton>
              </S.AlarmTitleContainer>
              <S.AlarmContents>{alarm.message}</S.AlarmContents>
              <S.AlarmTime>{alarm.createdAt}</S.AlarmTime>
            </S.DropdownList>
          );
        })
      ) : (
        <S.EmptyMessage>새로운 알림이 없습니다.</S.EmptyMessage>
      )}
    </S.DropdownContainer>
  );
};

export default AlarmDropdown;

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

  AlarmTitleContainer: styled.div`
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

  AlarmTitle: styled.p`
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

  AlarmContents: styled.p`
    font-size: 14px;
    color: var(--gray-700);

    @media (max-width: 768px) {
      font-size: 12px;
    }
  `,

  AlarmTime: styled.p`
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
