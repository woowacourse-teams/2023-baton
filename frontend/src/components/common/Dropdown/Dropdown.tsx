import React, { useEffect, useState } from 'react';
import styled from 'styled-components';

interface Props extends React.HTMLProps<HTMLDivElement> {
  trigger: React.ReactNode;
  children: React.ReactNode;
  isDropdownOpen: boolean;
  gapFromTrigger: string;
  onClose: () => void;
}

const Dropdown = ({ trigger, children, isDropdownOpen, gapFromTrigger, onClose }: Props) => {
  const handleClose = (e: KeyboardEvent) => {
    if (e.key === 'Escape') {
      onClose();
    }
  };

  useEffect(() => {
    if (isDropdownOpen) {
      window.addEventListener('keydown', handleClose);
    }

    return () => {
      window.removeEventListener('keydown', handleClose);
    };
  }, [isDropdownOpen]);

  return (
    <>
      <S.DropdownContainer>
        {trigger}
        {isDropdownOpen ? (
          <>
            <S.DropdownMenuContainer $gapFromTrigger={gapFromTrigger}>{children}</S.DropdownMenuContainer>
            <S.BackDrop onClick={onClose} />
          </>
        ) : null}
      </S.DropdownContainer>
    </>
  );
};

export default Dropdown;

const S = {
  BackDrop: styled.div`
    position: fixed;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;
  `,

  DropdownContainer: styled.div`
    display: flex;
    justify-content: end;
    position: relative;
  `,

  DropdownMenuContainer: styled.div<{ $gapFromTrigger: string }>`
    position: absolute;
    top: ${({ $gapFromTrigger }) => $gapFromTrigger};
    background-color: var(--white);
    border-radius: 0 0 10px 10px;
    border: 1px solid var(--gray-400);
    box-shadow: 0px 0px 25px 0px rgba(0, 0, 0, 0.05);
    z-index: 101;
  `,
};
