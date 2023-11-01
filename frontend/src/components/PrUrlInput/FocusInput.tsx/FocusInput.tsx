import React, { Suspense, useCallback, useRef, useState } from 'react';
import styled from 'styled-components';
import AutoCompleteList from './AutoCompleteList';

interface Props {
  value: string;
  setValue: (value: string) => void;
  handleBlur: () => void;
}

export interface Item {
  url: string;
  title: string;
}

// {
//   githubInfo && (
//     <S.Anchor href={githubInfo.githubUrl + '?tab=repositories'} target="_blank">
//       <img src={githubIcon} />
//       {!isMobile && <S.GoToGitHub>github</S.GoToGitHub>}
//     </S.Anchor>
//   );
// }

const FallbackUI = () => {
  return (
    <>
      <S.InputUnderLine />
      Loading...
      <S.ListEndSpace />
    </>
  );
};

const FocusInput = ({ value, setValue, handleBlur }: Props) => {
  const [inputBuffer, setInputBuffer] = useState(value);
  const [currentIndex, setCurrentIndex] = useState(0);
  const [autoCompleteListLength, setAutoCompleteListLength] = useState(0);

  const selectItemRef = useRef<{ selectPointedItem: () => void } | null>(null);

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

  return (
    <S.Container onKeyDown={handleKeyDown}>
      <S.Input value={value} onChange={handleChangeInput} ref={inputRef} />
      <Suspense fallback={<FallbackUI />}>
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
  `,

  Input: styled.input`
    width: 640px;
    padding: 0 30px;
    height: 50px;
    border-radius: 20px;

    font-size: 18px;

    &::placeholder {
      font-size: 18px;
    }

    &:focus {
      outline: none;
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
