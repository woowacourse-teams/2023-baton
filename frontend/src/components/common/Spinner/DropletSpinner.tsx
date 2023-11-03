import React from 'react';
import styled from 'styled-components';

const DropletSpinner = () => {
  return (
    <S.Container>
      <S.DropletSpinner>
        <S.DropLet />
        <S.DropLet />
        <S.DropLet />
      </S.DropletSpinner>
    </S.Container>
  );
};

export default DropletSpinner;

const S = {
  Container: styled.div`
    width: calc(100% - 50px);
    text-align: center;
  `,

  DropletSpinner: styled.div`
    display: flex;
    justify-content: center;
    margin: 30px;
  `,

  DropLet: styled.div`
    width: 9px;
    height: 9px;
    margin: 0 3px;

    background-color: var(--gray-400);
    border-radius: 50%;
    transform-origin: center bottom;

    animation: bounce 1.2s cubic-bezier(0.3, 0.01, 0.4, 1) infinite;

    &:nth-child(1) {
      animation-delay: -0.4s;
    }

    &:nth-child(2) {
      animation-delay: -0.2s;
    }

    &:nth-child(3) {
      animation-delay: 0;
    }

    @keyframes bounce {
      0%,
      100% {
        transform: translateY(2px);
      }
      50% {
        transform: translateY(-10px);
      }
    }

    @media (max-width: 768px) {
      height: 8px;
      width: 8px;
    }
  `,
};
