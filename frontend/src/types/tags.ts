import { TECHNICS } from '@/constants/tags';

export type Technic = (typeof TECHNICS)[number];

export interface GetSearchTagResponse {
  data: Tag[];
}

export interface Tag {
  id: number;
  tagName: string;
}
