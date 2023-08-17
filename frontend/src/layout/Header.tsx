import { usePageRouter } from '@/hooks/usePageRouter';
import React, { useContext, useEffect, useState } from 'react';
import styled from 'styled-components';
import LogoImage from '@/assets/logo-image.svg';
import { useToken } from '@/hooks/useToken';
import { GetHeaderProfileResponse } from '@/types/profile';
import Avatar from '@/components/common/Avatar/Avatar';
import { getRequest } from '@/api/fetch';
import { ToastContext } from '@/contexts/ToastContext';
import { ERROR_DESCRIPTION, ERROR_TITLE } from '@/constants/message';

const Header = () => {
  const [profile, setProfile] = useState<GetHeaderProfileResponse | null>(null);

  const { goToMainPage, goToLoginPage, goToMyPage } = usePageRouter();

  const { getToken, removeToken, hasToken } = useToken();

  const { showErrorToast } = useContext(ToastContext);

  useEffect(() => {
    const isLogin = hasToken();

    if (isLogin) getProfile();
  }, []);

  const getProfile = () => {
    const token = getToken()?.value;
    if (!token) return;

    getRequest(`/profile/me`, token)
      .then(async (response) => {
        const data: GetHeaderProfileResponse = await response.json();

        setProfile(data);
      })
      .catch((error: Error) =>
        showErrorToast({
          description: error instanceof Error ? error.message : ERROR_DESCRIPTION.UNEXPECTED,
          title: ERROR_TITLE.REQUEST,
        }),
      );
  };

  const handleClickLogoutButton = () => {
    removeToken();

    goToMainPage();
  };

  const handleClickProfile = () => {
    goToMyPage();
  };

  return (
    <S.HeaderWrapper>
      <S.HeaderContainer>
        <S.Logo src={LogoImage} onClick={goToMainPage} alt="바톤로고" />
        <S.MenuContainer>
          {hasToken() ? (
            <>
              <S.AvatarContainer onClick={handleClickProfile}>
                <Avatar width="35px" height="35px" imageUrl={profile?.imageUrl || 'https://via.placeholder.com/150'} />
                <p>{profile?.name}</p>
              </S.AvatarContainer>
              <S.LoginButton onClick={handleClickLogoutButton}>로그아웃</S.LoginButton>
            </>
          ) : (
            <S.LoginButton onClick={goToLoginPage}>로그인</S.LoginButton>
          )}
        </S.MenuContainer>
      </S.HeaderContainer>
    </S.HeaderWrapper>
  );
};

export default Header;

const S = {
  HeaderWrapper: styled.header`
    display: flex;
    justify-content: center;

    width: 100%;
    padding: 0 30px;

    border-bottom: 0.3px solid #333333;
  `,

  HeaderContainer: styled.div`
    display: flex;
    justify-content: space-between;
    align-items: center;

    width: 1200px;
    height: 80px;
  `,

  AvatarContainer: styled.div`
    display: flex;
    align-items: center;
    gap: 10px;

    cursor: pointer;
  `,

  Logo: styled.img`
    width: 140px;
    height: 40px;

    cursor: pointer;
  `,

  MenuContainer: styled.div`
    display: flex;
    align-items: center;
    gap: 30px;
  `,

  LoginButton: styled.button`
    width: 76px;
    height: 35px;

    border-radius: 50px;

    background-color: var(--baton-red);
    color: var(--white-color);
    font-size: 14px;
  `,
};
