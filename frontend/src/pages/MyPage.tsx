import ListFilter from '@/components/ListFilter/ListFilter';
import TechLabel from '@/components/TechLabel/TechLabel';
import Avatar from '@/components/common/Avatar/Avatar';
import Button from '@/components/common/Button/Button';
import { useToken } from '@/hooks/useToken';
import Layout from '@/layout/Layout';
import { GetMyPagePostResponse, GetMyPageProfileResponse, MyPagePost } from '@/types/myPage';
import React, { useContext, useEffect, useState } from 'react';
import styled from 'styled-components';
import githubIcon from '@/assets/github-icon.svg';
import MyPagePostList from '@/components/MyPage/MyPagePostList/MyPagePostList';
import { getRequest } from '@/api/fetch';
import { PostOptions, runnerPostOptions, supporterPostOptions } from '@/utils/postOption';
import { ToastContext } from '@/contexts/ToastContext';
import { ERROR_TITLE } from '@/constants/message';
import { usePageRouter } from '@/hooks/usePageRouter';

const MyPage = () => {
  const [myPageProfile, setMyPageProfile] = useState<GetMyPageProfileResponse | null>(null);
  const [myPagePostList, setMyPagePostList] = useState<MyPagePost[]>([]);
  const [postOptions, setPostOptions] = useState<PostOptions>(runnerPostOptions);
  const [isRunner, setIsRunner] = useState(true);

  const { getToken } = useToken();
  const { goToProfileEditPage } = usePageRouter();
  const { showErrorToast } = useContext(ToastContext);

  useEffect(() => {
    setPostOptions(isRunner ? runnerPostOptions : supporterPostOptions);
  }, [isRunner]);

  useEffect(() => {
    const fetchMyPageData = async (role: 'runner' | 'supporter') => {
      getProfile(role);
      getPostList(role);
    };

    fetchMyPageData(isRunner ? 'runner' : 'supporter');
  }, [isRunner, postOptions]);

  const getProfile = (role: 'runner' | 'supporter') => {
    const token = getToken()?.value;
    if (!token) return;

    getRequest(`/profile/${role}/me`, token)
      .then(async (response) => {
        const data: GetMyPageProfileResponse = await response.json();

        setMyPageProfile(data);
      })
      .catch((error: Error) => showErrorToast({ title: ERROR_TITLE.REQUEST, description: error.message }));
  };

  const getPostList = (role: 'runner' | 'supporter') => {
    const token = getToken()?.value;
    if (!token) return;

    const selectedPostOption = postOptions.filter((option) => option.selected)[0].value;

    const rolePath =
      role === 'runner'
        ? `runner/me/runner?reviewStatus=${selectedPostOption}`
        : `runner/me/supporter?reviewStatus=${selectedPostOption}`;

    getRequest(`/posts/${rolePath}`, token)
      .then(async (response) => {
        const data: GetMyPagePostResponse = await response.json();

        setMyPagePostList(data.data);
      })
      .catch((error: Error) => showErrorToast({ title: ERROR_TITLE.REQUEST, description: error.message }));
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

  const handleClickSupporterButton = () => {
    setIsRunner(!isRunner);
  };

  return (
    <Layout>
      <S.TitleContainer>
        <S.Title>마이페이지</S.Title>
        <S.ButtonContainer>
          <S.RunnerSupporterButton $isSelected={isRunner} onClick={handleClickSupporterButton}>
            러너
          </S.RunnerSupporterButton>
          <S.RunnerSupporterButton $isSelected={!isRunner} onClick={handleClickSupporterButton}>
            서포터
          </S.RunnerSupporterButton>
        </S.ButtonContainer>
      </S.TitleContainer>
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
        <Button
          width="80px"
          height="35px"
          colorTheme="WHITE"
          fontSize="12px"
          fontWeight={700}
          onClick={goToProfileEditPage}
        >
          프로필 수정
        </Button>
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
        <MyPagePostList filteredPostList={myPagePostList} isRunner={isRunner} />
      </S.PostsContainer>
    </Layout>
  );
};

export default MyPage;

const S = {
  TitleContainer: styled.div`
    display: flex;
    justify-content: space-between;
    align-items: end;
  `,

  Title: styled.h1`
    font-size: 36px;
    font-weight: 700;
  `,
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
    justify-content: space-between;
    gap: 10px;

    margin-top: 50px;
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
    width: 75%;

    font-size: 18px;
    line-height: 1.5;

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
