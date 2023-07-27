import { useNavigate } from 'react-router-dom';
import { ROUTER_PATH } from '../router';
import { CreateRunnerPostRequest } from '@/types/runnerPost';

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

  const goToSupporterSelectPage = (data: Omit<CreateRunnerPostRequest, 'supporterId'>) => {
    navigate(ROUTER_PATH.SUPPORTER_SELECT, { state: data });
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
    goToSupporterSelectPage,
    goToCreationResultPage,
    goBack,
  };
};
