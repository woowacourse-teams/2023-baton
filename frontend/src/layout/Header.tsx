import { usePageRouter } from '@/hooks/usePageRouter';
import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import LogoImage from '@/assets/logo-image.svg';
import { useToken } from '@/hooks/useToken';
import { GetHeaderProfileResponse } from '@/types/profile';
import Avatar from '@/components/common/Avatar/Avatar';
import { getRequest } from '@/api/fetch';

const Header = () => {
  const [profile, setProfile] = useState<GetHeaderProfileResponse | null>(null);

  const { goToMainPage, goToLoginPage, goToMyPage } = usePageRouter();
  const { getToken, removeToken } = useToken();
  const [isLogin, setIsLogin] = useState(false);

  useEffect(() => {
    setIsLogin(!!getToken());

    if (isLogin) getProfile();
  }, [isLogin]);

  const getProfile = () => {
    const token = getToken()?.value;
    if (!token) throw new Error('토큰이 존재하지 않습니다');

    getRequest(`/profile/me`, `Bearer ${token}`).then(async (response) => {
      const data: GetHeaderProfileResponse = await response.json();

      setProfile(data);
    });
  };

  const handleClickLogoutButton = () => {
    removeToken();
    setIsLogin(false);

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
          {isLogin ? (
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
