import InputBox from '@/components/InputBox';
import TagInput from '@/components/TagInput';
import TextArea from '@/components/Textarea';
import Button from '@/components/common/Button';
import { usePageRouter } from '@/hooks/usePageRouter';
import Layout from '@/layout/Layout';
import React, { useState } from 'react';
import { styled } from 'styled-components';
import { BATON_BASE_URL } from '@/constants';
import { CreateRunnerPostRequest } from '@/types/runnerPost';
import { useToken } from '@/hooks/useToken';

const RunnerPostCreatePage = () => {
  const { goBack, goToCreationResultPage } = usePageRouter();
  const { getToken } = useToken();

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

    submitForm();
  };

  const validateInputs = () => {
    if (!title) throw new Error('제목을 입력해주세요');
    if (!pullRequestUrl) throw new Error('PR주소를 입력해주세요');

    const isDeadlineValidate = deadline.split('T').every((item) => item && item.length > 1);
    if (!isDeadlineValidate) throw new Error("마감기한의 '날짜'과 '시간' 모두 입력해주세요");
  };

  const postRunnerForm = async (data: CreateRunnerPostRequest) => {
    const token = getToken()?.value;
    const body = JSON.stringify(data);

    if (!token) {
      throw new Error('토큰이 존재하지 않습니다');
    }

    const response = await fetch(`${BATON_BASE_URL}/posts/runner/test`, {
      method: 'POST',
      body,
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${token}`,
      },
    });

    if (response.status !== 201) throw new Error(`${response.status} ERROR`);
  };

  const submitForm = async () => {
    const postData: CreateRunnerPostRequest = {
      tags,
      title,
      pullRequestUrl,
      deadline,
      contents,
    };

    try {
      await postRunnerForm(postData);
    } catch (error) {
      return alert(error);
    }

    goToCreationResultPage();
  };

  return (
    <Layout>
      <S.FormContainer>
        <S.Form>
          <S.InputContainer>
            <InputBox
              inputTextState={title}
              handleInputTextState={changeTitle}
              maxLength={30}
              width="800px"
              fontSize="38px"
              fontWeight="700"
              placeholder="제목을 입력해주세요"
              autoFocus={true}
            />
          </S.InputContainer>
          <S.InputContainer>
            <TagInput tags={tags} pushTag={pushTag} popTag={removeTag} width="100%" />
          </S.InputContainer>

          <S.InputContainer>
            <InputBox
              inputTextState={pullRequestUrl}
              handleInputTextState={changePullRequestUrl}
              width="500px"
              placeholder="코드 리뷰받을 PR 주소를 입력해주세요"
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
            height="340px"
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
    margin: 40px 0 50px 0;

    color: var(--gray-800);
    font-size: 32px;
    font-weight: 700;
  `,

  FormContainer: styled.div`
    display: flex;
    flex-direction: column;
    gap: 20px;

    padding: 0 20px;
  `,

  InputContainer: styled.div`
    display: flex;
    justify-content: start;
  `,

  Form: styled.form`
    display: flex;
    flex-direction: column;
    gap: 30px;

    &:first-child {
      margin-top: 60px;
    }
  `,

  InputName: styled.div`
    display: flex;
    align-items: center;

    height: 36px;
    margin-right: 20px;

    color: var(--gray-800);
    font-size: 18px;
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
    margin-right: 40px;

    &:focus {
      outline: 0;
    }
  `,

  ButtonContainer: styled.div`
    display: flex;
    justify-content: center;
    gap: 20px;

    margin: 50px 0;
  `,

  SelectedSupporter: styled.div`
    display: flex;
    align-items: end;
    gap: 30px;
  `,
};
