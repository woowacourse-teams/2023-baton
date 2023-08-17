import { rest } from 'msw';
import runnerPostList from './data/runnerPostList.json';
import runnerPostDetails from './data/runnerPostDetails.json';
import supporterCardList from './data/supporterCardList.json';
import myPageRunnerProfile from './data/myPageRunnerProfile.json';
import myPageSupporterProfile from './data/myPageSupporterProfile.json';
import myPageRunnerPost from './data/myPageRunnerPost.json';
import myPageSupporterPost from './data/myPageSupporterPost.json';
import runnerProfileInfo from './data/runnerProfileInfo.json';
import supporterProfileInfo from './data/supporterProfileInfo.json';
import supporterCandidate from './data/supporterCandidate.json';
import headerProfile from './data/headerProfile.json';
import supporterProfilePost from './data/supporterProfilePost.json';

export const handlers = [
  rest.post('*/posts/runner/test', async (req, res, ctx) => {
    return res(ctx.delay(300), ctx.status(201));
  }),

  rest.get('*/posts/runner/test', async (req, res, ctx) => {
    return res(ctx.delay(300), ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(runnerPostList));
  }),

  rest.get('*/profile/me', async (req, res, ctx) => {
    return res(ctx.delay(300), ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(headerProfile));
  }),

  rest.put('*/posts/runner/:runnerPostId/test', async (req, res, ctx) => {
    return res(ctx.delay(300), ctx.status(201));
  }),

  rest.delete('*/posts/runner/:runnerPostId/test', async (req, res, ctx) => {
    return res(ctx.delay(300), ctx.status(204));
  }),

  rest.get('*/posts/runner/:runnerPostId/test', async (req, res, ctx) => {
    return res(
      ctx.delay(300),
      ctx.status(200),
      ctx.set('Content-Type', 'application/json'),
      ctx.json(runnerPostDetails),
    );
  }),

  rest.get('*/supporters/test', async (req, res, ctx) => {
    return res(ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(supporterCardList));
  }),

  rest.post('*/posts/runner/test', async (req, res, ctx) => {
    return res(ctx.delay(300), ctx.status(201));
  }),

  rest.get('*/profile/runner/me', async (req, res, ctx) => {
    return res(ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(myPageRunnerProfile));
  }),

  rest.get('*/profile/supporter/me', async (req, res, ctx) => {
    return res(ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(myPageSupporterProfile));
  }),

  rest.get('*/posts/runner/me/runner', async (req, res, ctx) => {
    return res(ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(myPageRunnerPost));
  }),

  rest.get('*/posts/runner/me/supporter', async (req, res, ctx) => {
    return res(ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(myPageSupporterPost));
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
];
