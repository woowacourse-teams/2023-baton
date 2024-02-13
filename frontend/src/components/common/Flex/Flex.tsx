import { CSSProperties } from 'react';
import styled from 'styled-components';

interface FlexProps {
  align?: CSSProperties['alignItems'];
  justify?: CSSProperties['justifyContent'];
  direction?: CSSProperties['flexDirection'];
  gap?: CSSProperties['gap'];
}

const Flex = styled.div<FlexProps>(({ align, justify, direction, gap }) => ({
  display: 'flex',
  alignItems: align,
  justifyContent: justify,
  flexDirection: direction,
  gap: `${gap}px`,
}));

export default Flex;
