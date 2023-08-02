import React from 'react';
import styled, { css } from 'styled-components';

interface Props extends React.HTMLProps<HTMLButtonElement> {
  children: React.ReactNode;
  colorTheme: 'RED' | 'WHITE' | 'GRAY' | 'BLACK';
  fontSize?: string | number;
  fontWeight?: number;
  type?: 'button' | 'submit' | 'reset';
}

const Button = ({ colorTheme, children, width, height, type, fontSize, fontWeight, onClick }: Props) => {
  return (
    <S.ButtonWrapper>
      <S.Button
        $width={width}
        $height={height}
        $colorTheme={colorTheme}
        type={type}
        $fontSize={fontSize}
        $fontWeight={fontWeight}
        onClick={onClick}
      >
        {children}
      </S.Button>
    </S.ButtonWrapper>
  );
};

export default Button;

const S = {
  ButtonWrapper: styled.div``,

  Button: styled.button<{
    $colorTheme: 'RED' | 'WHITE' | 'GRAY' | 'BLACK';
    $width?: string | number;
    $height?: string | number;
    $fontSize?: string | number;
    $fontWeight?: number;
  }>`
    ${({ $colorTheme }) => themeStyles[$colorTheme]}

    width: ${({ $width }) => $width || '180px'};
    height: ${({ $height }) => $height || '40px'};

    padding: 10px 10px;

    font-size: ${({ $fontSize }) => $fontSize || '18px'};
    font-weight: ${({ $fontWeight }) => $fontWeight || '400'};
  `,
};

export const themeStyles = {
  RED: css`
    background: var(--baton-red);
    border-radius: 6px;

    color: #ffffff;
  `,
  WHITE: css`
    background: #ffffff;
    border: 1px solid var(--baton-red);
    border-radius: 6px;

    color: var(--baton-red);
  `,
  GRAY: css`
    background: #ffffff;
    border: 1px solid var(--gray-500);
    border-radius: 6px;

    color: var(--gray-400);
  `,

  BLACK: css`
    background: #ffffff;
    border: 1px solid #000000;
    border-radius: 6px;

    color: #000000;
  `,
};
