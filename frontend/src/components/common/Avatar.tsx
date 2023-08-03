import React from 'react';
import styled from 'styled-components';

interface Props extends React.HTMLProps<HTMLImageElement> {
  imageUrl: string;
}

const Avatar = ({ imageUrl, width, height }: Props) => {
  return (
    <S.ImageWrapper>
      <S.Image src={imageUrl} $width={width} $height={height} alt="프로필" />
    </S.ImageWrapper>
  );
};

export default Avatar;

const S = {
  ImageWrapper: styled.div``,

  Image: styled.img<{ $width?: string | number; $height?: string | number }>`
    width: ${({ $width }) => $width || '60px'};
    height: ${({ $height }) => $height || '60px'};

    border: 0.5px solid var(--gray-800);
    border-radius: 50%;

    object-fit: cover;
  `,
};
