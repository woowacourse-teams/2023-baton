import React from 'react';
import styled from 'styled-components';

const Spinner = () => {
  return (
    <S.SpinnerWrapper>
      <div>
        <span>/</span>
      </div>
    </S.SpinnerWrapper>
  );
};

export default Spinner;

const S = {
  SpinnerWrapper: styled.div`
    div {
      color: var(--baton-red);
      font-family: Consolas, Menlo, Monaco, monospace;
      font-weight: 900;
      font-size: 120px;
      opacity: 0.9;

      @media (max-width: 768px) {
        font-size: 78px;
      }
    }
    div:before {
      content: '<';
      display: inline-block;
      animation: pulse 0.6s alternate infinite ease-in-out;
    }
    div:after {
      content: '>';
      display: inline-block;
      animation: pulse 0.6s 0.3s alternate infinite ease-in-out;
    }

    span {
      font-size: 112px;
      font-weight: 900;

      margin: 0 8px;
      animation: pulse 0.6s 0.3s alternate infinite ease-in-out;

      @media (max-width: 768px) {
        font-size: 74px;
      }
    }

    @keyframes pulse {
      to {
        transform: scale(1.2);
        opacity: 0.8;
      }
    }
  `,
};
