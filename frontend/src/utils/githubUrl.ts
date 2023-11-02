export type TypingGithubUrlPart = 'domain' | 'userId' | 'repoName' | 'pullRequest' | 'complete';

export const checkTypingUserIdUrl = (url: string) => {
  return (
    new RegExp(/^(https?:\/\/)?(www\.)?github\.com\/([a-zA-Z0-9_-]+)$/).test(url) ||
    new RegExp(/^(https?:\/\/)?(www\.)?github\.com\/$/).test(url)
  );
};

export const checkTypingRepositoryUrl = (url: string) => {
  return (
    new RegExp(/^(https?:\/\/)?(www\.)?github\.com\/([a-zA-Z0-9_-]+)\/([a-zA-Z0-9_-]+)$/).test(url) ||
    new RegExp(/^(https?:\/\/)?(www\.)?github\.com\/([a-zA-Z0-9_-]+)\/$/).test(url)
  );
};

export const checkTypingPullRequestUrl = (url: string) => {
  return (
    new RegExp(
      /^(https?:\/\/)?(www\.)?github\.com\/([a-zA-Z0-9_-]+)\/([a-zA-Z0-9_-]+)\/([-a-zA-Z0-9@:%._\+\/~#=]+)$/,
    ).test(url) || new RegExp(/^(https?:\/\/)?(www\.)?github\.com\/([a-zA-Z0-9_-]+)\/([a-zA-Z0-9_-]+)\/$/).test(url)
  );
};

export const checkCorrectPullRequestUrl = (url: string) => {
  return new RegExp(/^https:\/\/github\.com\/([a-zA-Z0-9_-]+)\/([a-zA-Z0-9_-]+)\/pull\/\d+$/).test(url);
};

export const typingGithubUrlPart = (url: string): TypingGithubUrlPart => {
  if (checkTypingUserIdUrl(url)) {
    return 'userId';
  }

  if (checkTypingRepositoryUrl(url)) {
    return 'repoName';
  }

  if (checkCorrectPullRequestUrl(url)) {
    return 'complete';
  }

  if (checkTypingPullRequestUrl(url)) {
    return 'pullRequest';
  }

  return 'domain';
};

export const extractUserId = (githubUrl: string) => {
  const githubUrlRegex = new RegExp(/https:\/\/github\.com\/([a-zA-Z0-9-_.]+)/);

  const match = githubUrl.match(githubUrlRegex);
  const userId = match?.[1];

  return userId;
};

export const extractRepoName = (githubUrl: string) => {
  const githubUrlRegex = new RegExp(/https:\/\/github\.com\/([a-zA-Z0-9-_.]+)\/([a-zA-Z0-9-_.]+)/);

  const match = githubUrl.match(githubUrlRegex);
  const userId = match?.[2];

  return userId;
};

export const extractPrNumber = (githubUrl: string) => {
  if (!checkCorrectPullRequestUrl(githubUrl)) return;

  const prNumber = githubUrl.split('/').slice(-1)[0];

  return prNumber;
};
