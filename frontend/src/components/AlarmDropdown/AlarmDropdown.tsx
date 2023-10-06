import React from 'react';
import styled from 'styled-components';
import { Alarm } from '@/types/alarm';
import { usePageRouter } from '@/hooks/usePageRouter';
import { useAlarm } from '@/hooks/query/useAlarm';
import { useLogin } from '@/hooks/useLogin';
import { useAlarmDelete } from '@/hooks/query/useAlarmDelete';
import { useAlarmCheck } from '@/hooks/query/useAlarmCheck';

const AlarmDropdown = () => {
  const { isLogin } = useLogin();
  const { data: alarmList } = useAlarm(isLogin);
  const { mutate: deleteAlarm } = useAlarmDelete();
  const { mutate: patchAlarmCheck } = useAlarmCheck();
  const { goToRunnerPostPage } = usePageRouter();

  const handlePostClick = (alarmId: number, runnerPostId: number) => {
    patchAlarmCheck(alarmId);
    goToRunnerPostPage(runnerPostId);
  };

  const handleDeleteAlarm = (e: React.MouseEvent, alarmId: number) => {
    e.stopPropagation();

    deleteAlarm(alarmId);
  };

  return (
    <S.DropdownContainer>
      {alarmList?.data.length > 0 ? (
        alarmList?.data?.map((alarm) => {
          return (
            <S.DropdownList key={alarm.alarmId} onClick={() => handlePostClick(alarm.alarmId, alarm.referencedId)}>
              <S.AlarmTitleContainer>
                <S.AlarmTitle>{alarm.title}</S.AlarmTitle>
                <S.CloseButton onClick={(e) => handleDeleteAlarm(e, alarm.alarmId)}>삭제</S.CloseButton>
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
