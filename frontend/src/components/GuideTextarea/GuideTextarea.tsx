import { DownArrowIcon, UpArrowIcon } from '@/assets/arrow-icon';
import React, { useState } from 'react';
import { css, keyframes, styled } from 'styled-components';
import Modal from '../common/Modal/Modal';
import useViewport from '@/hooks/useViewport';

interface Props extends React.HTMLProps<HTMLTextAreaElement> {
  inputTextState: string;
  handleInputTextState: (e: React.ChangeEvent<HTMLTextAreaElement>) => void;
  title: string;
  guideTexts?: readonly string[];
  isOptional?: boolean;
  isErrorOnSubmit?: boolean;
}

const ArrowIcon = (isOpen: boolean) => {
  return isOpen ? (
    <S.RequiredIcon aria-label="입력 창 닫기">
      <UpArrowIcon />
    </S.RequiredIcon>
  ) : (
    <S.RequiredIcon aria-label="입력 창 열기">
      <DownArrowIcon />
    </S.RequiredIcon>
  );
};

const GuideTextarea = ({
  inputTextState,
  handleInputTextState,
  maxLength,
  minLength = 20,
  title,
  guideTexts,
  isOptional = false,
  isErrorOnSubmit = false,
  ...rest
}: Props) => {
  const [isError, setIsError] = useState(isErrorOnSubmit);
  const [isTextareaOpen, setIsTextareaOpen] = useState(!isOptional);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [IsAnimated, setIsAnimated] = useState(false);

  const { isMobile } = useViewport();

  const handleBlur = () => {
    if (isOptional) return;

    if (minLength > inputTextState.length) {
      setIsError(true);
    }
  };

  const handleFocus = () => {
    setIsError(false);
  };

  const openTextarea = () => {
    setIsTextareaOpen(true);
  };

  const closeTextarea = () => {
    setIsTextareaOpen(false);
  };

  const toggleTextarea = () => {
    if (!isOptional) return;

    if (!IsAnimated) {
      setIsAnimated(true);
    }

    isTextareaOpen ? closeTextarea() : openTextarea();
  };

  const openModal = () => {
    setIsModalOpen(true);
  };

  const closeModal = () => {
    setIsModalOpen(false);
  };

  const handleKeyDownTitle = (e: React.KeyboardEvent<HTMLDivElement>) => {
    if (e.key === 'Enter') {
      toggleTextarea();
    }
  };

  const handleClickGuideButton = (e: React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();

    openModal();
  };

  return (
    <S.Container>
      <S.HeaderContainer>
        <S.TitleContainer
          onClick={toggleTextarea}
          onKeyDown={handleKeyDownTitle}
          $isOptional={isOptional}
          aria-label={title}
          tabIndex={isOptional ? 0 : -1}
        >
          <S.Title>{title}</S.Title>
          {isOptional ? ArrowIcon(isTextareaOpen) : <S.RequiredIcon>*</S.RequiredIcon>}
        </S.TitleContainer>
        {guideTexts && <S.GuideButton onClick={handleClickGuideButton}>예시</S.GuideButton>}{' '}
      </S.HeaderContainer>
      <S.TextareaContainer
        $isError={isError}
        $isOpen={isTextareaOpen}
        $isAnimated={IsAnimated}
        $isOptional={isOptional}
      >
        <S.Textarea
          {...rest}
          onFocus={handleFocus}
          onBlur={handleBlur}
          onChange={handleInputTextState}
          maxLength={maxLength}
          tabIndex={isTextareaOpen ? 0 : -1}
        />
        <S.InputConfig>
          {minLength && isError ? <S.ErrorMessage>{minLength}자 이상 입력해주세요</S.ErrorMessage> : <S.Empty />}
          {maxLength && (
            <S.InputTextLength $isError={isError}>
              {inputTextState?.length ?? 0} / {maxLength}
            </S.InputTextLength>
          )}
        </S.InputConfig>
      </S.TextareaContainer>
      {isModalOpen && (
        <Modal closeModal={closeModal} width={isMobile ? '90%' : '70%'} height="620px">
          <S.ModalContainer>
            <S.ModalTitle>{title}</S.ModalTitle>
            <S.ModalContents>
              {guideTexts?.map((text) => (
                <S.GuideText>{text}</S.GuideText>
              ))}
            </S.ModalContents>
            <S.ConfirmButton onClick={closeModal}>확인</S.ConfirmButton>
          </S.ModalContainer>
        </Modal>
      )}
    </S.Container>
  );
};

export default GuideTextarea;

const open = keyframes`
 0% {
    padding: 0 20px;
    border: 1px solid var(--gray-300);
 }
 100% {
    height: 300px;
    padding: 8px 20px;
    border: 1px solid var(--gray-500);
}
`;

const close = keyframes`
 0% {
    height: 300px;
    padding: 8px 20px;
    order: 1px solid var(--gray-300);
 }
 100% {
    height: 0;
    padding: 0px 20px;
    border: 1px solid var(--gray-300);
}
`;

