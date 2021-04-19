'use strict'

const path = require('path')
const utils = require('./utils')
const config = require('../config')
const vue_loader_config = require('./vue-loader.conf')

function resolve(dir) {
	return path.join(__dirname, '..', dir)
}

const createLintingRule = () => ({
	test: /\.(js|vue)$/,
	loader: 'eslint-loader',
	enforce: 'pre',
	include: [resolve('src'), resolve('test')],
	options: {
		formatter: require('eslint-friendly-formatter'),
		emitWarning: !config.dev.showEslintErrorsInOverlay
	}
})

module.exports = {
	context: path.resolve(__dirname, '../'),
	entry: {
		app: './src/main.js'
	},
	output: {
		path: config.build.assetsRoot,
		filename: '[name].js',
		publicPath: process.env.NODE_ENV === 'production' ? config.build.assetsPublicPath : config.dev.assetsPublicPath
	},
	resolve: {
		extensions: ['.js', '.vue', '.json'],
		alias: {
			'vue$': 'vue/dist/vue.esm.js',
			'@': resolve('src'),
		}
	},
	module: {
		rules: [
			...(config.dev.useEslint ? [createLintingRule()] : []),
			{
				test: /\.vue$/,
				loader: 'vue-loader',
				options: vue_loader_config
			},
			{
				test: /\.js$/,
				loader: 'babel-loader',
				include: [resolve('src'), resolve('test'),
				resolve('node_modules/webpack-server/client')]
			},
			{
				test: /\.svg$/,
				loader: 'svg-sprite-loader',
				include: [resolve('src/icons')],
				options: {
					symbolId: 'icon-[name]'
				}
			},
			{
				test: /\.(png|jpe?g|gif|svg)(\?.*)?$/,
				loader: 'url-loader',
				exclude: [resolve('src/icons')],
				options: {
					limit: 10000,
					name: utils.assetsPath('img/[name].[hash:7].[ext]')
				}
			},
			{
				test: /\.(mp4|webm|ogg|mp3|wav|flac|aac)(\?.*)?$/,
				loader: 'url-loader',
				options: {
					limit: 10000,
					name: utils.assetsPath('media/[name].[hash:7].[ext]')
				}
			},
			{
				test: /\.(woff2?|eot|ttf|otf)(\?.*)?$/,
				loader: 'url-loader',
				options: {
					limit: 100000,
					name: utils.assetsPath('fonts/[name].[hash:7].[ext]')
				}
			}
		]
	},
	// node: {
	// 	setImmediate: false,
	// 	dgram: 'empty',
	// 	fs: 'empty',
	// 	net: 'empty',
	// 	tls: 'empty',
	// 	child_process: 'empty'
	// }
}