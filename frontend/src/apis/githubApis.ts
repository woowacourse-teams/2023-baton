import { fetchJson } from './fetch';

interface GetGithubRepoListResponse {
  data: RepoInfo[];
}

interface GetGithubPrListResponse {
  data: PrInfo[];
}

interface RepoInfo {
  title: string;
  isForked: boolean;
  originUser: string | null;
  originRepoName: string | null;
}

interface PrInfo {
  title: string;
  link: string;
}

const GITHUB_BASE_URL = 'http://51.20.87.151:3000/api/github';

export const getGithubRepoList = (userId: string, page?: number, enabled?: boolean) => {
  if (!enabled) return { data: [] };

  if (!userId) return { data: [] };

  return fetchJson<GetGithubRepoListResponse>(`/repos?userId=${userId}&page=${page ?? 1}`, {}, GITHUB_BASE_URL);
};

export const getGithubPrList = (userId: string, repoName: string, page?: number, enabled?: boolean) => {
  if (!enabled) return { data: [] };

  if (!userId || !repoName) return { data: [] };

  return fetchJson<GetGithubPrListResponse>(
    `/pulls?userId=${userId}&repoName=${repoName}&page=${page ?? 1}`,
    {},
    GITHUB_BASE_URL,
  );
};
