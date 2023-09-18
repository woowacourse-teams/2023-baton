import { rest } from 'msw';
import runnerPostList from './data/runnerPostList.json';
import runnerPostDetails from './data/runnerPostDetails.json';
import myPageRunnerProfile from './data/myPageRunnerProfile.json';
import myPageSupporterProfile from './data/myPageSupporterProfile.json';
import runnerProfileInfo from './data/runnerProfileInfo.json';
import supporterProfileInfo from './data/supporterProfileInfo.json';
import supporterCandidate from './data/supporterCandidate.json';
import headerProfile from './data/headerProfile.json';
import supporterProfilePost from './data/supporterProfilePost.json';
import notStarted from './data/myPagePost/notStarted.json';
import inProgress from './data/myPagePost/inProgress.json';
import done from './data/myPagePost/done.json';
import tagList from './data/tagList.json';
import searchedPostList from './data/searchedPostList.json';
import emptyPostList from './data/emptyRunnerPostList.json';
import { BATON_BASE_URL } from '@/constants';

export const handlers = [
  rest.post('*/posts/runner', async (req, res, ctx) => {
    return res(ctx.delay(300), ctx.status(201));
  }),

  rest.get('*/posts/runner', async (req, res, ctx) => {
    return res(ctx.delay(300), ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(runnerPostList));
  }),

  rest.get('*/profile/me', async (req, res, ctx) => {
    return res(ctx.delay(300), ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(headerProfile));
  }),

  rest.put('*/posts/runner/:runnerPostId', async (req, res, ctx) => {
    return res(ctx.delay(300), ctx.status(201));
  }),

  rest.delete('*/posts/runner/:runnerPostId', async (req, res, ctx) => {
    return res(ctx.delay(300), ctx.status(204));
  }),

  rest.get('*/posts/runner/:runnerPostId', async (req, res, ctx) => {
    return res(
      ctx.delay(300),
      ctx.status(200),
      ctx.set('Content-Type', 'application/json'),
      ctx.json(runnerPostDetails),
    );
  }),

  rest.get('*/profile/runner/me', async (req, res, ctx) => {
    return res(ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(myPageRunnerProfile));
  }),

  rest.get('*/profile/supporter/me', async (req, res, ctx) => {
    return res(ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(myPageSupporterProfile));
  }),

  rest.get('*/posts/runner/me/runner?size&page&reviewStatus', async (req, res, ctx) => {
    const reviewStatus = req.url.searchParams.get('reviewStatus');

    switch (reviewStatus) {
      case 'NOT_STARTED':
        return res(ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(notStarted));

      case 'IN_PROGRESS':
        return res(ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(inProgress));

      case 'DONE':
        return res(ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(done));

      default:
        return res(ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json({}));
    }
  }),

  rest.get('*/posts/runner/me/supporter?size&page&reviewStatus', async (req, res, ctx) => {
    const reviewStatus = req.url.searchParams.get('reviewStatus');

    switch (reviewStatus) {
      case 'NOT_STARTED':
        return res(ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(notStarted));

      case 'IN_PROGRESS':
        return res(ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(inProgress));

      case 'DONE':
        return res(ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(done));

      default:
        return res(ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json({}));
    }
  }),

  rest.get('*/profile/runner/me', async (req, res, ctx) => {
    return res(
      ctx.status(200),
      ctx.delay(500),
      ctx.set('Content-Type', 'application/json'),
      ctx.json(runnerProfileInfo),
    );
  }),

  rest.get('*/profile/supporter/me', async (req, res, ctx) => {
    return res(
      ctx.status(200),
      ctx.delay(500),
      ctx.set('Content-Type', 'application/json'),
      ctx.json(supporterProfileInfo),
    );
  }),

  rest.patch('*/profile/runner/me', async (req, res, ctx) => {
    return res(ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(runnerProfileInfo));
  }),

  rest.patch('*/profile/supporter/me', async (req, res, ctx) => {
    return res(ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(runnerProfileInfo));
  }),

  rest.get('*/posts/runner/:runnerPostId/supporters', async (req, res, ctx) => {
    return res(ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(supporterCandidate));
  }),

  rest.patch('*/posts/runner/:runnerPostId/supporters', async (req, res, ctx) => {
    return res(ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(supporterCandidate));
  }),

  rest.post('*/feedback/supporter', async (req, res, ctx) => {
    const { reviewType, descriptions, supporterId, runnerPostId } = await req.json();

    return res(ctx.delay(300), ctx.status(201), ctx.set('Content-Type', 'application/json'), ctx.json({}));
  }),

  rest.get('*/profile/runner/:runnerId', async (req, res, ctx) => {
    return res(ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(runnerProfileInfo));
  }),

  rest.get('*/profile/supporter/:supporterId', async (req, res, ctx) => {
    return res(ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(supporterProfileInfo));
  }),

  rest.get('*/posts/runner/search/:supporterId', async (req, res, ctx) => {
    return res(ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(supporterProfilePost));
  }),

  rest.post('*/posts/runner/:runnerPostId/application', async (req, res, ctx) => {
    return res(ctx.status(201));
  }),

  rest.patch('*/posts/runner/:runnerPostId/cancelation', async (req, res, ctx) => {
    return res(ctx.status(201));
  }),

  rest.get(`${BATON_BASE_URL}/tags/search`, async (req, res, ctx) => {
    const name = req.url.searchParams.get('tagName');

    const searchedTags = name ? tagList.data.filter((tag) => tag.tagName.includes(name)) : [];

    return res(ctx.status(200), ctx.json({ data: searchedTags }));
  }),

  rest.post('*/branch', async (req, res, ctx) => {
    const { repoName } = await req.json();

    return res(ctx.status(201));
  }),

  rest.get('*/posts/runner/tags/search', async (req, res, ctx) => {
    const name = req.url.searchParams.get('tagName');

    if (!name)
      return res(ctx.delay(300), ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(emptyPostList));

    const list = structuredClone(searchedPostList);
    list.data[0].tags = [name];

    return res(ctx.delay(300), ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(list));
  }),
];
