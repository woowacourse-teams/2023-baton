export interface GetSupporterCandidateResponse {
  data: Candidate[];
}

export interface Candidate {
  supporterId: number;
  name: string;
  company: string;
  imageUrl: string;
  reviewCount: number;
  message: string;
  technicalTags: string[];
}
