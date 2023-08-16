import React from 'react';
import { createPortal } from 'react-dom';
import { css, keyframes, styled } from 'styled-components';
import completeIcon from '@/assets/complete-icon.svg';
import errorIcon from '@/assets/error-icon.svg';

interface Props {
  colorTheme: 'ERROR' | 'COMPLETION';
  title: string;
  description: string;
  ms: number;
}

const Toast = ({ colorTheme, description, title, ms }: Props) => {
  return createPortal(
    <S.ToastWrapper key={crypto.randomUUID()} $colorTheme={colorTheme} ms={ms}>
      <S.Icon src={colorTheme === 'COMPLETION' ? completeIcon : errorIcon} />
      <S.MessageContainer>
        <S.Title>{title}</S.Title>
        <S.Description>{description}</S.Description>
      </S.MessageContainer>
    </S.ToastWrapper>,
    document.getElementById('toast-root') as HTMLDivElement,
  );
};

export default Toast;

const fadeIn = keyframes`
  0% {
    transform: translate(-50%, 150%);
  }

  8%, 92% {
    transform: translate(-50%, 50%);
  }

  100% {
    transform: translate(-50%, 170%);
  }
`;

const S = {
  ToastWrapper: styled.div<{ $colorTheme: 'ERROR' | 'COMPLETION'; ms: number }>`
    ${({ $colorTheme }) => themeStyles[$colorTheme]}

    position: fixed;
    display: flex;
    align-items: center;
    gap: 15px;
    bottom: 8%;
    left: 50%;
    transform: translate(-50%, -50%);
    z-index: 999;

    width: 380px;
    height: 90px;
    padding: 10px 15px;
    border-radius: 8px;

    background-color: white;

    animation-name: ${fadeIn};
    animation-timing-function: linear;
    animation-duration: ${({ ms }) => `${ms + 50}ms`};
  `,

  Icon: styled.img`
    width: 32px;
  `,

  MessageContainer: styled.div`
    display: flex;
    flex-direction: column;
    gap: 10px;
  `,

  Title: styled.h2`
    font-size: 19px;
    font-weight: 700;
  `,

  Description: styled.div`
    font-size: 17px;
  `,
};

const themeStyles = {
  ERROR: css`
    border: 1.5px solid var(--baton-red);
    box-shadow: 0 0 16px 9px rgba(235, 87, 87, 0.1);
  `,

  COMPLETION: css`
    border: 1.5px solid#1ee06c;
    box-shadow: 0 0 16px 9px rgba(30, 224, 108, 0.1);
  `,
};
