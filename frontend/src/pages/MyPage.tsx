import ListFilter from '@/components/ListFilter/ListFilter';
import TechLabel from '@/components/TechLabel/TechLabel';
import Avatar from '@/components/common/Avatar/Avatar';
import Button from '@/components/common/Button/Button';
import { BATON_BASE_URL } from '@/constants';
import { useToken } from '@/hooks/useToken';
import Layout from '@/layout/Layout';
import { GetMyPagePost, GetMyPageProfileResponse, MyPagePost } from '@/types/myPage';
import { ReviewStatus } from '@/types/runnerPost';
import { SelectOption } from '@/types/select';
import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import githubIcon from '@/assets/github-icon.svg';
import MyPagePostList from '@/components/MyPage/MyPagePostList/MyPagePostList';

export type PostOptions = SelectOption<ReviewStatus>[];

const createPostOptions = (labels: string[]): PostOptions => {
  return [
    {
      value: 'NOT_STARTED',
      label: labels[0],
      selected: true,
    },
    {
      value: 'IN_PROGRESS',
      label: labels[1],
      selected: false,
    },
    {
      value: 'DONE',
      label: labels[2],
      selected: false,
    },
  ];
};

const runnerPostLabels = ['대기중인 리뷰', '진행중인 리뷰', '완료된 리뷰'];
const supporterPostLabels = ['신청한 리뷰', '진행중인 리뷰', '완료된 리뷰'];

export const RUNNER_POST_OPTIONS = createPostOptions(runnerPostLabels);
export const SUPPORTER_POST_OPTIONS = createPostOptions(supporterPostLabels);

const MyPage = () => {
  const [myPageProfile, setMyPageProfile] = useState<GetMyPageProfileResponse | null>(null);
  const [myPagePostList, setMyPagePostList] = useState<GetMyPagePost | null>(null);
  const [postOptions, setPostOptions] = useState<PostOptions>(RUNNER_POST_OPTIONS);
  const [isRunner, setIsRunner] = useState(true);

  const { getToken } = useToken();
  const token = getToken();

  useEffect(() => {
    if (isRunner) {
      setPostOptions(RUNNER_POST_OPTIONS);
    } else setPostOptions(SUPPORTER_POST_OPTIONS);
  }, [isRunner]);

  useEffect(() => {
    if (isRunner) {
      const fetchRunnerMyPage = async () => {
        const profileResult = await getRunnerProfile();
        const postResult = await getRunnerPostList();

        setMyPageProfile(profileResult);
        setMyPagePostList(postResult);
      };

      fetchRunnerMyPage();
    } else {
      const fetchSupporterMyPage = async () => {
        const profileResult = await getSupporterProfile();
        const postResult = await getSupporterPostList();

        setMyPageProfile(profileResult);
        setMyPagePostList(postResult);
      };

      fetchSupporterMyPage();
    }
  }, [isRunner]);

  const getRunnerProfile = async () => {
    if (!token) throw new Error('토큰이 존재하지 않습니다');

    const response = await fetch(`${BATON_BASE_URL}/profile/runner/me`, {
      method: 'GET',
      headers: { Authorization: `Bearer ${token}` },
    });

    const data = await response.json();

    return data;
  };

  const getRunnerPostList = async () => {
    if (!token) throw new Error('토큰이 존재하지 않습니다');

    const response = await fetch(`${BATON_BASE_URL}/posts/runner/me/runner`, {
      method: 'GET',
      headers: { Authorization: `Bearer ${token}` },
    });

    const data = await response.json();

    return data;
  };

  const getSupporterProfile = async () => {
    if (!token) throw new Error('토큰이 존재하지 않습니다');

    const response = await fetch(`${BATON_BASE_URL}/profile/supporter/me`, {
      method: 'GET',
      headers: { Authorization: `Bearer ${token}` },
    });

    const data = await response.json();

    return data;
  };

  const getSupporterPostList = async () => {
    if (!token) throw new Error('토큰이 존재하지 않습니다');

    const response = await fetch(`${BATON_BASE_URL}/posts/runner/me/supporter`, {
      method: 'GET',
      headers: { Authorization: `Bearer ${token}` },
    });

    const data = await response.json();

    return data;
  };

  const selectOptions = (value: string | number) => {
    const selectedOptionIndex = postOptions.findIndex((option) => option.value === value);
    if (selectedOptionIndex === -1) return;

    const newOptions = postOptions.map((option, index) => {
      if (index === selectedOptionIndex) return { ...option, selected: true };
      return { ...option, selected: false };
    });

    setPostOptions(newOptions);
  };

  const filterList = (postList: GetMyPagePost | null, options: PostOptions) => {
    const posts = postList?.data || [];

    const selectedOption = options.filter((option) => option.selected)[0];
    if (!selectedOption) return [];

    const filteredPosts = posts.filter((post) => post.reviewStatus === selectedOption.value);

    return filteredPosts;
  };

  const handleClickSupporterButton = () => {
    setIsRunner(!isRunner);
  };

  return (
    <Layout>
      <S.ProfileContainer>
        <S.InfoContainer>
          <Avatar
            imageUrl={myPageProfile?.imageUrl || 'https://via.placeholder.com/150'}
            width="100px"
            height="100px"
          />
          <S.InfoDetailContainer>
            <S.Name>{myPageProfile?.name}</S.Name>
            <S.Company>{myPageProfile?.company}</S.Company>
            <S.TechLabel>
              {myPageProfile?.technicalTags.map((tag) => (
                <TechLabel key={tag} tag={tag} />
              ))}
            </S.TechLabel>
          </S.InfoDetailContainer>
        </S.InfoContainer>
        <S.ButtonContainer>
          <S.RunnerSupporterButton $isSelected={isRunner} onClick={handleClickSupporterButton}>
            러너
          </S.RunnerSupporterButton>
          <S.RunnerSupporterButton $isSelected={!isRunner} onClick={handleClickSupporterButton}>
            서포터
          </S.RunnerSupporterButton>
        </S.ButtonContainer>
      </S.ProfileContainer>

      <S.IntroductionContainer>
        <S.Introduction>{myPageProfile?.introduction}</S.Introduction>
        <Button width="127px" height="43px" colorTheme="BLACK" fontWeight={700}>
          <S.Anchor href={myPageProfile?.githubUrl} target="_blank">
            <img src={githubIcon} />
            <S.GoToGitHub>Github</S.GoToGitHub>
          </S.Anchor>
        </Button>
      </S.IntroductionContainer>

      <S.PostsContainer>
        <S.FilterWrapper>
          <ListFilter options={postOptions} selectOption={selectOptions} />
        </S.FilterWrapper>
        <MyPagePostList filterList={() => filterList(myPagePostList, postOptions)} isRunner={isRunner} />
      </S.PostsContainer>
    </Layout>
  );
};

