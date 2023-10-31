import React, { useCallback, useRef, useState } from 'react';
import styled from 'styled-components';
import AutoCompleteList from './AutoCompleteList';
import { typingGithubUrlPart } from '@/utils/githubUrl';

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

const FocusInput = ({ value, setValue, handleBlur }: Props) => {
  const [inputBuffer, setInputBuffer] = useState('');
  const [currentIndex, setCurrentIndex] = useState(0);
  const [autoCompleteListLength, setAutoCompleteListLength] = useState(0);

  const selectItemRef = useRef<{ handleKeyDownEnter: (e: React.KeyboardEvent) => void } | null>(null);

  const inputRef = useCallback((element: HTMLInputElement) => {
    element?.focus();
  }, []);

  const handleChangeInput = (e: React.ChangeEvent<HTMLInputElement>) => {
    setInputBuffer(e.target.value);
    setValue(e.target.value);

    setCurrentIndex(0);
  };

  const handleKeyDownEnter = (e: React.KeyboardEvent) => {
    selectItemRef.current?.handleKeyDownEnter(e);
  };

  const handleKeyDown = (e: React.KeyboardEvent) => {
    if (!['ArrowUp', 'ArrowDown', 'Enter'].includes(e.key)) {
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
        handleKeyDownEnter(e);
        break;
    }
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
      <AutoCompleteList
        url={value}
        setUrl={setValue}
        currentIndex={currentIndex}
        setCurrentIndex={setCurrentIndex}
        inputBuffer={inputBuffer}
        setAutoCompleteListLength={setAutoCompleteListLength}
        handleBlur={handleBlur}
        ref={selectItemRef}
      />
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
};
