import React, { useState } from 'react';
import styled from 'styled-components';

interface Props extends React.HTMLProps<HTMLDivElement> {
  trigger: React.ReactNode;
  children: React.ReactNode;
  isDropdownOpen: boolean;
  gapFromTrigger: string;
}

const Dropdown = ({ trigger, children, isDropdownOpen, gapFromTrigger }: Props) => {
  return (
    <>
      <S.DropdownContainer>
        {trigger}
        {isDropdownOpen ? (
          <S.DropdownMenuContainer $gapFromTrigger={gapFromTrigger}>{children}</S.DropdownMenuContainer>
        ) : null}
      </S.DropdownContainer>
    </>
  );
};

export default Dropdown;

const S = {
  DropdownContainer: styled.div`
    display: flex;
    justify-content: end;
    position: relative;

    @media (max-width: 768px) {
      justify-content: center;
    }
  `,

  DropdownMenuContainer: styled.div<{ $gapFromTrigger: string }>`
    position: absolute;
    top: ${({ $gapFromTrigger }) => $gapFromTrigger};

    background-color: var(--white-color);

    border-radius: 0 0 10px 10px;
    border: 1px solid var(--gray-400);
    box-shadow: 0px 0px 25px 0px rgba(0, 0, 0, 0.05);

    z-index: 101;
  `,
};
