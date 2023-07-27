import React, { useRef, useState } from 'react';
import { styled } from 'styled-components';
import Tag from './common/Tag';

interface Props {
  tags: string[];
  pushTag: (tag: string) => void;
  popTag: (tag?: string) => void;
  width?: string | number;
}

const TagInput = ({ tags, pushTag, popTag, width }: Props) => {
  const inputRef = useRef<HTMLInputElement>(null);
  const [isFocus, setIsFocus] = useState(false);

  const onkeyDownInput = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.nativeEvent.isComposing) return;

    if (!inputRef.current) return;

    if (e.key === 'Backspace' && !inputRef.current.value) {
      popTag();
    }

    if (!inputRef.current.value) return;

    if (e.key === 'Enter') {
      e.preventDefault();
      const newTag = inputRef.current.value;
      pushTag(newTag);
      inputRef.current.value = '';
    }
  };

  const onFocus = () => {
    setIsFocus(true);
  };

  const outFocus = () => {
    setIsFocus(false);
  };

  return (
    <S.TagInputContainer $width={width}>
      <S.TagList>
        {tags.map((item) => (
          <S.TagItem key={item}>
            <Tag
              onClick={() => {
                popTag(item);
              }}
            >
              {item}
            </Tag>
          </S.TagItem>
        ))}
        <S.InputBoxContainer>
          <S.InputBox
            onKeyDown={onkeyDownInput}
            ref={inputRef}
            placeholder="태그를 입력해주세요"
            onFocus={onFocus}
            onBlur={outFocus}
          />
          {isFocus && <S.InputDescription>엔터를 입력하여 태그를 등록할 수 있습니다.</S.InputDescription>}
        </S.InputBoxContainer>
      </S.TagList>
    </S.TagInputContainer>
  );
};

export default TagInput;

const S = {
  TagInputContainer: styled.div<{ $width?: string | number }>`
    display: flex;
    align-items: center;

    gap: 15px;
    width: ${({ $width }) => $width || '500px'};

    font-size: 18px;
  `,

  InputBox: styled.input`
    height: 36px;

    border: transparent;

    &:focus {
      outline: 0;
    }
  `,

  TagList: styled.ul`
    display: flex;
    flex-wrap: wrap;

    gap: 10px;
  `,

  TagItem: styled.li``,

  InputBoxContainer: styled.div`
    position: relative;
  `,

  InputDescription: styled.div`
    position: absolute;
    top: 35px;

    width: 230px;
    padding: 20px 10px;

    background-color: var(--gray-500);
    border-radius: 3px;
    box-shadow: rgba(0, 0, 0, 0.15) 1.95px 1.95px 2.6px;

    font-size: 12px;
    color: white;
  `,
};
