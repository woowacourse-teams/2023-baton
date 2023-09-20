import React from 'react';
import { styled } from 'styled-components';

interface Props {
  tag: string;
}

function PostTag({ tag }: Props) {
  return <S.TagName>#{tag}</S.TagName>;
}

export default PostTag;

const S = {
  TagName: styled.li`
    float: left;

    font-size: 18px;
    color: var(--gray-500);

    @media (max-width: 768px) {
      font-size: 12px;
    }
  `,
};
