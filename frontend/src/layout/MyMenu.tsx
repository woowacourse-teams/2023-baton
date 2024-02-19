import React, { Suspense, useEffect, useState } from 'react';
import styled from 'styled-components';
import LogoImage from '@/assets/logo-image.svg';
import LogoImageMobile from '@/assets/logo-image-mobile.svg';
import NotificationOffIcon from '@/assets/notification_off.svg';
import NotificationOnIcon from '@/assets/notification_on.svg';
import Avatar from '@/components/common/Avatar/Avatar';
import { useHeaderProfile } from '@/hooks/query/useHeaderProfile';
import Dropdown from '@/components/common/Dropdown/Dropdown';
import NotificationDropdown from '@/components/NotificationDropdown/NotificationDropdown';
import ProfileDropdown from '@/components/ProfileDropdown/ProfileDropdown';
import { useNotification } from '@/hooks/query/useNotification';

const MyMenu = () => {
  const [isNotificationDropdownOpen, setIsNotificationDropdownOpen] = useState(false);
  const [isProfileDropdownOpen, setIsProfileDropdownOpen] = useState(false);
  const [isAllRead, setIsAllRead] = useState<boolean>(true);

  const { data: profile } = useHeaderProfile();
  const { data: notificationList } = useNotification();

  useEffect(() => {
    checkIsAllRead();
  }, [isAllRead]);

  const checkIsAllRead = () => {
    const isNotRead = notificationList.data.filter((notification) => {
      return !notification.isRead;
    });

    setIsAllRead(isNotRead.length === 0);
  };

  const handleNotificationDropdown = () => {
    setIsNotificationDropdownOpen(!isNotificationDropdownOpen);
    setIsProfileDropdownOpen(false);
  };

  const handleProfileDropdown = () => {
    setIsProfileDropdownOpen(!isProfileDropdownOpen);
    setIsNotificationDropdownOpen(false);
  };

  const handleCloseDropdown = () => {
    setIsProfileDropdownOpen(false);
    setIsNotificationDropdownOpen(false);
  };

  return (
    <S.MyMenuContainer>
      <S.NotificationContainer>
        <Dropdown
          onClose={handleCloseDropdown}
          gapFromTrigger="42px"
          isDropdownOpen={isNotificationDropdownOpen}
          trigger={
            <S.NotificationIcon
              onClick={handleNotificationDropdown}
              src={isAllRead ? NotificationOffIcon : NotificationOnIcon}
            />
          }
        >
          <Suspense>
            <NotificationDropdown checkIsAllRead={checkIsAllRead} notificationList={notificationList.data} />
          </Suspense>
        </Dropdown>
      </S.NotificationContainer>
      <Dropdown
        onClose={handleCloseDropdown}
        gapFromTrigger="47px"
        isDropdownOpen={isProfileDropdownOpen}
        trigger={
          <S.AvatarContainer onClick={handleProfileDropdown}>
            <Avatar width="35px" height="35px" imageUrl={profile?.imageUrl || 'https://via.placeholder.com/150'} />
          </S.AvatarContainer>
        }
      >
        <ProfileDropdown />
      </Dropdown>
    </S.MyMenuContainer>
  );
};

export default MyMenu;

const S = {
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

  MyMenuContainer: styled.div`
    display: flex;
    align-items: center;
    gap: 30px;

    @media (max-width: 768px) {
      gap: 16px;
    }
  `,
};
