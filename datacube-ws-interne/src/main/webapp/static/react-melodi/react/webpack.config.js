var path = require("path");
var Webpack = require("webpack");
var reactPath = path.resolve(__dirname);
var indexPath = path.resolve(__dirname, "index.js");

module.exports = {
  entry: indexPath,
  output: {
    path: reactPath,
    filename: "application.js",
    publicPath: reactPath
  },
  module: {
    rules: [
      {
        test: /\.js$/,
        loader: "babel-loader",
        exclude: /node_modules/,
        options: {
          presets: ["env", "react", "stage-0"]
        }
      },
      {
        test: /\.css$/,
        use: ["style-loader", "css-loader"]
      }
    ]
  },
  devtool: "eval-source-map",
  devServer: {
    port: 3030,
    proxy: {
      "/": "http://localhost:8080/"
    }
  }
};
