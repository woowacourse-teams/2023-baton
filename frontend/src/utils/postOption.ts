import { REVIEW_STATUS, RUNNER_POST_OPTIONS, SUPPORTER_POST_OPTIONS } from '@/constants';
import { ReviewStatus } from '@/types/runnerPost';
import { SelectOption } from '@/types/select';

export type PostOptions = SelectOption<ReviewStatus>[];

export const createPostOptions = (value: ReviewStatus[], labels: string[]) => {
  const newOptions: PostOptions = [];

  for (const index in labels) {
    const postOption = {
      value: value[index],
      label: labels[index],
      selected: false,
    };

    newOptions.push(postOption);
  }

  newOptions[0].selected = true;

  return newOptions;
};

export const runnerPostOptions = createPostOptions(REVIEW_STATUS, RUNNER_POST_OPTIONS);
export const supportPostOptions = createPostOptions(REVIEW_STATUS, SUPPORTER_POST_OPTIONS);
