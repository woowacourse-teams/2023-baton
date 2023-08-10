import ListFilter from '@/components/ListFilter/ListFilter';
import MyPageRunnerPostList from '@/components/MyPage/MyPageRunnerPostList/MyPageRunnerPostList';
import TechLabel from '@/components/TechLabel/TechLabel';
import Avatar from '@/components/common/Avatar/Avatar';
import Button from '@/components/common/Button/Button';
import { BATON_BASE_URL } from '@/constants';
import { useToken } from '@/hooks/useToken';
import Layout from '@/layout/Layout';
import { GetRunnerMyPageResponse, MyPageRunnerPost } from '@/types/myPage';
import { ReviewStatus } from '@/types/runnerPost';
import { SelectOption } from '@/types/select';
import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import githubIcon from '@/assets/github-icon.svg';

type ReviewPostOptions = SelectOption<ReviewStatus>[];

const reviewPostOptions: ReviewPostOptions = [
  {
    value: 'NOT_STARTED',
    label: '대기중인 리뷰',
    selected: true,
  },
  {
    value: 'IN_PROGRESS',
    label: '진행중인 리뷰',
    selected: false,
  },
  {
    value: 'DONE',
    label: '완료된 리뷰',
    selected: false,
  },
];

const MyPage = () => {
  const [runnerProfile, setRunnerProfile] = useState<GetRunnerMyPageResponse | null>(null);

  const [postOptions, setPostOptions] = useState<ReviewPostOptions>(reviewPostOptions);

  const [isRunner, setIsRunner] = useState(true);

  const token =
    'eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJiYXRvbiIsImlhdCI6MTY5MTY3MjUwNCwiZXhwIjoxNjk0MjY0NTA0LCJzb2NpYWxJZCI6Imd5ZW9uZ3phIn0.CWUC0Q9Qlw4oRC_CNm-aVKNNGYYUKuVplz16WdreFC8';

  const getRunnerProfile = async () => {
    try {
      if (!token) throw new Error('토큰이 존재하지 않습니다');

      const response = await fetch(`${BATON_BASE_URL}/profile/runner/me`, {
        method: 'GET',
        headers: { Authorization: `Bearer ${token}` },
      });

      if (!response.ok) {
        throw new Error(`Error: ${response.status}`);
      }

      const supporterCardList = await response.json();

      return supporterCardList;
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    const fetchRunnerPost = async () => {
      const result = await getRunnerProfile();

      setRunnerProfile(result);
    };

    fetchRunnerPost();
  }, []);

  const selectOptions = (value: string | number) => {
    const selectedOptionIndex = postOptions.findIndex((option) => option.value === value);
    if (selectedOptionIndex === -1) return;

    const newOptions = postOptions.map((option, index) => {
      if (index === selectedOptionIndex) return { ...option, selected: true };
      return { ...option, selected: false };
    });

    setPostOptions(newOptions);
  };

  const filterList = () => {
    const posts = runnerProfile?.runnerPosts || [];

    const selectedOption = postOptions.filter((option) => option.selected)[0];
    if (!selectedOption) return [];

    const filteredPosts = posts.filter((post) => post.reviewStatus === selectedOption.value);
    return filteredPosts;
  };

  const handleClickSupporterButton = () => {
    alert('준비중인 기능입니다');
  };

  return (
    <Layout>
      <S.ProfileContainer>
        <S.InfoContainer>
          <Avatar
            imageUrl={runnerProfile?.imageUrl || 'https://via.placeholder.com/150'}
            width="100px"
            height="100px"
          />
          <S.InfoDetailContainer>
            <S.Name>{runnerProfile?.name}</S.Name>
            <S.Company>{runnerProfile?.company}</S.Company>
            <S.TechLabel>
              {runnerProfile?.technicalTags.map((tag) => (
                <TechLabel tag={tag} />
              ))}
            </S.TechLabel>
          </S.InfoDetailContainer>
        </S.InfoContainer>
        <S.ButtonContainer>
          <S.RunnerSupporterButton $isSelected={isRunner}>러너</S.RunnerSupporterButton>
          <S.RunnerSupporterButton $isSelected={!isRunner} onClick={handleClickSupporterButton}>
            서포터
          </S.RunnerSupporterButton>
        </S.ButtonContainer>
      </S.ProfileContainer>

      <S.IntroductionContainer>
        <S.Introduction>{runnerProfile?.introduction}</S.Introduction>
        <Button width="127px" height="43px" colorTheme="BLACK" fontWeight={700}>
          <S.Anchor href={runnerProfile?.githubUrl} target="_blank">
            <img src={githubIcon} />
            <S.GoToGitHub>Github</S.GoToGitHub>
          </S.Anchor>
        </Button>
      </S.IntroductionContainer>

      <S.PostsContainer>
        <S.FilterWrapper>
          <ListFilter options={postOptions} selectOption={selectOptions} />
        </S.FilterWrapper>
        <MyPageRunnerPostList filterList={filterList} />
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
