import React, { forwardRef, useEffect, useImperativeHandle, useMemo, useState } from 'react';
import styled from 'styled-components';
import AutoCompleteItem from './AutoCompleteItem';
import { useMyGithubInfo } from '@/hooks/query/useMyGithubInfo';
import { useGithubRepoList } from '@/hooks/query/github/useGithubRepoList';
import { useGithubPrList } from '@/hooks/query/github/useGithubPrList';
import {
  checkTypingPullRequestUrl,
  checkTypingRepositoryUrl,
  extractPrNumber,
  extractRepoName,
  extractUserId,
  typingGithubUrlPart,
} from '@/utils/githubUrl';

interface Props {
  url: string;
  setUrl: (url: string) => void;
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

    const [explanation, setExplanation] = useState('');

    const [githubId, setGithubId] = useState(extractUserId(url) ?? '');
    const [githubRepoName, setGithubRepoName] = useState(extractRepoName(url) ?? '');

    const { data: myGithubInfo } = useMyGithubInfo();
    const { data: githubRepoInfos } = useGithubRepoList(githubId, '', checkTypingRepositoryUrl(url));
    const { data: githubPrInfos } = useGithubPrList(githubId, githubRepoName, checkTypingPullRequestUrl(url));

    const filterAutoCompleteList = (list: { title: string; url: string }[]) => {
      const maxLength = 5;
      const typingPart = typingGithubUrlPart(inputBuffer);

      switch (typingPart) {
        case 'repoName':
          const repoName = extractRepoName(url) ?? '';

          const includedInputRepoNameList = list.filter((item) => {
            const urlRepoName = extractRepoName(item.url) ?? '';
            return urlRepoName.includes(repoName);
          });

          return includedInputRepoNameList.slice(0, maxLength);

        case 'complete':
        case 'pullRequest':
          const prNumber = extractPrNumber(url) ?? '';

          const includedInputPrNumberList = list.filter((item) => {
            const urlPrNumber = extractPrNumber(item.url) ?? '';
            return urlPrNumber.includes(prNumber);
          });

          return includedInputPrNumberList.slice(0, maxLength);

        default:
          return autoCompleteList.slice(0, maxLength);
      }
    };

    const filteredAutoCompleteList = useMemo<{ title: string; url: string }[]>(() => {
      return filterAutoCompleteList(autoCompleteList);
    }, [inputBuffer, autoCompleteList]);

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

    const updateListToMyGithub = () => {
      if (!myGithubInfo) return;

      const myGithubList = [{ title: '내 저장소', url: myGithubInfo.githubUrl }];

      setAutoCompleteList(myGithubList);
      setAutoCompleteListLength(1);
    };

    useEffect(() => {
      setAutoCompleteListLength(filteredAutoCompleteList.length);
    }, [filteredAutoCompleteList]);

    useEffect(() => {
      const typingPart = typingGithubUrlPart(url);

      switch (typingPart) {
        case 'userId':
          updateListToMyGithub();

          const userId = extractUserId(url);
          setExplanation(`유저 이름을 입력하는 중이에요 ${userId ? `( ${userId} )` : ''}`);

          break;

        case 'domain':
          updateListToMyGithub();

          setExplanation('깃허브 도메인이 잘못되었어요');

          break;

        case 'repoName':
          if (githubRepoInfos.isDummy) return;

          setAutoCompleteList(githubRepoInfos.data);

          const repoName = extractRepoName(url);
          setExplanation(`저장소 이름을 입력하는 중이에요 ${repoName ? `( ${repoName} )` : ''}`);

          break;

        case 'pullRequest':
          if (githubPrInfos.isDummy) return;

          setAutoCompleteList(githubPrInfos.data);

          setExplanation(`PR 순서를 입력하는 중이에요`);

          break;

        case 'complete':
          const prNumber = extractPrNumber(url);
          setExplanation(`PR 순서를 입력하는 중이에요 ${prNumber ? `( ${prNumber} )` : ''}`);
          break;

        default:
          setExplanation('잘못된 도메인이에요');

          break;
      }
    }, [url, myGithubInfo]);

    useEffect(() => {
      if (currentIndex === 0) {
        if (inputBuffer) setUrl(inputBuffer);

        return;
      }

      const newUrl = filteredAutoCompleteList[currentIndex - 1]?.url ?? '';
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
        <S.InputUnderLine />
        <S.Explanation>{explanation}</S.Explanation>
        {filteredAutoCompleteList.map((item, i) => {
          const isPointed = i + 1 === currentIndex;
          return (
            <AutoCompleteItem
              key={item.url}
              title={item.title}
              url={item.url}
              selectItem={selectItem}
              isPointed={isPointed}
            />
          );
        })}
        {filteredAutoCompleteList.length > 0 && <S.ListEndSpace />}
      </>
    );
  },
);

export default AutoCompleteList;

const S = {
  InputUnderLine: styled.div`
    position: relative;

    border-top: 1px solid var(--gray-300);
    height: 20px;
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
    height: 25px;
    width: 100%;
    border-radius: 5px;
  `,

  Explanation: styled.div`
    width: 100%;
    padding: 0 30px;

    font-size: 12px;
    margin-bottom: 16px;

    color: var(--gray-500);
  `,
};
