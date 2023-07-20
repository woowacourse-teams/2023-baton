export interface RunnerPost {
  runnerPostId: number;
  title: string;
  deadline: string;
  tags: string[];
  profile: { name: string; imageUrl: string };
}
