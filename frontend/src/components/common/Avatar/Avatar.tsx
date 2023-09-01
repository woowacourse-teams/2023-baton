import React from 'react';
import styled from 'styled-components';

interface Props extends React.HTMLProps<HTMLImageElement> {
  imageUrl: string;
}

const Avatar = ({ imageUrl, width, height, onClick }: Props) => {
  return (
    <S.ImageWrapper>
      <S.Image src={imageUrl} $width={width} $height={height} alt="프로필" onClick={onClick} $isPointer={!!onClick} />
    </S.ImageWrapper>
  );
};

export default Avatar;

const S = {
  ImageWrapper: styled.div``,

  Image: styled.img<{ $width?: string | number; $height?: string | number; $isPointer: boolean }>`
    width: ${({ $width }) => $width || '60px'};
    height: ${({ $height }) => $height || '60px'};

    border: 0.5px solid var(--gray-800);
    border-radius: 50%;

    object-fit: cover;

    cursor: ${({ $isPointer }) => ($isPointer ? 'pointer' : '')};

    @media (max-width: 768px) {
      width: 30px;
      height: 30px;
    }
  `,
};
