import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import AutoCompleteItem from './AutoCompleteItem';
import { useMyGithubInfo } from '@/hooks/query/useMyGithubInfo';
import { useGithubRepoList } from '@/hooks/query/github/useGithubRepoList';
import { useGithubPrList } from '@/hooks/query/github/useGithubPrList';
import { typingGithubUrlPart } from '@/utils/githubUrl';

interface Props {
  url: string;
  setUrl: (url: string) => void;
  currentIndex: number;
  handleBlur: () => void;
  setAutoCompleteListLength: (length: number) => void;
  githubId: string;
  githubRepoName: string;
}

const AutoCompleteList = ({
  url,
  setUrl,
  currentIndex,
  setAutoCompleteListLength,
  githubId,
  githubRepoName,
}: Props) => {
  const [autoCompleteList, setAutoCompleteList] = useState<{ url: string; title: string }[]>([]);

  const { data: myGithubInfo } = useMyGithubInfo();
  const { data: githubRepoInfos } = useGithubRepoList(githubId);
  const { data: githubPrInfos } = useGithubPrList(githubId, githubRepoName);

  useEffect(() => {
    const typingPart = typingGithubUrlPart(url);

    switch (typingPart) {
      case 'domain':
      case 'userId':
        if (!myGithubInfo) return;

        const list = [{ title: '내 저장소', url: myGithubInfo.githubUrl }];
        setAutoCompleteList(list);
        setAutoCompleteListLength(1);
        break;

      case 'repoName':
        if (!githubRepoInfos) return;

        setAutoCompleteList(githubRepoInfos);
        setAutoCompleteListLength(githubRepoInfos.length);
        break;

      case 'pullRequest':
        if (!githubPrInfos) return;

        const data = githubPrInfos.data.map((item) => {
          return { title: item.title, url: 'https://github.com' + item.link };
        });

        setAutoCompleteList(data);
        setAutoCompleteListLength(data.length);
        break;

      case 'complete':
      default:
        break;
    }
  }, [url, myGithubInfo, githubRepoInfos, githubPrInfos]);

  useEffect(() => {
    if (currentIndex === 0) return;

    const newUrl = autoCompleteList[currentIndex - 1]?.url ?? '';
    setUrl(newUrl);
  }, [currentIndex]);

  return (
    <div>
      <S.InputUnderLine />
      {autoCompleteList?.map((item, i) => {
        const isPointed = i + 1 === currentIndex;
        return (
          <AutoCompleteItem
            key={item.url}
            title={item.title}
            url={item.url}
            selectItem={(url: string) => {}}
            pointItem={(url: string) => {}}
            isPointed={isPointed}
          />
        );
      })}
      <S.ListEndSpace />
    </div>
  );
};

export default AutoCompleteList;

const S = {
  InputUnderLine: styled.div`
    border-top: 1px solid var(--gray-300);
    height: 15px;
  `,

  ListItem: styled.li`
    display: flex;
    align-items: center;

    height: 45px;
    width: 100%;
    padding: 0 30px;

    font-size: 14px;

    cursor: pointer;

    &:hover {
      background-color: var(--gray-100);
    }

    &:focus {
      background-color: var(--gray-100);
      outline: none;
    }
  `,

  ListEndSpace: styled.div`
    height: 15px;
    width: 100%;
    border-radius: 5px;
  `,
};
