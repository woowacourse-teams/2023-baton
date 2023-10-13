import React from 'react';
import { styled } from 'styled-components';
import LogoImage from '@/assets/logo-image.svg';
import GithubIcon from '@/assets/github-icon.svg';
import { BATON_BASE_URL } from '@/constants';
import { usePageRouter } from '@/hooks/usePageRouter';

const LoginPage = () => {
  const { goToMainPage } = usePageRouter();

  const handleLoginButton = () => {
    window.location.href =
      process.env.NODE_ENV === 'development' ? `/oauth/github/callback?code=dev` : `${BATON_BASE_URL}/oauth/github`;
  };

  return (
    <S.LoginContainer>
      <S.Logo src={LogoImage} onClick={goToMainPage} />
      <S.LoginBoxContainer>
        <S.LoginBoxText>코드 리뷰를 위해 github로 로그인 해주세요</S.LoginBoxText>
        <S.LoginBoxGithubIcon src={GithubIcon} />
        <S.LoginButton onClick={handleLoginButton}>
          <S.LoginButtonIcon src={GithubIcon} />
          <S.LoginButtonText>Sign in with github</S.LoginButtonText>
        </S.LoginButton>
      </S.LoginBoxContainer>
    </S.LoginContainer>
  );
};

export default LoginPage;

const S = {
  LoginContainer: styled.div`
    display: flex;
    align-items: center;
    justify-content: center;
    flex-direction: column;
    gap: 45px;

    height: calc(100vh - 80px);
    min-height: 570px;

    overflow-y: hidden;
  `,

  Logo: styled.img`
    height: 45px;

    cursor: pointer;

    @media (max-width: 768px) {
      height: 35px;
    }
  `,

  LoginBoxContainer: styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 37px;

    width: 450px;
    padding: 35px;
    border-radius: 10px;
    box-shadow: 1px 1px 3px #333333;

    @media (max-width: 768px) {
      width: 340px;

      padding: 35px 20px;
    }
  `,

  LoginBoxText: styled.div`
    font-weight: 700;
    font-size: 18px;

    @media (max-width: 768px) {
      font-size: 15px;
    }
  `,

  LoginBoxGithubIcon: styled.img`
    width: 180px;
    height: 180px;

    @media (max-width: 768px) {
      width: 120px;
      height: 120px;
    }
  `,

  LoginButton: styled.a`
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 12px;

    width: 370px;
    height: 60px;
    padding: 0 45px;
    border: 1px solid var(--gray-800);
    border-radius: 5px;

    background-color: transparent;

    font-weight: 700;
    font-size: 14px;

    cursor: pointer;

    @media (max-width: 768px) {
      width: 260px;
      height: 55px;

      padding: 0 20px;
    }
  `,

  LoginButtonIcon: styled.img`
    width: 30px;
    height: 30px;

    @media (max-width: 768px) {
      width: 20px;
      height: 20px;
    }
  `,

  LoginButtonText: styled.div`
    font-size: 18px;
  `,
};
