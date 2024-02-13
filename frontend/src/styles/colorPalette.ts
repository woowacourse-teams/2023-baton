import { css } from 'styled-components';

export const colorPalette = css`
  :root {
    --baton-red: #f64545;
    --label-color: #333333;
    --border-color: #dddddd;

    --black: #000000;
    --gray-800: #282828;
    --gray-700: #5e5e5e;
    --gray-600: #727272;
    --gray-500: #a6a6a6;
    --gray-400: #bbbbbb;
    --gray-300: #dddddd;
    --gray-200: #e8e8e8;
    --gray-100: #f3f3f3;
    --white: #ffffff;
  }
`;

export const colors = {
  red: 'var(--baton-red)',
  border: 'var(--border-color)',
  label: 'var(--label-color)',
  white: 'var(--white)',
  black: 'var(--black)',
  gray800: 'var(--gray-800)',
  gray700: 'var(--gray-700)',
  gray600: 'var(--gray-600)',
  gray500: 'var(--gray-500)',
  gray400: 'var(--gray-400)',
  gray300: 'var(--gray-300)',
  gray200: 'var(--gray-200)',
  gray100: 'var(--gray-100)',
};

export type Colors = keyof typeof colors;
