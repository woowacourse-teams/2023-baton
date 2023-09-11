import { TECHNICS } from '@/constants/tags';

export type Technic = (typeof TECHNICS)[number];

export interface GetSearchTagResponse {
  data: Tag[];
}

export interface Tag {
  tagId: number;
  tagName: string;
}
