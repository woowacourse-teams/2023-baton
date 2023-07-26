export interface SupporterCard {
  supporterId: 1;
  name: string;
  company: string;
  reviewCount: number;
  totalRating: number;
  githubUrl: string;
  introduction: string;
}

export interface SupporterCardList {
  data: SupporterCard[];
}

export interface RunnerPostCreate {
  title: string;
  tags: string[];
  pullRequestUrl: string;
  deadline: string;
  contents: string;
  supporterId: number;
}
