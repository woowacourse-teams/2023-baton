const { merge } = require('webpack-merge');
const common = require('./webpack.common');
const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const webpack = require('webpack');

module.exports = merge(common, {
  mode: 'development',
  devtool: 'inline-source-map',
  output: {
    publicPath: '/',
  },
  devServer: {
    open: true,
    hot: true,
    compress: true,
    port: 3000,
    historyApiFallback: true,
    liveReload: true,
    proxy: {
      '/api': 'http://localhost:8080',
    },
  },
});
