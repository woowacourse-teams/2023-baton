import React from 'react';
import styled from 'styled-components';
const Spinner = () => {
  return (
    <SpinnerContainer>
      <LessThan />
      <Slash />
      <GreaterThan />
    </SpinnerContainer>
  );
};
export default Spinner;
const SpinnerContainer = styled.div`
  display: flex;
  align-items: center;
`;
const LessThan = styled.div`
  position: relative;
  width: 45px;
  height: 35px;
  &::before {
    content: ' ';
    position: absolute;
    width: 27px;
    height: 12px;
    border-radius: 1.5px;
    background-color: var(--baton-red);
    transform: skew(0, -33deg);
    @media (min-width: 768px) {
      width: 42px;
      height: 15px;
    }
  }
  &::after {
    content: ' ';
    position: absolute;
    top: 21px;
    width: 27px;
    height: 12px;
    border-radius: 1.5px;
    background-color: var(--baton-red);
    transform: skew(0, 33deg);
    @media (min-width: 768px) {
      top: 29px;
      width: 42px;
      height: 14px;
    }
  }
  animation: pulseLess 0.6s alternate infinite ease-in-out;
  @keyframes pulseLess {
    to {
      opacity: 0.7;
      transform: scale(1.2);
    }
  }
  @media (min-width: 768px) {
    width: 65px;
    height: 40px;
  }
`;
const GreaterThan = styled(LessThan)`
  transform: scaleX(-1);
  animation: pulseGreater 0.6s 0.3s alternate infinite ease-in-out;
  @keyframes pulseGreater {
    to {
      opacity: 0.7;
      transform: scaleX(-1) scale(1.2);
    }
  }
`;
const Slash = styled.div`
  width: 9.5px;
  height: 65px;
  border-radius: 1.5px;
  background-color: var(--gray-800);
  transform: skew(-18deg);
  animation: pulseSlash 0.6s 0.3s alternate infinite ease-in-out;
  @keyframes pulseSlash {
    to {
      opacity: 0.7;
      transform: skew(-18deg);
    }
  }
  @media (min-width: 768px) {
    width: 13px;
    height: 93px;
  }
`;
