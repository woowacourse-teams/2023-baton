import { ACCESS_TOKEN_LOCAL_STORAGE_KEY } from '@/constants';

describe('러너 E2E 테스트', () => {
  const typeAll = ($selector: string, values: string[]) => {
    cy.get($selector).each((ele, idx) => {
      if (idx + 1 > values.length) return;

      cy.wrap(ele).type(values[idx]);
    });
  };

  beforeEach(() => {
    cy.visit('/');

    localStorage.setItem(
      ACCESS_TOKEN_LOCAL_STORAGE_KEY,
      'eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJiYXRvbiIsImlhdCI6MTY5NTI2MDcxNSwiZXhwIjozMDk1MjYyNTE1LCJzb2NpYWxJZCI6Imd1cmlkYWVrIn0.TelcG0n8a7IxDU0-bOttjE9NZ4KtDgwaq5UhRkg1y9s',
    );
  });

  it('메인 페이지가 렌더링된다.', () => {
    cy.contains('리뷰 요청 글 작성').should('be.visible');
  });

  it('게시글을 검색한다.', () => {
    cy.contains('리뷰 완료').click();
    cy.wait(500);

    cy.get('input[aria-label="태그명 검색"]').click().type('React{enter}');

    cy.wait(500);

    const postList = cy.get('ul[aria-label="게시글 목록"]').children();

    postList.each((ele) => {
      cy.wrap(ele).contains('#React').should('be.visible');
      cy.wrap(ele).contains('리뷰 완료').should('be.visible');
    });
  });

  it('추가 게시글을 불러온다.', () => {
    cy.contains('더보기').click();

    cy.wait(500);
    const list = cy.get('ul[aria-label="게시글 목록"]').children();

    list.should('have.length', 20);
  });

  it('리뷰 요청글을 작성한다.', () => {
    cy.get('button[aria-label="리뷰 요청 글 작성"]').click();

    cy.wait(500);

    typeAll('form input', [
      '제제제제제목목목목목',
      'react{enter}js{enter}javascript{enter}자바스크립트{enter}자스{enter}',
      '주소주소',
    ]);

    typeAll('div textarea', [
      '구현 기능에 대한 설명입니다. 구현 기능에 대한 설명입니다.',
      '아쉬운 점은 없습니다. 아쉬운 점은 없습니다.',
    ]);

    cy.get('button[aria-label="리뷰 요청 글 생성"]').click();

    cy.wait(500);

    cy.get('div[aria-label="에러 메시지"]').should('be.visible');

    cy.get('input[value="주소주소"]').clear().type('https://github.com/woowacourse-teams/2023-baton/pull/1');

    cy.get('button[aria-label="리뷰 요청 글 생성"]').click();

    cy.wait(500);

    cy.get('div[aria-label="알림 메시지"]').should('be.visible');
  });

  it('마이페이지 게시글을 불러온다', () => {
    cy.get('img[alt="프로필"]').click();

    cy.wait(500);

    cy.contains('더보기').click();

    cy.wait(500);

    const list = cy.get('ul[aria-label="게시글 목록"]').children();

    list.should('have.length', 20);

    list.each((ele) => {
      cy.wrap(ele)
        .find('p[aria-label="지원한 서포터 수"]')
        .then((cur) => {
          cy.wrap(ele)
            .find('button')
            .should(Number(cur.text()) > 0 ? 'be.enabled' : 'be.disabled');
        });
    });
  });

  it('마이페이지 추가 게시글을 불러온다.', () => {
    cy.get('img[alt="프로필"]').click();

    cy.wait(500);

    cy.contains('button', '서포터').click();
    cy.contains('button', '진행중인 리뷰').click();

    cy.wait(500);

    const list = cy.get('ul[aria-label="게시글 목록"]').children();

    list.each((ele) => {
      cy.wrap(ele).should('contain.text', '리뷰 진행중');
    });

    cy.contains('더보기').click();

    cy.wait(500);

    cy.get('ul[aria-label="게시글 목록"]').children().should('have.length', 20);
  });
});
