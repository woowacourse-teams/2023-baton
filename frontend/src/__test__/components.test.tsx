import React from 'react';
import { render } from '@testing-library/react';
import PostTagList from '@/components/PostTagList';
import ConfirmModal from '@/components/ConfirmModal';

describe('컴포넌트를 렌더링 한다.', () => {
  test('PostTagList: 개발 언어 태그 렌더링', () => {
    const data = ['React', 'JS', 'TS', '개인프로젝트', '대학생'];

    const component = render(<PostTagList tags={data} />);

    data.forEach((tag) => {
      component.getByText(`#${tag}`);
    });
  });

  test('ConfirmModal: 확인 모달창 렌더링 ', () => {
    const supporterName = '도리';
    const closeModal = jest.fn();
    const handleClickConfirmButton = jest.fn();

    render(<div id="modal-root"></div>);

    const modal = render(
      <ConfirmModal name={supporterName} closeModal={closeModal} handleClickConfirmButton={handleClickConfirmButton} />,
    );

    modal.getByText(`${supporterName}님에게 리뷰를 요청하시겠습니까?`);
  });
});
