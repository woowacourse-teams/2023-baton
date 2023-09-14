import React from 'react';
import styled from 'styled-components';
import bannerBackground from '@/assets/banner/banner_background.png';
import eventBanner from '@/assets/banner/event_banner.webp';
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
    height: 292px;

    background-image: url(${bannerBackground});

    @media (max-width: 768px) {
      height: 120px;
    }
  `,

  BannerContents: styled.img`
    width: 904px;
    height: 240px;

    cursor: pointer;

    @media (max-width: 768px) {
      width: 340px;
      height: 90px;
    }
  `,
};
