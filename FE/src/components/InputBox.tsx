import React, { useRef } from 'react';
import styled from 'styled-components';

interface Props extends React.HTMLProps<HTMLInputElement> {}

const InputBox = ({ maxLength, placeholder, width, height, onChange }: Props) => {
  const inputRef = useRef<HTMLInputElement>(null);

  return (
    <S.InputContainer $width={width} $height={height}>
      <S.InputBox ref={inputRef} onChange={onChange} maxLength={maxLength} placeholder={placeholder} />
      {maxLength && (
        <S.InputTextLength>
          {inputRef.current?.value?.length ?? 0} / {maxLength}
        </S.InputTextLength>
      )}
    </S.InputContainer>
  );
};

export default InputBox;

const S = {
  InputContainer: styled.div<{ $width?: string | number; $height?: string | number }>`
    display: flex;
    justify-content: space-between;

    width: ${({ $width }) => $width || '400px'};
    height: ${({ $height }) => $height || '36px'};
    gap: 20px;
  `,

  InputTextLength: styled.div`
    display: flex;
    align-items: center;
    flex-basis: 40px;

    font-size: 12px;
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
