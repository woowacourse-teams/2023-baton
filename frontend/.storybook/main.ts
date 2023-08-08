import type { StorybookConfig } from '@storybook/react-webpack5';
import webpack from '../webpack/webpack.common';

const config: StorybookConfig = {
  stories: ['../src/**/*.stories.@(js|jsx|mjs|ts|tsx)'],

  staticDirs: ['../src/assets'],
  addons: [
    '@storybook/addon-links',
    '@storybook/addon-essentials',
    '@storybook/addon-onboarding',
    '@storybook/addon-interactions',
    '@storybook/addon-essentials',
    '@storybook/addon-styling',
  ],
  framework: {
    name: '@storybook/react-webpack5',
    options: {},
  },
  docs: {
    autodocs: 'tag',
  },
  webpackFinal: async (config) => {
    return {
      ...config,
      module: { ...config.module },
      resolve: { ...webpack.resolve },
    };
  },
};
export default config;
