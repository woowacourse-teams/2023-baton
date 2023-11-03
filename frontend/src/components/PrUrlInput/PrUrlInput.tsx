import styled from 'styled-components';
import React, { useState } from 'react';
import { useMyGithubInfo } from '@/hooks/query/useMyGithubInfo';
import FocusInput from './FocusInput.tsx/FocusInput';

interface Props {
  value: string;
  setValue: (value: string) => void;
}

const PrUrlInput = ({ value, setValue }: Props) => {
  //미리 url 정보를 가져오기 위해 상위 컴포넌트에서 호출
  const { data: myGithubInfo } = useMyGithubInfo();
  myGithubInfo;

  const [isFocus, setIsFocus] = useState(false);

  const handleFocus = () => {
    setIsFocus(true);
  };

  const handleBlur = () => {
    setIsFocus(false);
  };

  return (
    <S.Container onBlur={handleBlur} onFocus={handleFocus} aria-label="pr url input">
      {isFocus ? (
        <FocusInput value={value} setValue={setValue} handleBlur={handleBlur} prefixValue={'https://github.com/'} />
      ) : (
        <S.NotFocusInput defaultValue={value} placeholder="PR 주소를 입력해주세요" />
      )}
    </S.Container>
  );
};

export default PrUrlInput;

const S = {
  Container: styled.div`
    width: 100%;
    min-height: 50px;
  `,

  NotFocusInput: styled.input`
    width: 700px;
    height: 50px;
    font-size: 18px;

    &::placeholder {
      font-size: 18px;

      @media (max-width: 768px) {
        font-size: 16px;
      }
    }

    @media (max-width: 768px) {
      width: 100%;

      font-size: 16px;
    }
  `,
};
