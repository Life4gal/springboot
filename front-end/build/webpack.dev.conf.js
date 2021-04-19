'use strict'

const path = require('path')
const utils = require('./utils')
const webpack = require('webpack')
const config = require('../config')
const {merge} = require('webpack-merge')
const base_webpack_config = require('./webpack.base.conf')
const html_webpack_plugin = require('html-webpack-plugin')
const friendly_error_plugin = require('friendly-errors-webpack-plugin')
const portfinder = require('portfinder')

function resolve(dir) {
	return path.join(__dirname, '..', dir)
}

const HOST = process.env.HOST
const PORT = process.env.PORT && Number(process.env.PORT)

const devWebpackConfig = merge(base_webpack_config, {
	module: {
		rules: utils.styleLoaders({sourceMap: config.dev.cssSourceMap, usePostCss: true})
	},
	devtool: config.dev.devtool,
	devServer: {
		clientLogLevel: 'warning',
		historyApiFallback: true,
		hot: true,
		compress: true,
		host: HOST || config.dev.host,
		port: PORT || config.dev.port,
		open: config.dev.autoOpenBrowser,
		overlay: config.dev.errorOverlay ? {warnings: false, err: true} : false,
		publicPath: config.dev.assetsPublicPath,
		proxy: config.dev.proxyTable,
		quiet: true,
		watchOptions: {
			poll: config.dev.poll
		}
	},
	plugins: [
		new webpack.DefinePlugin({
			'process.env': require('../config/dev.env')
		}),
		new webpack.HotModuleReplacementPlugin(),
		new html_webpack_plugin({
			filename: 'index.html',
			template: 'index.html',
			inject: true,
			favicon: resolve('favicon.ico'),
			title: 'springboot_vue'
		})
	]
})

module.exports = new Promise((resolve, reject) => {
	portfinder.basePort = process.env.PORT || config.dev.port
	portfinder.getPort((err, port) => {
		if (err) {
			reject(err)
		} else {
			process.env.PORT = port
			devWebpackConfig.devServer.port = port
			devWebpackConfig.plugins.push(new friendly_error_plugin({
				compilationSuccessInfo: {
					message: [`Your application is running here: http://${devWebpackConfig.devServer.host}:${port}`],
					onError: config.dev.notifyOnErrors ? utils.createNotifierCallback() : undefined
				}
			}))

			resolve(devWebpackConfig)
		}
	})
})
