import { createGlobalStyle } from 'styled-components';
import { ResetStyle } from './ResetStyle';

export const GlobalStyle = createGlobalStyle`

${ResetStyle}
  @font-face {
      font-family: 'Pretendard Variable';
      src: url('https://cdn.jsdelivr.net/gh/orioncactus/pretendard/dist/web/static/pretendard.css') format("woff2");
  }

  :root {
      --font-pretendard: 'Pretendard Variable';
  }
    
  * {
    box-sizing: border-box;
    font-family: "Pretendard Variable", sans-serif !important;
    color: #333333;
  }
  
  html,

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
    display: flex;
    justify-content: center;
  }

  button{
    cursor: pointer;
  }
`;
