import { createGlobalStyle } from 'styled-components';
import { ResetStyle } from './ResetStyle';
import { colorPalette } from './colorPalette';

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
  ${colorPalette}

  #root {
    width: 100%;
  }

  button{
    cursor: pointer;
    background-color: transparent;
  }
`;
