export const validatePullRequestUrl = (pullRequestUrl: string) => {
  if (!pullRequestUrl) {
    throw new Error('PR 주소를 입력해주세요');
  }

  const URL_REG = new RegExp(/^((http(s?))\:\/\/)((github))\.+((com))(\:[0-9]+)?(\/\S*)?$/);
  if (!pullRequestUrl.match(URL_REG))
    throw new Error('올바른 URL주소를 입력해주세요\nex) http://github.com/team/baton/pull/1');
};
