import { createGlobalStyle } from 'styled-components';
import { ResetStyle } from './ResetStyle';

export const GlobalStyle = createGlobalStyle`

  ${ResetStyle}

  @font-face {
    font-family: 'Pretendard Variable';
    font-weight: 700;
    font-display: swap;
    src: local('Pretendard Bold'), url('https://cdn.jsdelivr.net/gh/orioncactus/pretendard/packages/pretendard/dist/web/static/woff2/Pretendard-Bold.woff2') format('woff2'), url('../../../packages/pretendard/dist/web/static/woff/Pretendard-Bold.woff') format('woff');
  }

  @font-face {
    font-family: 'Pretendard Variable';
    font-weight: 500;
    font-display: swap;
    src: local('Pretendard Medium'), url('https://cdn.jsdelivr.net/gh/orioncactus/pretendard/packages/pretendard/dist/web/static/woff2/Pretendard-Medium.woff2') format('woff2'), url('../../../packages/pretendard/dist/web/static/woff/Pretendard-Medium.woff') format('woff');
  }

  @font-face {
    font-family: 'Pretendard Variable';
    font-weight: 400;
    font-display: swap;
    src: local('Pretendard Regular'), url('https://cdn.jsdelivr.net/gh/orioncactus/pretendard/packages/pretendard/dist/web/static/woff2/Pretendard-Regular.woff2') format('woff2'), url('../../../packages/pretendard/dist/web/static/woff/Pretendard-Regular.woff') format('woff');
  }

  :root {
      --font-pretendard: 'Pretendard Variable';
  }
    
  * {
    box-sizing: border-box;
    font-family: "Pretendard Variable", sans-serif !important;
    color: #333333;
  }
  
  /* Colors *****************************************/
  :root {
    --baton-red: #F64545;
    --label-color: #333333;
    --count-color: #04c09e;
    --border-color: #dddddd;
    
    --black: #000000;
    --gray-800: #282828;
    --gray-700: #5e5e5e;
    --gray-500: #a6a6a6;
    --gray-600: #727272;
    --gray-300: #dddddd;
    --gray-400: #bbbbbb;
    --gray-100: #f3f3f3;
    --gray-200: #e8e8e8;
    --white-color: #ffffff;
}

  #root {
    width: 100%;
  }

  button{
    cursor: pointer;
    background-color: transparent;
  }
`;
