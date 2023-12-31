import React, { useRef } from 'react';
import styled from 'styled-components';

interface Props extends React.HTMLProps<HTMLInputElement> {
  inputTextState: string;
  handleInputTextState: (e: React.ChangeEvent<HTMLInputElement>) => void;
  fontSize?: string | number;
  fontWeight?: string | number;
  maxLengthFontSize?: string | number;
  autoFocus?: boolean;
}

const InputBox = ({
  inputTextState,
  maxLength,
  width,
  height,
  fontSize,
  fontWeight,
  maxLengthFontSize,
  autoFocus,
  handleInputTextState,
  ...rest
}: Props) => {
  return (
    <S.InputContainer $fontSize={fontSize} $fontWeight={fontWeight} $width={width} $height={height}>
      <S.InputBox onChange={handleInputTextState} maxLength={maxLength} autoFocus={autoFocus} {...rest} />
      {maxLength && (
        <S.InputTextLength $maxLengthFontSize={maxLengthFontSize}>
          {inputTextState.length ?? 0} / {maxLength}
        </S.InputTextLength>
      )}
    </S.InputContainer>
  );
};

export default InputBox;

const S = {
  InputContainer: styled.div<{
    $width?: string | number;
    $height?: string | number;
    $fontSize?: string | number;
    $fontWeight?: string | number;
  }>`
    display: flex;
    justify-content: space-between;

    width: ${({ $width }) => $width || '400px'};
    height: ${({ $height }) => $height || '36px'};
    gap: 20px;

    font-size: ${({ $fontSize }) => $fontSize || '18px'};
    font-weight: ${({ $fontWeight }) => $fontWeight || '400'};
  `,

  InputTextLength: styled.div<{ $maxLengthFontSize?: string | number }>`
    display: flex;
    align-items: center;

    font-size: ${({ $maxLengthFontSize }) => $maxLengthFontSize || '18px'};
    color: var(--gray-400);
  `,

  InputBox: styled.input`
    flex: 1;

    border-bottom: 1 solid var(--gray-400);
    background-color: white;
    border: transparent;

    &:focus {
      outline: 0;
    }
  `,
};
