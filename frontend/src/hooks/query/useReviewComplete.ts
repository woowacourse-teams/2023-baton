import { useContext } from 'react';
import { ToastContext } from '@/contexts/ToastContext';
import { InfiniteData, useMutation } from '@tanstack/react-query';
import { APIError } from '@/types/error';
import { patchReviewComplete } from '@/apis/apis';
import { ERROR_TITLE, TOAST_COMPLETION_MESSAGE } from '@/constants/message';
import { queryClient } from './queryClient';
import { GetMyPagePostResponse } from '@/types/myPage';

export const useReviewComplete = () => {
  const { showErrorToast, showCompletionToast } = useContext(ToastContext);

  const queryResult = useMutation<void, APIError, number>({
    mutationFn: (runnerPostId: number) => patchReviewComplete(runnerPostId),

    onMutate: async (deletePostId: number) => {
      await queryClient.cancelQueries({ queryKey: ['mySupporterPost', 'IN_PROGRESS'] });

      const previous = queryClient.getQueryData(['mySupporterPost', 'IN_PROGRESS']);

      queryClient.setQueryData<InfiniteData<GetMyPagePostResponse>>(['mySupporterPost', 'IN_PROGRESS'], (oldData) => {
        if (!oldData) return undefined;

        const newData = oldData.pages.map((page) => {
          return { ...page, data: page.data.filter((item) => item.runnerPostId !== deletePostId) };
        });

        return {
          ...oldData,
          pages: newData,
        };
      });

      return { previous };
    },

    onSuccess: () => {
      showCompletionToast(TOAST_COMPLETION_MESSAGE.REVIEW_COMPLETE);
    },

    onError: () => {
      showErrorToast({ title: ERROR_TITLE.REQUEST, description: '리뷰 완료 요청이 실패했어요' });

      queryClient.invalidateQueries({ queryKey: ['mySupporterPost', 'IN_PROGRESS'] });
    },
  });

  return queryResult;
};
