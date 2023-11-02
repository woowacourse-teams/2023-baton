import React, { Suspense, useCallback, useEffect, useRef, useState } from 'react';
import styled from 'styled-components';
import AutoCompleteList from './AutoCompleteList';
import Fallback from './Fallback';
import ShortCutIcon from '@/assets/select-window-icon.svg';

interface Props {
  value: string;
  setValue: (value: string) => void;
  initialValue?: string;
  handleBlur: () => void;
}

export interface Item {
  url: string;
  title: string;
}

const FocusInput = ({ value, setValue, initialValue, handleBlur }: Props) => {
  const [inputBuffer, setInputBuffer] = useState(value ?? initialValue ?? '');
  const [currentIndex, setCurrentIndex] = useState(0);
  const [autoCompleteListLength, setAutoCompleteListLength] = useState(0);

  const selectItemRef = useRef<{ selectPointedItem: () => void } | null>(null);

  useEffect(() => {
    if (value) return;

    setValue(initialValue ?? '');
  }, []);

  const inputRef = useCallback((element: HTMLInputElement) => {
    element?.focus();
  }, []);

  const handleChangeInput = (e: React.ChangeEvent<HTMLInputElement>) => {
    const input = e.target.value;

    setInputBuffer(input);
    setValue(input);

    setCurrentIndex(0);
  };

  const handleKeyDown = (e: React.KeyboardEvent) => {
    if (!['ArrowUp', 'ArrowDown', 'Enter', '/'].includes(e.key)) {
      return;
    }

    if (e.nativeEvent.isComposing) return;
    e.preventDefault();

    switch (e.key) {
      case 'ArrowUp':
        handleKeyDownArrowUp();
        break;
      case 'ArrowDown':
        handleKeyDownArrowDown();
        break;
      case 'Enter':
        handleKeyDownEnter();
        break;
      case '/':
        handleKeyDownEnter();
        break;
    }
  };

  const handleKeyDownEnter = () => {
    selectItemRef.current?.selectPointedItem();
  };

  const handleKeyDownArrowUp = () => {
    if (autoCompleteListLength === 0) return;

    const nextIndex = (currentIndex - 1) % (autoCompleteListLength + 1);

    if (nextIndex >= 0) {
      setCurrentIndex(nextIndex);
    }
  };

  const handleKeyDownArrowDown = () => {
    if (autoCompleteListLength === 0) return;

    let nextIndex = (currentIndex + 1) % (autoCompleteListLength + 1);

    if (nextIndex <= 0) nextIndex = 1;
    setCurrentIndex(nextIndex);
  };

  const handleMouseDownShortCut = (e: React.MouseEvent) => {
    e.preventDefault();

    window.open(value, '_blank', 'noopener, noreferrer');
  };

  return (
    <S.Container onKeyDown={handleKeyDown}>
      <S.InputContainer>
        <S.Input value={value} onChange={handleChangeInput} ref={inputRef} />
        <S.ShortcutContainer>
          <S.ShortcutIcon src={ShortCutIcon} onMouseDown={handleMouseDownShortCut} />
          <div>링크 바로가기</div>
        </S.ShortcutContainer>
      </S.InputContainer>
      <Suspense fallback={<Fallback />}>
        <AutoCompleteList
          url={value}
          setUrl={setValue}
          currentIndex={currentIndex}
          setCurrentIndex={setCurrentIndex}
          inputBuffer={inputBuffer}
          setInputBuffer={setInputBuffer}
          setAutoCompleteListLength={setAutoCompleteListLength}
          handleBlur={handleBlur}
          ref={selectItemRef}
        />
      </Suspense>
    </S.Container>
  );
};

export default FocusInput;

const S = {
  Container: styled.div`
    position: absolute;

    width: 700px;

    border-radius: 15px;
    box-shadow: 2px 2px 4px 2.5px rgba(0, 0, 0, 0.25);

    background-color: white;

    @media (max-width: 768px) {
      width: calc(100% - 30px);
    }
  `,

  InputContainer: styled.div`
    display: flex;
    align-items: center;
    gap: 20px;

    padding: 0 30px;
  `,

  Input: styled.input`
    width: calc(100% - 50px);
    height: 50px;
    border-radius: 20px;

    font-size: 18px;

    &::placeholder {
      font-size: 18px;
    }

    &:focus {
      outline: none;
    }

    @media (max-width: 768px) {
      font-size: 16px;
    }
  `,

  ShortcutContainer: styled.div`
    position: relative;

    &:hover div,
    &:active div {
      display: block;
    }

    & div {
      display: none;
      position: absolute;
      bottom: 0;
      left: 0;
      white-space: pre-line;

      border: var(--baton-red) solid 1px;
      border-radius: 5px;

      color: var(--baton-red);
      font-size: 12px;

      background-color: #ffffff;

      margin-top: 7px;
      padding: 7px 10px;

      width: max-content;

      z-index: 99;

      transform: translate(-38%, 120%);
    }

    & div::after {
      content: '';

      position: absolute;
      display: block;
      top: -7px;
      left: 50%;

      border-color: #ffffff transparent;
      border-width: 0 6px 8px 7px;
      border-style: solid;

      transform: translateX(-50%);

      z-index: 1;
    }

    & div::before {
      content: '';

      position: absolute;
      display: block;
      top: -8px;
      left: 50%;

      width: 0;
      border-color: var(--baton-red) transparent;
      border-width: 0 6px 8px 6.5px;
      border-style: solid;

      transform: translateX(-50%);

      z-index: 0;
    }
  `,

  ShortcutIcon: styled.img`
    width: 22px;
    height: 22px;

    animation: jump-shaking 1s 5;

    cursor: pointer;

    @keyframes jump-shaking {
      0% {
        transform: translateX(0);
      }
      25% {
        transform: translateY(-3px);
      }
      35% {
        transform: translateY(-3px) rotate(17deg);
      }
      55% {
        transform: translateY(-3px) rotate(-17deg);
      }
      65% {
        transform: translateY(-3px) rotate(17deg);
      }
      75% {
        transform: translateY(-3px) rotate(-17deg);
      }
      100% {
        transform: translateY(0) rotate(0);
      }
    }
  `,

  InputUnderLine: styled.div`
    border-top: 1px solid var(--gray-300);
    height: 15px;
  `,

  ListEndSpace: styled.div`
    height: 15px;
    width: 100%;
    border-radius: 5px;
  `,
};
