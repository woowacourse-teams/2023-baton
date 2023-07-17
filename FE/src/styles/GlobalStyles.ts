import { createGlobalStyle } from 'styled-components';
import PRETENDARD from '../assets/fonts/PretendardVariable.woff2';
import { ResetStyle } from './ResetStyle';

export const GlobalStyle = createGlobalStyle`

${ResetStyle}
  @font-face {
      font-family: 'Pretendard Variable';
      src: url(${PRETENDARD}) format("woff2");
  }

  :root {
      --font-pretendard: 'Pretendard Variable';
  }
    
  * {
    box-sizing: border-box;
    font-family: "Pretendard Variable", sans-serif !important;
  }
  
  html,

  /* Colors *****************************************/
  :root {
    --baton-red: #F13232;
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
`;
