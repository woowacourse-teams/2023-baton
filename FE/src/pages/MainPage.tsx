import RunnerPostList from '@components/RunnerPost/RunnerPostList/RunnerPostList';
import Button from '@components/common/Button';
import Tag from '@components/common/Tag';
import { usePageRouter } from '@hooks/usePageRouter';
import Layout from '@layout/Layout';
import React from 'react';
import { styled } from 'styled-components';

const MainPage = () => {
  const { goToRunnerPostCreatePage } = usePageRouter();

  return (
    <Layout>
      <S.TitleWrapper>
        <S.Title>서포터를 찾고있어요 👀</S.Title>
      </S.TitleWrapper>
      <S.ControlPanelContainer>
        <S.LeftSideContainer>
          <S.FormContainer>
            <S.SearchLabel htmlFor="search-tag">#tags</S.SearchLabel>
            <S.SearchInput id="search-tag" type="text" placeholder="태그명 검색 (최대 5개 설정 가능)" />
          </S.FormContainer>
          <S.TagContainer>
            <Tag>자바</Tag>
            <Tag>javascript</Tag>
            <Tag>react</Tag>
          </S.TagContainer>
        </S.LeftSideContainer>

        <Button onClick={goToRunnerPostCreatePage} colorTheme="WHITE" fontSize="18px">
          리뷰 요청 글 작성하기
        </Button>
      </S.ControlPanelContainer>
      <S.RunnerPostWrapper>
        <RunnerPostList />
      </S.RunnerPostWrapper>
    </Layout>
  );
};

export default MainPage;

const S = {
  TitleWrapper: styled.div`
    margin: 72px 0 53px 0;
  `,
  Title: styled.p`
    font-size: 36px;
    font-weight: 700;
  `,
  ControlPanelContainer: styled.div`
    display: flex;
    justify-content: space-between;
    align-items: end;

    margin-bottom: 36px;
  `,

  LeftSideContainer: styled.div`
    display: flex;
    align-items: end;
    gap: 20px;
  `,

  FormContainer: styled.form`
    display: flex;
    flex-direction: column;
    align-items: start;
  `,

  SearchInput: styled.input`
    width: 263px;
    height: 40px;
    padding: 10px 10px;

    font-size: 14px;

    border-radius: 6px;
    border: 1px solid var(--gray-500);
  `,

  SearchLabel: styled.label`
    margin-bottom: 12px;

    font-size: 18px;
  `,

  TagContainer: styled.div`
    display: flex;

    margin-bottom: 3px;
    gap: 10px;
  `,

  RunnerPostWrapper: styled.div``,
};
