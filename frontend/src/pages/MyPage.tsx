import ListFilter from '@/components/ListFilter/ListFilter';
import TechLabel from '@/components/TechLabel/TechLabel';
import Avatar from '@/components/common/Avatar/Avatar';
import Button from '@/components/common/Button/Button';
import { useToken } from '@/hooks/useToken';
import Layout from '@/layout/Layout';
import { GetMyPagePostResponse, GetMyPageProfileResponse } from '@/types/myPage';
import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import githubIcon from '@/assets/github-icon.svg';
import MyPagePostList from '@/components/MyPage/MyPagePostList/MyPagePostList';
import { getRequest } from '@/api/fetch';
import { PostOptions, runnerPostOptions, supporterPostOptions } from '@/utils/postOption';

const MyPage = () => {
  const [myPageProfile, setMyPageProfile] = useState<GetMyPageProfileResponse | null>(null);
  const [myPagePostList, setMyPagePostList] = useState<GetMyPagePostResponse | null>(null);
  const [postOptions, setPostOptions] = useState<PostOptions>(runnerPostOptions);
  const [isRunner, setIsRunner] = useState(true);

  const { getToken } = useToken();

  useEffect(() => {
    setPostOptions(isRunner ? runnerPostOptions : supporterPostOptions);
  }, [isRunner]);

  useEffect(() => {
    const fetchMyPageData = async (role: 'runner' | 'supporter') => {
      getProfile(role);
      getPostList(role);
    };

    fetchMyPageData(isRunner ? 'runner' : 'supporter');
  }, [isRunner]);

  const getProfile = (role: 'runner' | 'supporter') => {
    const token = getToken()?.value;
    if (!token) return;

    getRequest(`/profile/${role}/me`, `Bearer ${token}`).then(async (response) => {
      const data: GetMyPageProfileResponse = await response.json();

      setMyPageProfile(data);
    });
  };

  const getPostList = (role: 'runner' | 'supporter') => {
    const token = getToken()?.value;
    if (!token) return;

    const rolePath = role === 'runner' ? 'runner/me/runner' : 'runner/me/supporter';
    getRequest(`/posts/${rolePath}`, `Bearer ${token}`).then(async (response) => {
      const data: GetMyPagePostResponse = await response.json();

      setMyPagePostList(data);
    });
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

  const filterList = (postList: GetMyPagePostResponse | null, options: PostOptions) => {
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
