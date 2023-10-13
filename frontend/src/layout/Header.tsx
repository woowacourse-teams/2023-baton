import { usePageRouter } from '@/hooks/usePageRouter';
import React from 'react';
import styled from 'styled-components';
import LogoImage from '@/assets/logo-image.svg';
import LogoImageMobile from '@/assets/logo-image-mobile.svg';
import Button from '@/components/common/Button/Button';
import { isLogin } from '@/apis/auth';
import MyMenu from './MyMenu';

const Header = () => {
  const { goToMainPage, goToLoginPage } = usePageRouter();

  return (
    <>
      <S.HeaderWrapper>
        <S.HeaderContainer>
          <S.Logo onClick={goToMainPage} />
          {isLogin() ? (
            <MyMenu />
          ) : (
            <Button fontSize="14px" width="76px" height="35px" colorTheme="RED" onClick={goToLoginPage}>
              로그인
            </Button>
          )}
        </S.HeaderContainer>
      </S.HeaderWrapper>
    </>
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

    max-width: 1200px;
    width: 100%;
    height: 80px;
  `,

  NotificationContainer: styled.div``,

  NotificationIcon: styled.img`
    width: 25px;
    height: 25px;

    cursor: pointer;
  `,

  AvatarContainer: styled.div`
    cursor: pointer;
  `,

  Logo: styled.div`
    width: 197px;
    height: 35px;

    background-image: url(${LogoImage});
    background-size: cover;
    background-repeat: no-repeat;

    cursor: pointer;

    @media (max-width: 768px) {
      background-image: url(${LogoImageMobile});

      width: 53px;
      height: 30px;
    }
  `,

  MenuContainer: styled.div`
    display: flex;
    align-items: center;
    gap: 30px;

    @media (max-width: 768px) {
      gap: 16px;
    }
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
