import React, { TextareaHTMLAttributes } from 'react';
import styled from 'styled-components';

interface Props extends React.HTMLProps<HTMLTextAreaElement> {
  inputTextState: string;
  handleInputTextState: (e: React.ChangeEvent<HTMLTextAreaElement>) => void;
  fontSize?: string | number;
  padding?: string;
}

const TextArea = ({
  inputTextState,
  maxLength,
  width,
  height,
  handleInputTextState,
  fontSize,
  padding,
  ...rest
}: Props) => {
  return (
    <S.InputContainer $width={width} $height={height} $padding={padding}>
      <S.InputBox onChange={handleInputTextState} maxLength={maxLength} {...rest} />
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
  InputContainer: styled.div<{ $width?: string | number; $height?: string | number; $padding?: string }>`
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    gap: 15px;

    width: ${({ $width }) => $width || '400px'};
    height: ${({ $height }) => $height || '36px'};
    padding: ${({ $padding }) => $padding || '20px 10px'};
  `,

  InputTextLength: styled.div`
    display: flex;
    flex-direction: column;
    align-items: end;
    flex-basis: 20px;

    font-size: 12px;
    color: var(--gray-400);
  `,

  InputBox: styled.textarea<{ $fontSize?: string | number }>`
    flex: 1;

    border: transparent;

    font-size: ${({ $fontSize }) => $fontSize || '18px'};
    line-height: 2;

    &.note {
    }

    &::placeholder {
      font-size: 18px;
    }

    &:focus {
      outline: 0;
    }
  `,
};
