import React, { forwardRef, useEffect, useImperativeHandle, useState } from 'react';
import styled from 'styled-components';
import AutoCompleteItem from './AutoCompleteItem';
import { useMyGithubInfo } from '@/hooks/query/useMyGithubInfo';
import { useGithubRepoList } from '@/hooks/query/github/useGithubRepoList';
import { useGithubPrList } from '@/hooks/query/github/useGithubPrList';
import {
  checkTypingPullRequestUrl,
  checkTypingRepositoryUrl,
  extractRepoName,
  extractUserId,
  typingGithubUrlPart,
} from '@/utils/githubUrl';

interface Props {
  url: string;
  setUrl: React.Dispatch<React.SetStateAction<string>>;
  currentIndex: number;
  setCurrentIndex: React.Dispatch<React.SetStateAction<number>>;
  inputBuffer: string;
  setInputBuffer: React.Dispatch<React.SetStateAction<string>>;
  setAutoCompleteListLength: React.Dispatch<React.SetStateAction<number>>;
  handleBlur: () => void;
}

const AutoCompleteList = forwardRef<{ selectPointedItem: () => void }, Props>(
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
        selectPointedItem,
      };
    });

    const changeUrl = (url: string) => {
      setInputBuffer(url);
      setUrl(url);
      setCurrentIndex(0);
    };

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
          if (githubRepoInfos.isDummy) return;

          setAutoCompleteList(githubRepoInfos.data);
          setAutoCompleteListLength(githubRepoInfos.data.length);

          break;

        case 'pullRequest':
          if (githubPrInfos.isDummy) return;

          setAutoCompleteList(githubPrInfos.data);
          setAutoCompleteListLength(githubPrInfos.data.length);

          break;

        case 'complete':
        default:
          break;
      }
    }, [url, myGithubInfo]);

    useEffect(() => {
      if (currentIndex === 0) {
        setUrl(inputBuffer);

        return;
      }

      const newUrl = autoCompleteList[currentIndex - 1]?.url ?? '';
      setUrl(newUrl);
    }, [currentIndex]);

    const selectItem = (newUrl: string) => {
      const typingPart = typingGithubUrlPart(newUrl);

      switch (typingPart) {
        case 'userId':
          changeUrl(newUrl + '/');

          const newUserID = extractUserId(newUrl + '/') ?? '';
          setGithubId(newUserID);

          break;

        case 'repoName':
          changeUrl(newUrl + '/pull/');

          const newUserId = extractUserId(newUrl + '/pull/') ?? '';
          const newRepoName = extractRepoName(newUrl + '/pull/') ?? '';
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
        case 'domain':
          changeUrl('https://github.com/');

          break;
        case 'userId':
          changeUrl(url + '/');

          const newUserID = extractUserId(url + '/') ?? '';
          console.log(extractUserId(url + '/'));

          setGithubId(newUserID);

          break;

        case 'repoName':
          changeUrl(url + '/pull/');

          const newRepoName = extractRepoName(url + '/pull/') ?? '';
          const newUserId = extractUserId(url + '/pull/') ?? '';
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
