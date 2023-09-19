import React from 'react';
import styled from 'styled-components';

interface Props extends React.HTMLProps<HTMLImageElement> {
  imageUrl: string;
}

const Avatar = ({ imageUrl, width, height, onClick }: Props) => {
  const handleSrcError = (e: React.SyntheticEvent<HTMLImageElement, Event>) => {
    e.currentTarget.src = 'https://via.placeholder.com/150';
  };

  return (
    <S.ImageWrapper>
      <S.Image
        src={imageUrl}
        $width={width}
        $height={height}
        alt="프로필"
        onClick={onClick}
        $isPointer={!!onClick}
        onError={handleSrcError}
      />
    </S.ImageWrapper>
  );
};

export default Avatar;

const S = {
  ImageWrapper: styled.div`
    display: flex;
    justify-content: center;
    align-items: center;
  `,

  Image: styled.img<{ $width?: string | number; $height?: string | number; $isPointer: boolean }>`
    width: ${({ $width }) => $width || '60px'};
    height: ${({ $height }) => $height || '60px'};

    border: 0.5px solid var(--gray-800);
    border-radius: 50%;

    object-fit: cover;

    cursor: ${({ $isPointer }) => ($isPointer ? 'pointer' : '')};
  `,
};
