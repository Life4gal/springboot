'use strict'

const path = require('path')
const utils = require('./utils')
const webpack = require('webpack')
const config = require('../config')
const merge = require('webpack-merge')
const base_webpack_config = require('./webpack.base.conf')
// const copy_webpack_plugin = require('copy-webpack-plugin')
const html_webpack_plugin = require('html-webpack-plugin')
const extract_text_plugin = require('extract-text-webpack-plugin')
const optimize_css_plugin = require('optimize-css-assets-webpack-plugin')
const uglify_js_plugin = require('uglifyjs-webpack-plugin')

function resolve(dir) {
	return path.join(__dirname, '..', dir)
}

const env = require('../config/prod.env')
const webpackConfig = merge(base_webpack_config, {
	module: {
		rules: utils.styleLoaders({
			sourceMap: config.build.productionSourceMap,
			extract: true,
			usePostCss: true
		})
	},
	devtool: config.build.productionSourceMap ? config.build.devtool : false,
	output: {
		path: config.build.assetsRoot,
		filename: utils.assetsPath('js/[name].[chunkhash].js'),
		chunkFilename: utils.assetsPath('js/[id].[chunkhash].js')
	},
	plugins: [
		new webpack.DefinePlugin({
			'process.env': env
		}),
		new uglify_js_plugin({
			uglifyOptions: {
				compress: {
					warnings: false
				}
			},
			sourceMap: config.build.productionSourceMap,
			parallel: true
		}),
		new extract_text_plugin({
			filename: utils.assetsPath('css/[name].[contenthash].css'),
			allChunks: false
		}),
		new optimize_css_plugin({
			cssProcessorOptions: config.build.productionSourceMap ? {safe: true, map: {inline: false}} : {safe: true}
		}),
		new html_webpack_plugin({
			filename: config.build.index,
			template: 'index.html',
			inject: true,
			favicon: resolve('favicon.ico'),
			title: 'springboot_vue',
			minify: {
				removeComments: true,
				collapseWhitespace: true,
				removeAttributeQuotes: true
			},
			chuncksSortMode: 'dependency'
		}),
		new webpack.HashedModuleIdsPlugin(),
		new webpack.optimize.CommonsChunkPlugin({
			name: 'vendor',
			minChunks(module) {
				return (
					module.resource &&
					/\.js$/.test(module.resource) &&
						module.resource.indexOf(
							path.join(__dirname, '../node_modules')
						) === 0
				)
			}
		}),
		new webpack.optimize.CommonsChunkPlugin({
			name: 'manifest',
			minChunks: Infinity
		}),
		new webpack.optimize.CommonsChunkPLugin({
			name: 'app',
			async: 'vendor-async',
			children: true,
			minChunks: 3
		})
	]
})

if(config.build.productionGzip)
{
	const compressionWebpackPlugin = require('compression-webpack-plugin')

	webpackConfig.plugins.push(
		new compressionWebpackPlugin({
			asset: '[path].gz[query]',
			algorithm: 'gzip',
			test: new RegExp(
				'\\.(' +
				config.build.productionGzipExtensions.join('|') +
				')$'
			),
			threshold: 10240,
			minRatio: 0.8
		})
	)
}

if(config.build.buildAnalyzerReport){
	const bundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin

	webpackConfig.plugins.push(new bundleAnalyzerPlugin())
}

module.exports = webpackConfig
