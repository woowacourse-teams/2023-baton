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
  const [githubId, setGithubId] = useState('');
  const [githubRepoName, setGithubRepoName] = useState('');

  const inputBuffer = useRef('');
  const [currentIndex, setCurrentIndex] = useState(0);
  const [autoCompleteListLength, setAutoCompleteListLength] = useState(0);

  const inputRef = useCallback((element: HTMLInputElement) => {
    element?.focus();
  }, []);

  const handleChangeInput = (e: React.ChangeEvent<HTMLInputElement>) => {
    inputBuffer.current = e.target.value;

    setValue(e.target.value);

    setCurrentIndex(0);
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
        selectItem();
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

  const selectItem = () => {
    const typingPart = typingGithubUrlPart(value);
    console.log(typingPart, value);

    switch (typingPart) {
      case 'userId':
        inputBuffer.current = value + '/';
        setValue(value + '/');
        setCurrentIndex(0);

        const newUserId1 = value.split('/').slice(-1)[0];
        setGithubId(newUserId1);

        break;

      case 'repoName':
        inputBuffer.current = value + '/pull/';
        setValue(value + '/pull/');
        setCurrentIndex(0);

        const newRepoName = value.split('/').slice(-1)[0];
        const newUserId = value.split('/').slice(-2)[0];
        setGithubId(newUserId);
        setGithubRepoName(newRepoName);

        break;

      case 'complete':
        handleBlur();
        break;
    }
  };

  return (
    <S.Container onKeyDown={handleKeyDown}>
      <S.Input value={value} onChange={handleChangeInput} ref={inputRef} />
      <AutoCompleteList
        url={value}
        setUrl={setValue}
        currentIndex={currentIndex}
        setAutoCompleteListLength={setAutoCompleteListLength}
        handleBlur={handleBlur}
        githubId={githubId}
        githubRepoName={githubRepoName}
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
