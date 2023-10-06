import { usePageRouter } from '@/hooks/usePageRouter';
import React, { useState } from 'react';
import styled from 'styled-components';
import LogoImage from '@/assets/logo-image.svg';
import LogoImageMobile from '@/assets/logo-image-mobile.svg';
import AlarmOffIcon from '@/assets/Alarm_off.svg';
import AlarmOnIcon from '@/assets/Alarm_on.svg';
import Avatar from '@/components/common/Avatar/Avatar';
import Button from '@/components/common/Button/Button';
import { useLogin } from '@/hooks/useLogin';
import { useHeaderProfile } from '@/hooks/query/useHeaderProfile';
import Dropdown from '@/components/common/Dropdown/Dropdown';
import AlarmDropdown from '@/components/AlarmDropdown/AlarmDropdown';
import ProfileDropdown from '@/components/ProfileDropdown/ProfileDropdown';

const Header = () => {
  const [isAlarmDropdownOpen, setIsAlarmDropdownOpen] = useState(false);
  const [isProfileDropdownOpen, setIsProfileDropdownOpen] = useState(false);

  const { goToMainPage, goToLoginPage } = usePageRouter();
  const { isLogin } = useLogin();
  const { data: profile } = useHeaderProfile(isLogin);

  const handleAlarmDropdown = () => {
    setIsAlarmDropdownOpen(!isAlarmDropdownOpen);
    setIsProfileDropdownOpen(false);
  };

  const handleProfileDropdown = () => {
    setIsProfileDropdownOpen(!isProfileDropdownOpen);
    setIsAlarmDropdownOpen(false);
  };

  const handleCloseDropdown = () => {
    setIsProfileDropdownOpen(false);
    setIsAlarmDropdownOpen(false);
  };

  return (
    <>
      <S.HeaderWrapper>
        <S.HeaderContainer>
          <S.Logo onClick={goToMainPage} />
          <S.MenuContainer>
            {isLogin ? (
              <>
                <S.AlarmContainer>
                  <Dropdown
                    onClose={handleCloseDropdown}
                    gapFromTrigger="52px"
                    isDropdownOpen={isAlarmDropdownOpen}
                    trigger={<S.AlarmIcon onClick={handleAlarmDropdown} src={AlarmOnIcon} />}
                  >
                    <AlarmDropdown />
                  </Dropdown>
                  {/* <S.AlarmIcon src={AlarmOffIcon} /> */}
                </S.AlarmContainer>
                <Dropdown
                  onClose={handleCloseDropdown}
                  gapFromTrigger="57px"
                  isDropdownOpen={isProfileDropdownOpen}
                  trigger={
                    <S.AvatarContainer onClick={handleProfileDropdown}>
                      <Avatar
                        width="35px"
                        height="35px"
                        imageUrl={profile?.imageUrl || 'https://via.placeholder.com/150'}
                      />
                    </S.AvatarContainer>
                  }
                >
                  <ProfileDropdown />
                </Dropdown>
              </>
            ) : (
              <Button fontSize="14px" width="76px" height="35px" colorTheme="RED" onClick={goToLoginPage}>
                로그인
              </Button>
            )}
          </S.MenuContainer>
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

  AlarmContainer: styled.div``,

  AlarmIcon: styled.img`
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
