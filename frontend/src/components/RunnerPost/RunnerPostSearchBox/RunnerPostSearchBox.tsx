import React, { useRef, useState } from 'react';
import TagIcon from '@/assets/tag-icon.svg';
import { styled } from 'styled-components';
import RunnerPostFilter from '../RunnerPostFilter/RunnerPostFilter';
import { ReviewStatus } from '@/types/runnerPost';
import { useSearchTag } from '@/hooks/query/useSearchTag';

interface Props {
  reviewStatus: ReviewStatus;
  setReviewStatus: React.Dispatch<React.SetStateAction<ReviewStatus>>;
  setEnteredTag: React.Dispatch<React.SetStateAction<string>>;
  fetchNextPage: () => void;
}

const RunnerPostSearchBox = ({ reviewStatus, setReviewStatus, setEnteredTag, fetchNextPage }: Props) => {
  const [keyword, setKeyword] = useState('');
  const [keywordForResponse, setKeywordForResponse] = useState('');

  const [isInputFocused, setIsInputFocused] = useState<boolean>(false);
  const [inputIndex, setInputIndex] = useState<number>(0);
  const [inputBuffer, setInputBuffer] = useState<string>('');

  const inputRefs = useRef<HTMLElement[]>([]);
  const timer = useRef<number | null>(null);

  const { data: searchedTags } = useSearchTag(keywordForResponse);

  const handleChangeInput = (e: React.ChangeEvent<HTMLInputElement>) => {
    e.preventDefault();

    setKeyword(e.target.value);
    setInputBuffer(e.target.value);

    if (timer.current) window.clearTimeout(timer.current);

    timer.current = window.setTimeout(() => {
      setKeyword(e.target.value);
      setKeywordForResponse(e.target.value);
    }, 500);
  };

  const handleClickRadioButton = (e: React.ChangeEvent<HTMLInputElement>) => {
    const clickedStatus = e.target.value as ReviewStatus;

    if (clickedStatus === reviewStatus) return;

    setReviewStatus(clickedStatus);
  };

  const handleInputFocus = () => {
    setIsInputFocused(true);
  };

  const handleInputBlur = () => {
    setIsInputFocused(false);
  };

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    setEnteredTag(keyword);

    (document.activeElement as HTMLElement).blur();
  };

  const handleClickSearchedTag = (e: React.MouseEvent<HTMLLIElement>) => {
    setEnteredTag(e.currentTarget.id);
    setKeyword(e.currentTarget.id);

    (document.activeElement as HTMLElement).blur();
  };

  const handleKeyDown = (e: React.KeyboardEvent) => {
    if (!['ArrowUp', 'ArrowDown', 'Enter'].includes(e.key)) {
      inputRefs.current[0].focus();
      setInputIndex(0);

      return;
    }

    if (e.nativeEvent.isComposing) return;
    e.preventDefault();

    switch (e.key) {
      case 'ArrowUp':
        handleArrowUp();
        break;
      case 'ArrowDown':
        handleArrowDown();
        break;
      case 'Enter':
        handleEnter();
    }
  };

  const handleArrowUp = () => {
    const nextIndex = inputIndex >= 1 ? inputIndex - 1 : searchedTags.length;

    setKeyword(nextIndex === 0 ? inputBuffer : searchedTags[nextIndex - 1].tagName);
    setInputIndex(nextIndex);

    inputRefs.current[nextIndex].focus();
  };

  const handleArrowDown = () => {
    const nextIndex = inputIndex < searchedTags.length ? inputIndex + 1 : 0;

    setKeyword(nextIndex === 0 ? inputBuffer : searchedTags[nextIndex - 1].tagName);
    setInputIndex(nextIndex);

    inputRefs.current[nextIndex].focus();
  };

  const handleEnter = () => {
    setEnteredTag(keyword);

    (document.activeElement as HTMLElement).blur();
  };

  return (
    <S.SearchBoxContainer onSubmit={handleSubmit}>
      <RunnerPostFilter reviewStatus={reviewStatus} handleClickRadioButton={handleClickRadioButton} />
      <S.InputContainer onFocus={handleInputFocus} onBlur={handleInputBlur} onKeyDown={handleKeyDown}>
        <S.TagInput
          placeholder="태그명 검색"
          value={keyword}
          ref={(element) => {
            if (element) inputRefs.current[0] = element;
          }}
          onChange={handleChangeInput}
          aria-label="태그명 검색"
        />
        <S.SearchedTagList $isVisible={searchedTags.length > 0 && isInputFocused}>
          {searchedTags.map((tag, idx) => (
            <S.searchedTagItem
              key={tag.id}
              id={tag.tagName}
              tabIndex={idx}
              ref={(element) => {
                if (element) inputRefs.current[idx + 1] = element;
              }}
              onMouseDown={handleClickSearchedTag}
            >
              {tag.tagName}
            </S.searchedTagItem>
          ))}
        </S.SearchedTagList>
      </S.InputContainer>
    </S.SearchBoxContainer>
  );
};

export default RunnerPostSearchBox;

const S = {
  SearchBoxContainer: styled.form`
    display: flex;
    flex-direction: column;
    gap: 18px;
  `,

  InputContainer: styled.div`
    position: relative;
    display: flex;
    flex-direction: column;
  `,

  TagInput: styled.input`
    width: 320px;
    height: 40px;

    background-image: url(${TagIcon});
    background-position: 6px center;
    background-repeat: no-repeat;

    border: 1px solid var(--gray-400);
    border-radius: 5px;
    padding: 6px 31px;

    font-size: 18px;

    @media (max-width: 768px) {
      width: 280px;
      height: 36px;

      font-size: 16px;
    }
  `,

  SearchedTagList: styled.ul<{ $isVisible: boolean }>`
    display: flex;
    visibility: ${({ $isVisible }) => ($isVisible ? 'visible' : 'hidden')};

    flex-direction: column;
    position: absolute;
    gap: 5px;
    top: 40px;
    z-index: 100;

    width: 320px;

    border: 1px solid var(--gray-400);
    border-top: none;
    border-radius: 5px;
    background: white;

    font-size: 18px;

    @media (max-width: 768px) {
      width: 280px;

      font-size: 16px;
    }
  `,

  searchedTagItem: styled.li`
    padding: 6px 8px;

    &:hover {
      background: var(--gray-200);
    }

    &:focus {
      background: var(--gray-200);

      outline: none;
    }
  `,
};
