import { useNavigate } from 'react-router-dom';
import { ROUTER_PATH } from '../router';

export const usePageRouter = () => {
  const navigate = useNavigate();

  const goToMainPage = () => {
    navigate(ROUTER_PATH.MAIN);
  };

  const goToRunnerPostPage = (runnerPostId: number) => {
    navigate(ROUTER_PATH.RUNNER_POST.replace(':runnerPostId', runnerPostId.toString()));
  };

  const goToRunnerPostCreatePage = () => {
    navigate(ROUTER_PATH.RUNNER_POST_CREATE);
  };

  const goToLoginPage = () => {
    navigate(ROUTER_PATH.LOGIN);
  };

  const goToCreationResultPage = () => {
    navigate(ROUTER_PATH.RESULT);
  };

  const goBack = () => {
    navigate(-1);
  };

  return {
    goToMainPage,
    goToRunnerPostPage,
    goToRunnerPostCreatePage,
    goToLoginPage,
    goToCreationResultPage,
    goBack,
  };
};
