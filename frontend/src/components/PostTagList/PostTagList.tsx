import React from 'react';
import { styled } from 'styled-components';
import PostTag from '../PostTag/PostTag';

interface Props {
  tags: string[];
}

function PostTagList({ tags }: Props) {
  return (
    <S.List>
      {tags.map((tag) => (
        <PostTag key={tag} tag={tag} />
      ))}
    </S.List>
  );
}

export default PostTagList;

const S = {
  List: styled.ul`
    list-style: none;

    li:not(:last-child) {
      margin-right: 10px;
    }
  `,
};
