import { Technic } from '@/types/tags';

export const TECHNICS = ['javascript', 'typescript', 'react', 'java', 'spring'] as const;

export const TECH_COLOR_MAP: Record<Technic, string> = {
  javascript: '#F7DF1E',
  typescript: '#007acc',
  react: '#00D8FF',
  java: '#F58219',
  spring: '#5FB832',
} as const;
