import React from 'react';
import styled from 'styled-components';
import eventBanner from '@/assets/banner/banner.webp';
import { usePageRouter } from '@/hooks/usePageRouter';

const Banner = () => {
  const { goToNoticePage } = usePageRouter();

  const handleBannerButton = () => {
    goToNoticePage();
  };

  return (
    <S.BannerBackground>
      <S.BannerContents onClick={handleBannerButton} src={eventBanner}></S.BannerContents>
    </S.BannerBackground>
  );
};

export default Banner;

const S = {
  BannerBackground: styled.div`
    display: flex;
    justify-content: center;
    align-items: center;

    width: 100%;
    height: 100%;
  `,

  BannerContents: styled.img`
    width: 240px;

    cursor: pointer;
  `,
};
