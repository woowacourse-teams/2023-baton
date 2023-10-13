import { REVIEW_TYPE } from '@/constants/feedback';
import { SelectOption } from './select';

export type ReviewType = (typeof REVIEW_TYPE)[number];

export type ReviewTypeOptions = SelectOption<ReviewType>[];
export type DescriptionOptions = SelectOption<string>[];

export interface PostFeedbackRequest {
  reviewType: ReviewType;
  descriptions: string[];
  supporterId: number;
  runnerPostId: number;
}
