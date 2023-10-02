import React from 'react';
import styled from 'styled-components';
import MyPageIcon from '@/assets/my-page-icon.svg';
import LogoutIcon from '@/assets/logout-icon.svg';
import { useLogin } from '@/hooks/useLogin';
import { usePageRouter } from '@/hooks/usePageRouter';

const ProfileDropdown = () => {
  const { isLogin, logout } = useLogin();
  const { goToMainPage, goToRunnerMyPage, goToSupporterMyPage } = usePageRouter();

  const handleClickRunnerMyPage = () => {
    goToRunnerMyPage();
  };

  const handleClickSupporterMyPage = () => {
    goToSupporterMyPage();
  };

  const handleClickLogoutButton = () => {
    logout();

    window.location.reload();
  };

  return (
    <S.DropdownContainer>
      <S.DropdownList onClick={handleClickRunnerMyPage}>
        <img width="18px" height="18px" src={MyPageIcon} />
        <S.DropdownListTitle>러너 마이페이지</S.DropdownListTitle>
      </S.DropdownList>
      <S.DropdownList onClick={handleClickSupporterMyPage}>
        <img width="18px" height="18px" src={MyPageIcon} />
        <S.DropdownListTitle>서포터 마이페이지</S.DropdownListTitle>
      </S.DropdownList>
      <S.DropdownList onClick={handleClickLogoutButton}>
        <img width="18px" height="18px" src={LogoutIcon} />
        <S.DropdownListTitle>로그아웃</S.DropdownListTitle>
      </S.DropdownList>
    </S.DropdownContainer>
  );
};

export default ProfileDropdown;

const S = {
  DropdownContainer: styled.ul`
    width: max-content;
    height: max-content;

    & > li {
      border-bottom: 1px solid var(--gray-400);
    }

    & > li:last-child {
      border-bottom: none;
      border-radius: 0 0 10px 10px;
    }
  `,

  DropdownList: styled.li`
    display: flex;
    align-items: center;
    gap: 12px;

    padding: 15px 25px;

    cursor: pointer;

    &:hover {
      background-color: var(--gray-100);
    }
  `,

  DropdownListTitle: styled.p``,
};
