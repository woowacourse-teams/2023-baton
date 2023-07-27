import { useNavigate } from 'react-router-dom';
import { ROUTER_PATH } from '../router';
<<<<<<< HEAD
import { CreateRunnerPostRequest } from '@/types/runnerPost';
=======
>>>>>>> 1454089 (feat: 서포터 선택 목록 모달 기능 구현)

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

<<<<<<< HEAD
  const goToSupporterSelectPage = (data: Omit<CreateRunnerPostRequest, 'supporterId'>) => {
    navigate(ROUTER_PATH.SUPPORTER_SELECT, { state: data });
  };

=======
>>>>>>> 1454089 (feat: 서포터 선택 목록 모달 기능 구현)
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
