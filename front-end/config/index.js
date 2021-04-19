// see http://vuejs-templates.github.io/webpack for documentation.
const path = require('path');
module.exports = {
	build: {
		env: require('./prod.env'),
		index: path.resolve(__dirname, '../dist/index.html'),
		assetsRoot: path.resolve(__dirname, '../dist'),
		assetsSubDirectory: 'static',
		assetsPublicPath: './',
		productionSourceMap: true,
		productionGzip: false,
		productionExtensions: ['js', 'css'],
		buildAnalyzerReport: process.env.npm_config_report
	},
	dev: {
		env: require('./dev.env'),
		host: 'localhost',
		port: 9520,
		autoOpenBrowser: true,
		autoOpenPage: '/login',
		assetsSubDirectory: 'static',
		assetsPublicPath: '/',
		proxyTable: {
			'/api': {
				target: 'http://localhost:8080',
				pathRewrite: {
					'^/api': '/'
				}
			}
		},
		cssSourceMap: false
	}
}