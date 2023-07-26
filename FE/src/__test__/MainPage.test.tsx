import React from 'react';
import { render } from '@testing-library/react';
import PostTagList from '@/components/PostTagList';

describe('태그 리스트를 렌더링 한다.', () => {
  test('개발 언어 태그 렌더링', () => {
    const data = ['React', 'JS', 'TS', '개인프로젝트', '대학생'];

    const component = render(<PostTagList tags={data} />);

    data.forEach((tag) => {
      component.getByText(`#${tag}`);
    });
  });
});
