import { GetRunnerPostResponse, ReviewStatus } from '@/types/runnerPost';
import { request } from './fetch';
import { GetSearchTagResponse } from '@/types/tags';

export const getRunnerPost = (limit: number, reviewStatus?: ReviewStatus, cursor?: any, tagName?: string) => {
  const params = new URLSearchParams({
    limit: limit.toString(),
    ...(cursor && { cursor: cursor.toString() }),
    ...(reviewStatus && { reviewStatus }),
    ...(tagName && { tagName }),
  });

  return request.get<GetRunnerPostResponse>(`/posts/runner?${params.toString()}`, false);
};

export const getSearchTag = (keyword: string) => {
  return request.get<GetSearchTagResponse>(`/tags/search?tagName=${keyword}`, false);
};
