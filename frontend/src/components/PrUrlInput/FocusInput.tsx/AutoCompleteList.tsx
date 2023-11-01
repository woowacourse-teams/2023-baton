import React, { forwardRef, useEffect, useImperativeHandle, useState } from 'react';
import styled from 'styled-components';
import AutoCompleteItem from './AutoCompleteItem';
import { useMyGithubInfo } from '@/hooks/query/useMyGithubInfo';
import { useGithubRepoList } from '@/hooks/query/github/useGithubRepoList';
import { useGithubPrList } from '@/hooks/query/github/useGithubPrList';
import { checkTypingPullRequestUrl, checkTypingRepositoryUrl, typingGithubUrlPart } from '@/utils/githubUrl';

interface Props {
  url: string;
  setUrl: (url: string) => void;
  currentIndex: number;
  setCurrentIndex: (index: number) => void;
  inputBuffer: string;
  setInputBuffer: (url: string) => void;
  setAutoCompleteListLength: (length: number) => void;
  handleBlur: () => void;
}

const AutoCompleteList = forwardRef<{ handleKeyDownEnter: (e: React.KeyboardEvent) => void }, Props>(
  (
    { url, setUrl, currentIndex, setCurrentIndex, inputBuffer, setInputBuffer, handleBlur, setAutoCompleteListLength },
    ref,
  ) => {
    const [autoCompleteList, setAutoCompleteList] = useState<{ url: string; title: string }[]>([]);

    const [githubId, setGithubId] = useState('');
    const [githubRepoName, setGithubRepoName] = useState('');

    const { data: myGithubInfo } = useMyGithubInfo();
    const { data: githubRepoInfos } = useGithubRepoList(githubId, '', checkTypingRepositoryUrl(url));
    const { data: githubPrInfos } = useGithubPrList(githubId, githubRepoName, checkTypingPullRequestUrl(url));

    useImperativeHandle(ref, () => {
      return {
        handleKeyDownEnter,
      };
    });

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

          if (githubRepoInfos.isDummy) return;

          setAutoCompleteList(githubRepoInfos.data);
          setAutoCompleteListLength(githubRepoInfos.data.length);

          break;

        case 'pullRequest':
          if (!githubPrInfos) return;

          if (githubPrInfos.isDummy) return;

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
      if (currentIndex === 0) {
        setUrl(inputBuffer);

        return;
      }

      const newUrl = autoCompleteList[currentIndex - 1]?.url ?? '';
      setUrl(newUrl);
    }, [currentIndex]);

    const handleKeyDownEnter = (e: React.KeyboardEvent) => {
      e.preventDefault();

      selectPointedItem();
    };

    const selectItem = (newUrl: string) => {
      const typingPart = typingGithubUrlPart(newUrl);

      switch (typingPart) {
        case 'userId':
          setInputBuffer(newUrl + '/');
          setUrl(newUrl + '/');
          setCurrentIndex(0);

          const newUserId1 = newUrl.split('/').slice(-1)[0];
          setGithubId(newUserId1);

          break;

        case 'repoName':
          setInputBuffer(newUrl + '/pull/');
          setUrl(newUrl + '/pull/');
          setCurrentIndex(0);

          const newRepoName = newUrl.split('/').slice(-1)[0];
          const newUserId = newUrl.split('/').slice(-2)[0];
          setGithubId(newUserId);
          setGithubRepoName(newRepoName);

          break;

        case 'complete':
          setUrl(newUrl);
          handleBlur();

          break;
      }
    };

    const selectPointedItem = () => {
      const typingPart = typingGithubUrlPart(url);

      switch (typingPart) {
        case 'userId':
          setInputBuffer(url + '/');
          setUrl(url + '/');
          setCurrentIndex(0);

          const newUserId1 = url.split('/').slice(-1)[0];
          setGithubId(newUserId1);

          break;

        case 'repoName':
          setInputBuffer(url + '/pull/');
          setUrl(url + '/pull/');
          setCurrentIndex(0);

          const newRepoName = url.split('/').slice(-1)[0];
          const newUserId = url.split('/').slice(-2)[0];
          setGithubId(newUserId);
          setGithubRepoName(newRepoName);

          break;

        case 'complete':
          handleBlur();
          break;
      }
    };

    return (
      <>
        {autoCompleteList.length > 0 && (
          <>
            <S.InputUnderLine />
            {autoCompleteList?.map((item, i) => {
              const isPointed = i + 1 === currentIndex;
              return (
                <AutoCompleteItem
                  key={item.url}
                  title={item.title}
                  url={item.url}
                  selectItem={selectItem}
                  pointItem={(url: string) => {}}
                  isPointed={isPointed}
                />
              );
            })}
            <S.ListEndSpace />
          </>
        )}
      </>
    );
  },
);

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
