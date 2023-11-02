import { extractPrNumber, typingGithubUrlPart } from '@/utils/githubUrl';
import React from 'react';
import styled from 'styled-components';

import GithubIcon from '@/assets/github-icon.svg';
import RepoIcon from '@/assets/disk-icon.svg';
import PrIcon from '@/assets/request-icon.svg';

interface Props {
  title: string;
  url: string;
  isPointed: boolean;
  selectItem: (url: string) => void;
}

const AutoCompleteItem = ({ title, url, isPointed, selectItem }: Props) => {
  const handleClickItem = (e: React.MouseEvent<HTMLLIElement>) => {
    e.preventDefault();

    selectItem(url);
  };

  const getIconSrc = () => {
    const urlPart = typingGithubUrlPart(url);

    switch (urlPart) {
      case 'pullRequest':
      case 'complete':
        return PrIcon;
      case 'repoName':
        return RepoIcon;
      default:
        return GithubIcon;
    }
  };

  const subInfo = () => {
    const prNumber = extractPrNumber(url) ?? '';

    return prNumber ? '#' + prNumber : '';
  };

  return (
    <S.ListItem $isPointed={isPointed} onMouseDown={handleClickItem}>
      <S.Icon src={getIconSrc()}></S.Icon>
      <S.ListText>{`${title} ${subInfo()}`}</S.ListText>
    </S.ListItem>
  );
};

export default AutoCompleteItem;

const S = {
  ListItem: styled.li<{ $isPointed: boolean }>`
    display: flex;
    align-items: center;
    gap: 15px;

    height: 45px;
    width: 100%;
    padding: 0 30px;

    background-color: ${({ $isPointed }) => $isPointed && 'var(--gray-100)'};

    cursor: pointer;

    &:hover {
      background-color: var(--gray-100);
    }

    &:focus {
      background-color: var(--gray-100);
      outline: none;
    }
  `,

  ListText: styled.div`
    width: 100%;

    font-size: 14px;

    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  `,

  Icon: styled.img`
    width: 20px;
    height: 20px;

    @media (max-width: 768px) {
      width: 18px;
      height: 18px;
    }
  `,
};
