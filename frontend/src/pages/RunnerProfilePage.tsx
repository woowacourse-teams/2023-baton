import TechLabel from '@/components/TechLabel/TechLabel';
import Avatar from '@/components/common/Avatar/Avatar';
import Button from '@/components/common/Button/Button';
import Layout from '@/layout/Layout';
import { GetRunnerProfileResponse } from '@/types/profile';
import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { styled } from 'styled-components';
import githubIcon from '@/assets/github-icon.svg';
import { useFetch } from '@/hooks/useFetch';

const RunnerProfilePage = () => {
  const [runnerProfile, setRunnerProfile] = useState<GetRunnerProfileResponse | null>(null);

  const { getRequest } = useFetch();

  const { runnerId } = useParams();

  useEffect(() => {
    getProfile();
  }, [runnerId]);

  const getProfile = async () => {
    getRequest(`/profile/runner/${runnerId}`, async (response) => {
      const data: GetRunnerProfileResponse = await response.json();

      setRunnerProfile(data);
    });
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
                <TechLabel key={tag} tag={tag} />
              ))}
            </S.TechLabel>
          </S.InfoDetailContainer>
        </S.InfoContainer>
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
    </Layout>
  );
};

export default RunnerProfilePage;

const S = {
  ProfileContainer: styled.div`
    display: flex;
    justify-content: space-between;
    align-items: flex-start;

    padding: 50px 0;

    @media (min-width: 768px) {
      padding: 50px 20px;
    }
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

    @media (max-width: 768px) {
      font-size: 22px;
    }
  `,

  Company: styled.div`
    font-size: 18px;

    @media (max-width: 768px) {
      font-size: 16px;
    }
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

    @media (max-width: 768px) {
      display: flex;
      flex-direction: column;
      align-items: start;
      gap: 40px;
    }
  `,

  Introduction: styled.div`
    position: relative;
    display: flex;
    flex-wrap: wrap;
    gap: 9px 7px;

    margin-left: 40px;
    width: 75%;

    &::before {
      position: absolute;
      content: '';

      left: -30px;
      height: 100%;
      width: 4.5px;
      border-radius: 2px;

      background-color: var(--gray-400);
    }

    font-size: 18px;
    line-height: 1.8;

    white-space: pre-line;

    @media (max-width: 768px) {
      width: calc(75% + 40px);

      font-size: 14px;
    }
  `,

  Anchor: styled.a`
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 10px;
  `,

  GoToGitHub: styled.p``,
};