const getAnimation = (isOpen: boolean, isAnimated: boolean) => {
  if (!isAnimated) {
    return css`
      animation: none;
    `;
  }

  if (isOpen) {
    return css`
      animation: 0.3s ease-in ${open} both;
    `;
  }

  if (!isOpen) {
    return css`
      animation: 0.3s ease-in ${close} both;
    `;
  }
};

const S = {
  Container: styled.div`
    display: flex;
    justify-content: center;
    flex-direction: column;
    gap: 10px;
  `,

  HeaderContainer: styled.div`
    display: flex;
    justify-content: space-between;

    padding: 5px 10px;
  `,

  GuideButton: styled.button`
    display: flex;
    justify-content: center;
    align-items: center;

    width: 50px;
    height: 26px;
    padding: 0 10px;
    border: 1px solid var(--baton-red);
    border-radius: 12px;

    background-color: transparent;

    color: var(--baton-red);
    font-size: 14px;

    cursor: pointer;

    @media (max-width: 768px) {
      width: 45px;
      height: 23px;

      font-size: 12px;
    }
  `,

  TitleContainer: styled.div<{ $isOptional: boolean }>`
    display: flex;
    align-items: end;
    gap: 8px;

    cursor: ${({ $isOptional }) => ($isOptional ? 'pointer' : '')};

    @media (max-width: 768px) {
      gap: 4px;
    }
  `,

  Title: styled.div`
    font-size: 20px;
    font-weight: 700;

    @media (max-width: 768px) {
      font-size: 18px;

      height: 20px;
    }

    @media (max-width: 400px) {
      font-size: 16px;
    }
  `,

  RequiredIcon: styled.div`
    display: flex;
    justify-content: center;

    color: var(--baton-red);
    font-size: 20px;

    @media (max-width: 768px) {
      font-size: 16px;
    }
  `,

  TextareaContainer: styled.div<{
    $isError: boolean;
    $isAnimated: boolean;
    $isOpen: boolean;
    $isOptional: boolean;
  }>`
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    gap: 15px;

    width: 100%;
    height: ${({ $isOptional }) => ($isOptional ? 0 : '300px')};
    padding: ${({ $isOptional }) => ($isOptional ? '' : '8px 20px')};
    border: ${({ $isError, $isOpen }) =>
        $isError ? `var(--baton-red)` : $isOpen ? `var(--gray-500)` : `var(--gray-300)`}
      1px solid;
    border-radius: 10px;

    box-shadow: ${({ $isError }) =>
      $isError &&
      `-3px -3px 3px rgba(56, 38, 38, 0.03), inset -3px -3px 3px rgba(220, 0, 0, 0.03), inset 3px 3px 3px rgba(220, 0, 0, 0.03), 3px 3px 3px rgba(220, 0, 0, 0.03)`};

    overflow: hidden;

    ${({ $isOpen, $isAnimated }) => getAnimation($isOpen, $isAnimated)};
  `,

  Textarea: styled.textarea`
    flex: 1;

    border: transparent 1px solid;

    line-height: 2;
    font-size: 18px;

    resize: none;

    overflow: hidden;

    &::placeholder {
      font-size: 18px;
    }

    &:focus {
      outline: 0;
    }

    @media (max-width: 768px) {
      font-size: 16px;

      &::placeholder {
        font-size: 16px;
      }
    }
  `,

  InputConfig: styled.div`
    display: flex;
    justify-content: space-between;

    padding: 8px 10px;
  `,

  ErrorMessage: styled.div`
    color: red;
    font-size: 14px;
  `,

  InputTextLength: styled.div<{ $isError: boolean }>`
    display: flex;
    flex-direction: column;

    font-size: 14px;
    color: ${({ $isError }) => ($isError ? 'red' : 'var(--gray-400)')};
  `,

  Empty: styled.div`
    width: 1px;
  `,

  ModalContainer: styled.div`
    display: flex;
    flex-direction: column;
  `,

  ModalTitle: styled.div`
    display: flex;
    justify-content: center;

    width: 100%;
    padding: 15px;
    padding-bottom: 25px;
    border-bottom: 1px solid var(--gray-400);

    font-size: 22px;
    font-weight: 700;

    @media (max-width: 768px) {
      font-size: 20px;
    }
  `,

  ModalContents: styled.div`
    display: flex;
    flex-direction: column;
    gap: 25px;

    height: 480px;
    padding: 30px 10px;

    @media (max-width: 768px) {
      gap: 20px;

      padding: 20px 0;
    }
  `,

  GuideText: styled.div`
    position: relative;
    display: flex;
    flex-wrap: wrap;

    margin-left: 30px;
    margin-right: 10px;

    font-size: 19px;
    line-height: 1.4;
    color: var(--gray-700);

    @media (max-width: 768px) {
      font-size: 18px;
    }

    &::before {
      content: '•';
      display: block;
      position: absolute;
      left: -30px;
      top: -3px;

      height: 100%;

      color: var(--gray-600);
      font-size: 24px;
      font-weight: 700;
    }
  `,

  ConfirmButton: styled.button`
    display: flex;
    justify-content: end;

    width: 100%;
    padding: 0 20px;

    background-color: transparent;

    font-size: 18px;
    color: var(--baton-red);

    cursor: pointer;

    @media (max-width: 768px) {
      font-size: 16px;
    }
  `,
};
