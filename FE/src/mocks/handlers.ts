import { rest } from 'msw';

export const handlers = [
  rest.post('msw/posts/runner', async (req, res, ctx) => {
    const { title, tags, pullRequestUrl, deadline, contents } = await req.json();

    return res(ctx.delay(300), ctx.status(201), ctx.set('Content-Type', 'application/json'));
  }),

  rest.get('msw/posts/runner', async (req, res, ctx) => {
    return res(
      ctx.delay(300),
      ctx.status(200),
      ctx.set('Content-Type', 'application/json'),
      ctx.json([
        {
          runnerPostId: 1,
          title: '제목',
          deadline: '마감기한',
          tags: ['java', 'JAVA'],
          profile: {
            name: '이름',
            imageUrl: '사실 프로필 사진',
          },
        },
      ]),
    );
  }),

  rest.put('msw/posts/runner/:runnerPostId', async (req, res, ctx) => {
    const { title, tags, pullRequestUrl, deadline, contents } = await req.json();

    return res(ctx.delay(300), ctx.status(201), ctx.set('Content-Type', 'application/json'));
  }),

  rest.delete('msw/posts/runner/:runnerPostId', async (req, res, ctx) => {
    return res(ctx.delay(300), ctx.status(204), ctx.set('Content-Type', 'application/json'));
  }),

  rest.get('msw/posts/runner/:runnerPostId', async (req, res, ctx) => {
    return res(
      ctx.delay(300),
      ctx.status(200),
      ctx.set('Content-Type', 'application/json'),
      ctx.json({
        runnerPostId: 1,
        title: '제목',
        deadline: '마감기한',
        tags: ['java', 'JAVA'],
        contents: '내용',
        isOwner: true,
        profile: {
          memberId: 2,
          name: '이름',
          company: '회사 및 소속',
          imageUrl: '사실 프로필 사진',
        },
      }),
    );
  }),
];
