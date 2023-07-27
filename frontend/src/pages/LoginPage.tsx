import Layout from '@/layout/Layout';
import React from 'react';
import { styled } from 'styled-components';
import LogoImage from '../assets/logo-image.svg';
import GithubIcon from '../assets/github-icon.svg';

const LoginPage = () => {
  return (
    <Layout>
      <S.LoginContainer>
        <S.Logo src={LogoImage} />
        <S.LoginBoxContainer>
          <S.LoginBoxText>코드 리뷰를 위해 github로 로그인 해주세요</S.LoginBoxText>
          <S.LoginBoxGithubIcon src={GithubIcon} />
          <S.LoginButton>
            <S.LoginButtonIcon src={GithubIcon} />
            <S.LoginButtonText>Sign in with github</S.LoginButtonText>
          </S.LoginButton>
        </S.LoginBoxContainer>
      </S.LoginContainer>
    </Layout>
  );
};

export default LoginPage;

const S = {
  LoginContainer: styled.div`
    display: flex;
    align-items: center;
    justify-content: center;
    flex-direction: column;
    gap: 30px;

    height: calc(100vh - 80px);
    min-height: 570px;

    overflow-y: hidden;
  `,

  Logo: styled.img`
    width: 300px;
    height: 80px;
  `,

  LoginBoxContainer: styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 37px;

    width: 450px;
    min-height: 400px;
    padding: 35px;
    border-radius: 10px;
    border: 1px solid var(--gray-700);
  `,

  LoginBoxText: styled.div`
    font-size: 18px;
  `,

  LoginBoxGithubIcon: styled.img`
    width: 180px;
    height: 180px;
  `,

  LoginButton: styled.a`
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: 6px;

    width: 370px;
    height: 60px;
    padding: 0 45px;
    border: 1px solid var(--gray-800);
    border-radius: 5px;

    background-color: transparent;

    font-weight: 700;
    font-size: 14px;

    cursor: pointer;
  `,

  LoginButtonIcon: styled.img`
    width: 35px;
    height: 35px;
  `,

  LoginButtonText: styled.div`
    font-size: 19px;
    margin-right: 40px;
  `,
};
