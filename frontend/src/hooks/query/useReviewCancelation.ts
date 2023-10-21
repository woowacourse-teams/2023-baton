import { useContext } from 'react';
import { ToastContext } from '@/contexts/ToastContext';
import { InfiniteData, useMutation } from '@tanstack/react-query';
import { APIError } from '@/types/error';
import { patchReviewCancelation } from '@/apis/apis';
import { ERROR_TITLE, TOAST_COMPLETION_MESSAGE } from '@/constants/message';
import { queryClient } from './queryClient';
import { GetMyPagePostResponse } from '@/types/myPage';

export const useReviewCancelation = () => {
  const { showErrorToast, showCompletionToast } = useContext(ToastContext);

  const queryResult = useMutation<void, APIError, number>({
    mutationFn: (runnerPostId: number) => patchReviewCancelation(runnerPostId),

    onMutate: async (deletedPostId: number) => {
      await queryClient.cancelQueries({ queryKey: ['mySupporterPost', 'NOT_STARTED'] });

      const previous = queryClient.getQueryData(['mySupporterPost', 'NOT_STARTED']);

      queryClient.setQueryData<InfiniteData<GetMyPagePostResponse>>(['mySupporterPost', 'NOT_STARTED'], (oldData) => {
        if (!oldData) return undefined;

        const newData = oldData.pages.map((page) => {
          return { ...page, data: page.data.filter((item) => item.runnerPostId !== deletedPostId) };
        });

        return {
          ...oldData,
          pages: newData,
        };
      });

      return { previous };
    },

    onSuccess: () => {
      showCompletionToast(TOAST_COMPLETION_MESSAGE.REVIEW_CANCEL);
    },

    onError: () => {
      showErrorToast({ title: ERROR_TITLE.REQUEST, description: '리뷰 취소 요청이 실패했어요' });
    },

    onSettled: () => {
      queryClient.invalidateQueries({ queryKey: ['mySupporterPost', 'NOT_STARTED'] });
    },
  });

  return queryResult;
};
