import { usePageRouter } from '@/hooks/usePageRouter';
import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import LogoImage from '@/assets/logo-image.svg';
import LogoImageMobile from '@/assets/logo-image-mobile.svg';
import { GetHeaderProfileResponse } from '@/types/profile';
import Avatar from '@/components/common/Avatar/Avatar';
import Button from '@/components/common/Button/Button';
import { useFetch } from '@/hooks/useFetch';
import { useLogin } from '@/hooks/useLogin';
import useViewport from '@/hooks/useViewport';

const Header = () => {
  const [profile, setProfile] = useState<GetHeaderProfileResponse | null>(null);

  const { goToMainPage, goToLoginPage, goToMyPage } = usePageRouter();
  const { isLogin, logout } = useLogin();
  const { isMobile } = useViewport();
  const { getRequestWithAuth } = useFetch();

  useEffect(() => {
    if (isLogin) getProfile();
  }, []);

  const getProfile = () => {
    getRequestWithAuth(`/profile/me`, async (response) => {
      const data: GetHeaderProfileResponse = await response.json();

      setProfile(data);
    });
  };

  const handleClickLogoutButton = () => {
    logout();

    goToMainPage();
  };

  const handleClickProfile = () => {
    goToMyPage();
  };

  return (
    <S.HeaderWrapper>
      <S.HeaderContainer>
        <S.Logo onClick={goToMainPage} />
        <S.MenuContainer>
          {isLogin ? (
            <>
              <S.AvatarContainer onClick={handleClickProfile}>
                {isMobile ? null : <S.ProfileName>{profile?.name}</S.ProfileName>}
                <Avatar width="35px" height="35px" imageUrl={profile?.imageUrl || 'https://via.placeholder.com/150'} />
              </S.AvatarContainer>
              <Button
                fontSize="14px"
                width={isMobile ? '80px' : '85px'}
                height="35px"
                colorTheme="WHITE"
                onClick={handleClickLogoutButton}
              >
                로그아웃
              </Button>
            </>
          ) : (
            <Button fontSize="14px" width="76px" height="35px" colorTheme="RED" onClick={goToLoginPage}>
              로그인
            </Button>
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
