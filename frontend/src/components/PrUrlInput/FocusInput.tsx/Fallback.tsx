import styled from 'styled-components';
import React from 'react';
import DropletSpinner from '@/components/common/Spinner/DropletSpinner';

const Fallback = () => {
  return (
    <>
      <S.InputUnderLine />
      <S.LoadingIcon>
        <DropletSpinner />
      </S.LoadingIcon>
    </>
  );
};

export default Fallback;

const S = {
  LoadingIcon: styled.div`
    padding-left: 30px;
  `,

  InputUnderLine: styled.div`
    height: 7px;
    border-top: 1px solid var(--gray-300);

    @media (max-width: 768px) {
      height: 4px;
    }
  `,

  ListEndSpace: styled.div`
    width: 100%;
  `,
};
