export type Method = 'GET' | 'POST' | 'DELETE' | 'PUT' | 'PATCH';

export interface pageParamsRequest {
  limit: number;
  cursor?: number;
}
