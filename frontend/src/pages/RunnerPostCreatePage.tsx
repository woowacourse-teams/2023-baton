import InputBox from '@/components/InputBox';
import TagInput from '@/components/TagInput';
import TextArea from '@/components/Textarea';
import Button from '@/components/common/Button';
import { usePageRouter } from '@/hooks/usePageRouter';
import Layout from '@/layout/Layout';
import React, { useState } from 'react';
import { styled } from 'styled-components';

const RunnerPostCreatePage = () => {
  const { goBack, goToSupporterSelectPage } = usePageRouter();

  const [tags, setTags] = useState<string[]>([]);
  const [title, setTitle] = useState<string>('');
  const [pullRequestUrl, setPullRequestUrl] = useState<string>('');
  const [deadline, setDeadline] = useState<string>('');
  const [contents, setContents] = useState<string>('');

  const pushTag = (newTag: string) => {
    if (newTag.length > 15) return alert('태그명은 15자 이내로 입력해주세요.');
    if (tags.length >= 5) return alert('입력할 수 있는 태그는 최대 5개입니다.');
    if (tags.includes(newTag)) return alert('중복된 태그는 입력 불가합니다.');

    setTags((current) => [...current, newTag]);
  };

  const removeTag = (tag?: string) => {
    if (!tag) {
      setTags((current) => current.slice(0, -1));
      return;
    }

    const newTags = tags.filter((item) => item !== tag);
    setTags(newTags);
  };

  const changeTitle = (e: React.ChangeEvent<HTMLInputElement>) => {
    setTitle(e.target.value);
  };

  const changePullRequestUrl = (e: React.ChangeEvent<HTMLInputElement>) => {
    setPullRequestUrl(e.target.value);
  };

  const changeDeadlineDate = (e: React.ChangeEvent<HTMLInputElement>) => {
    const newDate = e.target.value;
    const time = deadline.split('T')[1] ?? '';
    const newDeadline = `${newDate}T${time}`;
    setDeadline(newDeadline);
  };

  const changeDeadlineTime = (e: React.ChangeEvent<HTMLInputElement>) => {
    const newTime = e.target.value;
    const date = deadline.split('T')[0] ?? '';
    const newDeadline = `${date}T${newTime}`;
    setDeadline(newDeadline);
  };

  const changeContents = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    setContents(e.target.value);
  };

  const cancelPostWrite = (e: React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();

    goBack();
  };

  const goToNextForm = () => {
    try {
      validateInputs();
    } catch (error) {
      return alert(error);
    }

    goToSupporterSelectPage({ tags, title, pullRequestUrl, deadline, contents });
  };

  const validateInputs = () => {
    if (!title) throw new Error('제목을 입력해주세요');
    if (!pullRequestUrl) throw new Error('PR주소를 입력해주세요');

    const isDeadlineValidate = deadline.split('T').every((item) => item && item.length > 1);
    if (!isDeadlineValidate) throw new Error("마감기한의 '날짜'과 '시간' 모두 입력해주세요");
  };

  return (
    <Layout>
      <S.Title>서포터를 찾고 있어요</S.Title>
      <S.FormContainer>
        <S.InputContainer>
          <S.InputName>태그</S.InputName>
          <TagInput tags={tags} pushTag={pushTag} popTag={removeTag} width={'800px'} />
        </S.InputContainer>
        <S.Form>
          <S.InputContainer>
            <S.InputName>제목</S.InputName>
            <InputBox
              inputTextState={title}
              handleInputTextState={changeTitle}
              maxLength={15}
              placeholder="제목을 입력하세요"
            />
          </S.InputContainer>
          <S.InputContainer>
            <S.InputName>PR주소</S.InputName>
            <InputBox
              inputTextState={pullRequestUrl}
              handleInputTextState={changePullRequestUrl}
              placeholder="PR 주소를 입력하세요"
            />
          </S.InputContainer>
          <S.InputContainer>
            <S.InputName>마감기한</S.InputName>
            <S.DeadlineContainer>
              <S.DeadlineDate type="date" onChange={changeDeadlineDate} />
              <S.DeadlineTime type="time" onChange={changeDeadlineTime} />
            </S.DeadlineContainer>
          </S.InputContainer>
          <TextArea
            inputTextState={contents}
            width="1200px"
            height="500px"
            maxLength={500}
            handleInputTextState={changeContents}
            placeholder="> 리뷰어가 작성된 코드의 의미를 파악할 수 있도록 내용을 작성해주시면 더 나은 리뷰가 될 수 있어요 :)"
          />
          <S.ButtonContainer>
            <Button type="button" onClick={cancelPostWrite} colorTheme="GRAY" fontWeight={700}>
              취소
            </Button>
            <Button type="button" colorTheme="WHITE" fontWeight={700} onClick={goToNextForm}>
              리뷰요청 글 생성
            </Button>
          </S.ButtonContainer>
        </S.Form>
      </S.FormContainer>
    </Layout>
  );
};

export default RunnerPostCreatePage;

const S = {
  Title: styled.div`
    margin: 50px 0;

    color: var(--gray-800);
    font-size: 32px;
    font-weight: 700;
  `,

  FormContainer: styled.div`
    display: flex;
    flex-direction: column;

    gap: 20px;
  `,

  InputContainer: styled.div`
    display: flex;
    justify-content: start;
  `,

  Form: styled.form`
    display: flex;
    flex-direction: column;

    gap: 20px;
  `,

  InputName: styled.div`
    display: flex;
    align-items: center;

    width: 100px;
    height: 36px;

    color: var(--gray-800);
    font-size: 15px;
    font-weight: 500px;
  `,

  DeadlineContainer: styled.div`
    display: flex;

    gap: 20px;
  `,

  DeadlineDate: styled.input`
    &:focus {
      outline: 0;
    }
  `,

  DeadlineTime: styled.input`
    &:focus {
      outline: 0;
    }
  `,

  ButtonContainer: styled.div`
    display: flex;
    justify-content: center;
    gap: 20px;

    margin-bottom: 100px;
  `,

  ModalChildrenContainer: styled.div`
    display: flex;
    flex-direction: column;

    height: 100%;
    padding: 10px;
    gap: 20px;
  `,

  ModalTitle: styled.div`
    display: flex;
    justify-content: center;
  `,

  DisClaimMessage: styled.div`
    line-height: 1.5;
    color: var(--gray-500);

    margin-bottom: 20px;
  `,
};
