export interface GetSupporterCardResponse {
  data: SupporterCard[];
}

export interface SupporterCard {
  supporterId: number;
  name: string;
  company: string;
  reviewCount: number;
  totalRating: number;
  githubUrl: string;
  introduction: string;
  technicalTags: string[];
}
