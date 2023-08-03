import { usePageRouter } from '@/hooks/usePageRouter';
import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import LogoImage from '@/assets/logo-image.svg';
import { useLogin } from '@/hooks/useLogin';

const Header = () => {
  const { goToMainPage, goToLoginPage } = usePageRouter();
  const { getToken, removeToken } = useLogin();
  const [isLogin, setIsLogin] = useState(false);

  useEffect(() => {
    setIsLogin(!!getToken());
  }, []);

  const handleClickLogoutButton = () => {
    removeToken();
    setIsLogin(false);
  };

  return (
    <S.HeaderWrapper>
      <S.HeaderContainer>
        <S.Logo src={LogoImage} onClick={goToMainPage} />
        <S.MenuContainer>
          {isLogin ? (
            <>
              <S.ProfileAvatar />
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
  HeaderWrapper: styled.div`
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

  ProfileAvatar: styled.div`
    width: 50px;
    height: 50px;

    border-radius: 50%;

    background-color: #ababab;
  `,
};
