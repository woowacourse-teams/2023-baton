import InputBox from '@components/InputBox';
import TagInput from '@components/TagInput';
import TextArea from '@components/Textarea';
import Button from '@components/common/Button';
import Modal from '@components/common/Modal';
import Layout from '@layout/Layout';
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { styled } from 'styled-components';
import { validate } from 'webpack';

interface RunnerPostCreateType {
  tags: string[];
  title: string;
  pullRequestUrl: string;
  deadline: string;
  contents: string;
}

const RunnerPostCreatePage = () => {
  const navigate = useNavigate();

  const [tags, setTags] = useState<string[]>([]);
  const [title, setTitle] = useState<string>('');
  const [pullRequestUrl, setPullRequestUrl] = useState<string>('');
  const [deadline, setDeadline] = useState<string>('');
  const [contents, setContents] = useState<string>('');

  const [isOpenModal, setIsOpenModal] = useState(false);

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

  const onChangeTitle = (e: React.ChangeEvent<HTMLInputElement>) => {
    setTitle(e.target.value);
  };

  const onChangepullRequestUrl = (e: React.ChangeEvent<HTMLInputElement>) => {
    setPullRequestUrl(e.target.value);
  };

  const onChangeDeadlineDate = (e: React.ChangeEvent<HTMLInputElement>) => {
    const newDate = e.target.value;
    const time = deadline.split('T')[1] ?? '';
    const newDeadline = `${newDate}T${time}`;
    setDeadline(newDeadline);
  };

  const onChangeDeadlineTime = (e: React.ChangeEvent<HTMLInputElement>) => {
    const newTime = e.target.value;
    const date = deadline.split('T')[0] ?? '';
    const newDeadline = `${date}T${newTime}`;
    setDeadline(newDeadline);
  };

  const onChangeContents = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    setContents(e.target.value);
  };

  const toggleModal = () => {
    setIsOpenModal((current) => !current);
  };

  const cancelPostWrite = (e: React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();

    navigate(-1);
  };

  const validateInputs = () => {
    if (!title) throw new Error('제목을 입력해주세요');
    if (!pullRequestUrl) throw new Error('PR주소를 입력해주세요');

    const isDeadlineValidate = deadline.split('T').every((item) => item && item.length > 1);
    if (!isDeadlineValidate) throw new Error("마감기한의 '날짜'과 '시간' 모두 입력해주세요");
  };

  const postRunnerForm = async (data: RunnerPostCreateType) => {
    const body = JSON.stringify(data);
    const response = await fetch(`msw/posts/runner`, {
      method: 'POST',
      body,
      headers: {
        'Content-Type': 'application/json',
      },
    });
    if (response.status !== 201) throw new Error(`${response.status} ERROR`);
  };

  const submitForm = async (e: React.MouseEvent<HTMLButtonElement>) => {
    try {
      validateInputs();
      await postRunnerForm({ tags, title, pullRequestUrl, deadline, contents });
    } catch (e) {
      return alert(e);
    }

    navigate('/');
  };

  return (
    <Layout>
      <S.Title>서포터를 찾고 있어요</S.Title>
      <S.FormContainer>
        <S.InputContainer>
          <S.InputName>태그</S.InputName>
          <TagInput tags={tags} pushTag={pushTag} popTag={removeTag} />
        </S.InputContainer>
        <S.Form>
          <S.InputContainer>
            <S.InputName>제목</S.InputName>
            <InputBox onChange={onChangeTitle} maxLength={15} placeholder="제목을 입력하세요" />
          </S.InputContainer>
          <S.InputContainer>
            <S.InputName>PR주소</S.InputName>
            <InputBox onChange={onChangepullRequestUrl} placeholder="PR 주소를 입력하세요" />
          </S.InputContainer>
          <S.InputContainer>
            <S.InputName>마감기한</S.InputName>
            <S.DeadlineContainer>
              <S.DeadlineDate type="date" onChange={onChangeDeadlineDate} />
              <S.DeadlineTime type="time" onChange={onChangeDeadlineTime} />
            </S.DeadlineContainer>
          </S.InputContainer>
          <TextArea
            inputText={contents}
            width="1200px"
            height="500px"
            maxLength={500}
            onChange={onChangeContents}
            placeholder="> 리뷰어가 작성된 코드의 의미를 파악할 수 있도록 내용을 작성해주시면 더 나은 리뷰가 될 수 있어요 :)"
          />
          <S.ButtonContainer>
            <Button type="button" onClick={cancelPostWrite} colorTheme="GRAY" fontWeight={700}>
              취소
            </Button>
            <Button type="button" colorTheme="WHITE" fontWeight={700} onClick={toggleModal}>
              리뷰요청 글 생성
            </Button>
          </S.ButtonContainer>
        </S.Form>
      </S.FormContainer>
      {isOpenModal && (
        <Modal closeModal={toggleModal}>
          <S.ModalChildrenContainer>
            <S.ModalTitle>알림</S.ModalTitle>
            <S.DisClaimMessage>
              운영자는 회원 상호 간 또는 회원과 제 3자 상호 간에 서비스를 매개로 하여 물품거래등을 한 경우에 그로부터
              발생하는 일체의 손해에 대하여 책임지지 아니합니다.
            </S.DisClaimMessage>
            <S.ButtonContainer>
              <Button type="button" colorTheme="WHITE" fontWeight={700} onClick={submitForm}>
                글 작성하기
              </Button>
            </S.ButtonContainer>
          </S.ModalChildrenContainer>
        </Modal>
      )}
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

    //임시
    padding-bottom: 50px;

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
