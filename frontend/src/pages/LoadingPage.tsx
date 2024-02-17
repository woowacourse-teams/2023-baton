import Spinner from '@/components/common/Spinner/Spinner';
import styled from 'styled-components';

const LoadingPage = () => {
  return (
    <S.LoadingContainer>
      <S.LoadingWrapper>
        <Spinner />
      </S.LoadingWrapper>
    </S.LoadingContainer>
  );
};

export default LoadingPage;

const S = {
  LoadingContainer: styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
  `,

  LoadingWrapper: styled.div`
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 80vh;
    max-width: 1200px;
    width: 100%;

    @media (max-width: 768px) {
      padding: 15px;
    }
  `,
};
