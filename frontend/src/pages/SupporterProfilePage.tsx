import TechLabel from '@/components/TechLabel/TechLabel';
import Avatar from '@/components/common/Avatar/Avatar';
import Button from '@/components/common/Button/Button';
import Layout from '@/layout/Layout';
import React from 'react';
import { useParams } from 'react-router-dom';
import { styled } from 'styled-components';
import githubIcon from '@/assets/github-icon.svg';
import RunnerPostItem from '@/components/RunnerPost/RunnerPostItem/RunnerPostItem';
import useViewport from '@/hooks/useViewport';
import { useOtherSupporterProfile } from '@/hooks/query/useOtherSupporterProfile';
import { useOtherSupporterPost } from '@/hooks/query/useOtherSupporterPost';

/*
 * api에 페이지네이션 도입 후 수정 필요
 */
const SupporterProfilePage = () => {
  const { supporterId } = useParams();

  const { isMobile } = useViewport();

  const { data: supporterProfile } = useOtherSupporterProfile(Number(supporterId));
  const { data: supporterProfilePost, hasNextPage, fetchNextPage } = useOtherSupporterPost(Number(supporterId));

  const handleClickMoreButton = () => {
    fetchNextPage();
  };
  return (
    <Layout>
      <S.ProfileContainer>
        <S.InfoContainer>
          <Avatar
            imageUrl={supporterProfile?.imageUrl || 'https://via.placeholder.com/150'}
            width="100px"
            height="100px"
          />
          <S.InfoDetailContainer>
            <S.Name>{supporterProfile?.name}</S.Name>
            <S.Company>{supporterProfile?.company}</S.Company>
            <S.TechLabel>
              {supporterProfile?.technicalTags.map((tag) => (
                <TechLabel key={tag} tag={tag} />
              ))}
            </S.TechLabel>
          </S.InfoDetailContainer>
        </S.InfoContainer>
      </S.ProfileContainer>

      <S.IntroductionContainer>
        <S.Introduction>{supporterProfile?.introduction}</S.Introduction>
        <S.GithubButtonWrapper>
          <Button fontSize={isMobile ? '16px' : '20px'} width="127px" height="43px" colorTheme="BLACK" fontWeight={700}>
            <S.Anchor href={supporterProfile?.githubUrl} target="_blank">
              <img src={githubIcon} />
              <S.GoToGitHub>Github</S.GoToGitHub>
            </S.Anchor>
          </Button>
        </S.GithubButtonWrapper>
      </S.IntroductionContainer>

      <S.ReviewCountWrapper>
        <S.ReviewCountTitle>완료된 리뷰</S.ReviewCountTitle>
        <S.ReviewCount>{supporterProfilePost?.length}</S.ReviewCount>
      </S.ReviewCountWrapper>
      <S.PostsContainer>
        {supporterProfilePost?.map((runnerPostData) => (
          <RunnerPostItem key={runnerPostData.runnerPostId} runnerPostData={runnerPostData} />
        ))}
      </S.PostsContainer>
      <S.MoreButtonWrapper>
        {hasNextPage && (
          <Button
            colorTheme="RED"
            width={isMobile ? '100%' : '1200px'}
            fontSize={isMobile ? '14px' : '18px'}
            height="55px"
            onClick={handleClickMoreButton}
          >
            더보기
          </Button>
        )}
      </S.MoreButtonWrapper>
    </Layout>
  );
};

export default SupporterProfilePage;

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
    margin-bottom: 80px;

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

  GithubButtonWrapper: styled.div`
    margin-left: auto;

    @media (max-width: 768px) {
      padding: 20px 0;
    }
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
    gap: 30px;

    @media (min-width: 768px) {
      padding: 0 20px;
    }
  `,

  ReviewCountWrapper: styled.div`
    display: flex;
    align-items: center;
    margin: 0 0 40px 20px;

    @media (max-width: 768px) {
      margin: 0 0 25px 5px;
    }
  `,

  ReviewCountTitle: styled.span`
    font-size: 30px;
    margin-right: 15px;

    @media (max-width: 768px) {
      font-size: 22px;
    }
  `,

  ReviewCount: styled.span`
    font-size: 40px;
    font-weight: 700;

    @media (max-width: 768px) {
      font-size: 28px;
    }
  `,

  MoreButtonWrapper: styled.div`
    max-width: 1200px;
    min-width: 340px;
    width: 100%;
    margin-top: 30px;
    margin-bottom: 20px;
  `,
};
