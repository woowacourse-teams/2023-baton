import { Colors, colors } from '@/styles/colorPalette';
import { Typography, typographyMap } from '@/styles/typography';
import { CSSProperties } from 'react';
import styled from 'styled-components';

interface TextProps {
  typography?: Typography;
  color?: Colors;
  display?: CSSProperties['display'];
  textAlign?: CSSProperties['textAlign'];
  fontWeight?: CSSProperties['fontWeight'];
  textDecoration?: string;
  $bold?: boolean;
}

const Text = styled.span<TextProps>(
  ({ color = 'label', display, textAlign, fontWeight, $bold, textDecoration }) => ({
    color: colors[color],
    display,
    textAlign,
    fontWeight: $bold ? 'bold' : fontWeight,
    textDecoration,
  }),
  ({ typography = 't6' }) => typographyMap[typography],
);

export default Text;
