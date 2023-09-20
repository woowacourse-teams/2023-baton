import Spinner from '@/components/common/Spinner/Spinner';
import React from 'react';
import styled from 'styled-components';
import LogoImage from '@/assets/logo-image.svg';
import LogoImageMobile from '@/assets/logo-image-mobile.svg';
import useViewport from '@/hooks/useViewport';

const LoadingPage = () => {
  const { isMobile } = useViewport();

  return (
    <S.LoadingContainer>
      {isMobile ? null : (
        <S.HeaderWrapper>
          <S.HeaderContainer>
            <S.Logo onClick={() => {}} />
            <S.MenuContainer></S.MenuContainer>
          </S.HeaderContainer>
        </S.HeaderWrapper>
      )}
      <S.LoadingWrapper>
        <Spinner />
      </S.LoadingWrapper>
    </S.LoadingContainer>
  );
};

export default LoadingPage;

const S = {
  LoadingContainer: styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
  `,

  LoadingWrapper: styled.div`
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 80vh;
    max-width: 1200px;
    width: 100%;

    @media (max-width: 768px) {
      padding: 15px;
    }
  `,

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

  AvatarContainer: styled.div`
    display: flex;
    align-items: center;
    gap: 10px;

    cursor: pointer;

    @media (max-width: 768px) {
      gap: 5px;
    }
  `,

  ProfileName: styled.p`
    text-align: end;

    @media (max-width: 768px) {
    }
  `,

  Logo: styled.div`
    width: 197px;
    height: 35px;

    background-image: url(${LogoImage});
    background-size: cover;
    background-repeat: no-repeat;

    cursor: pointer;

    /* @media (max-width: 768px) {
      background-image: url(${LogoImageMobile});

      width: 53px;
      height: 30px;
    } */
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
