const { merge } = require('webpack-merge');
const common = require('./webpack.common');
const path = require('path');
const webpack = require('webpack');
const CompressionPlugin = require('compression-webpack-plugin');

module.exports = merge(common, {
  mode: 'production',
  devtool: 'cheap-module-source-map',
  output: {
    filename: '[name].[contenthash].js',
    path: path.resolve(__dirname, '../dist'),
    publicPath: '/',
    clean: true,
  },
  optimization: {
    usedExports: true,
    minimize: true,
    splitChunks: {
      chunks: 'all',
    },
  },
  performance: {
    hints: false,
    maxEntrypointSize: 512000,
    maxAssetSize: 512000,
  },
  plugins: [
    new webpack.EnvironmentPlugin(['REACT_APP_BASE_URL']),
    new CompressionPlugin({
      filename: '[path][base].gz',
      algorithm: 'gzip',
      test: /\.js$|\.css$|\.html$/,
      threshold: 10240,
      minRatio: 0.8,
    }),
  ],
});
