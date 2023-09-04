import React, { useState } from 'react';
import TagIcon from '@/assets/tag-icon.svg';
import { styled } from 'styled-components';
import RunnerPostFilter from '../RunnerPostFilter/RunnerPostFilter';
import { ReviewStatus } from '@/types/runnerPost';

interface Props {
  reviewStatus: ReviewStatus;
  setReviewStatus: React.Dispatch<React.SetStateAction<ReviewStatus>>;
  tag: string;
  setTag: React.Dispatch<React.SetStateAction<string>>;
  searchedTags: string[];
  setSearchedTags: React.Dispatch<React.SetStateAction<string[]>>;
  searchPosts: (reviewStatus: ReviewStatus, tag?: string) => void;
}

const RunnerPostSearchBox = ({
  reviewStatus,
  setReviewStatus,
  tag,
  setTag,
  searchedTags,
  setSearchedTags,
  searchPosts,
}: Props) => {
  const [isInputFocused, setIsInputFocused] = useState<boolean>(false);

  const handleChangeInput = (e: React.ChangeEvent<HTMLInputElement>) => {
    setTag(e.target.value);
  };

  const handleClickRadioButton = (e: React.ChangeEvent<HTMLInputElement>) => {
    const clickedStatus = e.target.value as ReviewStatus;

    if (clickedStatus === reviewStatus) return;

    searchPosts(clickedStatus, tag);
    setReviewStatus(clickedStatus);
  };

  const onFocus = () => {
    setIsInputFocused(true);
  };

  const outFocus = () => {
    setIsInputFocused(false);
  };

  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    searchPosts(reviewStatus, tag);
  };

  const handleClickSearchedTag = (e: React.MouseEvent<HTMLLIElement>) => {
    searchPosts(reviewStatus, e.currentTarget.id);
  };

  return (
    <S.SearchBoxContainer onSubmit={handleSubmit}>
      <RunnerPostFilter reviewStatus={reviewStatus} handleClickRadioButton={handleClickRadioButton} />
      <S.TagContainer>
        <S.TitleContainer>
          <S.Icon src={TagIcon} />
          <S.Title>Tags</S.Title>
        </S.TitleContainer>
        <S.InputContainer onFocus={onFocus} onBlur={outFocus}>
          <S.TagInput placeholder="태그명 검색" value={tag} onChange={handleChangeInput} />
          <S.SearchedTagList $isVisible={searchedTags.length > 0 && isInputFocused}>
            {searchedTags.map((tag) => (
              <S.searchedTagItem key={tag} id={tag} onMouseDown={handleClickSearchedTag}>
                {tag}
              </S.searchedTagItem>
            ))}
          </S.SearchedTagList>
        </S.InputContainer>
      </S.TagContainer>
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

  TagContainer: styled.div`
    display: flex;
    flex-direction: row;
    align-items: center;
    gap: 10px;
  `,

  TitleContainer: styled.div`
    display: flex;
    align-items: flex-end;
    gap: 5px;

    width: 70px;
  `,

  Icon: styled.img`
    width: 22px;
  `,

  Title: styled.h3`
    font-size: 20px;
  `,

  InputContainer: styled.div`
    position: relative;
    display: flex;
    flex-direction: column;
  `,

  TagInput: styled.input`
    width: 300px;
    height: 34px;

    border: 1px solid var(--gray-400);
    border-radius: 5px;
    padding: 6px 8px;
  `,

  SearchedTagList: styled.ul<{ $isVisible: boolean }>`
    display: flex;
    visibility: ${({ $isVisible }) => ($isVisible ? 'visible' : 'hidden')};

    flex-direction: column;
    position: absolute;
    gap: 5px;
    top: 34px;
    z-index: 100;

    width: 300px;

    border: 1px solid var(--gray-400);
    border-top: none;
    border-radius: 5px;
    background: white;
  `,

  searchedTagItem: styled.li`
    padding: 6px 8px;

    &:hover {
      background: var(--gray-200);
    }
  `,
};
