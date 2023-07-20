import React, { TextareaHTMLAttributes } from 'react';
import styled from 'styled-components';

interface Props extends React.HTMLProps<HTMLTextAreaElement> {
  inputTextState: string;
  handleInputTextState: (e: React.ChangeEvent<HTMLTextAreaElement>) => void;
}

const TextArea = ({ inputTextState, maxLength, placeholder, width, height, handleInputTextState }: Props) => {
  return (
    <S.InputContainer $width={width} $height={height}>
      <S.InputBox onChange={handleInputTextState} maxLength={maxLength} placeholder={placeholder} />
      {maxLength && (
        <S.InputTextLength>
          {inputTextState?.length ?? 0} / {maxLength}
        </S.InputTextLength>
      )}
    </S.InputContainer>
  );
};

export default TextArea;

const S = {
  InputContainer: styled.div<{ $width?: string | number; $height?: string | number }>`
    display: flex;
    flex-direction: column;
    justify-content: space-between;

    padding: 10px 0;

    width: ${({ $width }) => $width || '400px'};
    height: ${({ $height }) => $height || '36px'};
    gap: 15px;
  `,

  InputTextLength: styled.div`
    display: flex;
    flex-direction: column;
    align-items: end;
    flex-basis: 20px;

    font-size: 12px;
    color: var(--gray-400);
  `,

  InputBox: styled.textarea`
    flex: 1;

    border-bottom: 1 solid var(--gray-400);
    background-color: white;
    border: transparent;

    font-size: 16px;
    line-height: 2;

    &::placeholder {
      font-size: 16px;
    }

    &:focus {
      outline: 0;
    }
  `,
};