export default MyPage;

const S = {
  ProfileContainer: styled.div`
    display: flex;
    justify-content: space-between;
    align-items: flex-start;

    padding: 50px 0;
  `,

  InfoContainer: styled.div`
    display: flex;
    align-items: center;

    gap: 20px;
  `,

  InfoDetailContainer: styled.div`
    display: flex;
    flex-direction: column;
    gap: 10px;
  `,

  Name: styled.div`
    font-size: 26px;
    font-weight: 700;
  `,

  Company: styled.div`
    font-size: 18px;
  `,
  TechLabel: styled.div`
    display: flex;
    gap: 8px;
  `,

  ButtonContainer: styled.div`
    display: flex;
    align-items: center;
    gap: 10px;
  `,

  RunnerSupporterButton: styled.button<{ $isSelected: boolean }>`
    display: flex;
    justify-content: center;
    align-items: center;

    width: 95px;
    height: 38px;
    border-radius: 18px;
    border: 1px solid ${({ $isSelected }) => ($isSelected ? 'white' : 'var(--baton-red)')};

    background-color: ${({ $isSelected }) => ($isSelected ? 'var(--baton-red)' : 'white')};

    color: ${({ $isSelected }) => ($isSelected ? 'white' : 'var(--baton-red)')};
  `,

  IntroductionContainer: styled.div`
    display: flex;
    justify-content: space-between;

    padding: 0 10px;
    margin-bottom: 50px;

    border-left: 3px solid var(--gray-600);
  `,

  Introduction: styled.div`
    width: 700px;

    font-size: 18px;

    white-space: no-wrap;
  `,

  Anchor: styled.a`
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 10px;
  `,

  GoToGitHub: styled.p``,

  PostsContainer: styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
  `,

  FilterWrapper: styled.div`
    padding: 70px 20px;
  `,
};
