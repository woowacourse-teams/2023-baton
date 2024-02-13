export interface Rank {
  rank: number;
  name: string;
  supporterId: number;
  reviewedCount: number;
  imageUrl: string;
  githubUrl: string;
  technicalTags: string[];
}

export interface GetSupporterRankResponse {
  data: Rank[];
}
