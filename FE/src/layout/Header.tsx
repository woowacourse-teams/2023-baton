import React from 'react';
import styled from 'styled-components';

const Header = () => {
  return (
    <S.HeaderWrapper>
      <S.HeaderContainer>
        <S.Logo />
        <S.MenuContainer>
          <S.LoginButton>로그인</S.LoginButton>
          <S.ProfileAvatar />
        </S.MenuContainer>
      </S.HeaderContainer>
    </S.HeaderWrapper>
  );
};

export default Header;

const S = {
  HeaderWrapper: styled.div`
    display: flex;
    justify-content: center;

    width: 100%;
    padding: 0 30px;

    border-bottom: 0.3px solid #333333;
  `,

  HeaderContainer: styled.div`
    display: flex;
    justify-content: space-between;
    align-items: center;

    width: 1200px;
    height: 80px;
  `,

  Logo: styled.img`
    width: 140px;
    height: 40px;

    background-color: #222222;
  `,

  MenuContainer: styled.div`
    display: flex;
    align-items: center;
    gap: 30px;
  `,

  LoginButton: styled.button`
    width: 76px;
    height: 35px;

    border-radius: 50px;

    background-color: var(--baton-red);
    color: var(--white-color);
    font-size: 14px;
  `,

  ProfileAvatar: styled.div`
    width: 50px;
    height: 50px;

    border-radius: 50%;

    background-color: #ababab;
  `,
};
