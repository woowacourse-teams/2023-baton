export interface RunnerPostData {
  runnerPostId: number;
  title: string;
  deadline: string;
  tags: string[];
  profile: { name: string; imageUrl: string };
  watchedCount: number;
  chattingCount: number;
}

export interface RunnerPost {
  data: RunnerPostData[];
}
