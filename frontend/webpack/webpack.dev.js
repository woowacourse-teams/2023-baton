const { merge } = require('webpack-merge');
const common = require('./webpack.common');

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
<<<<<<< HEAD
    proxy: {
      '/api': 'http://localhost:8080',
    },
=======
>>>>>>> dev/FE
  },
});
