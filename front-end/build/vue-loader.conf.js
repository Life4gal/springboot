'use strict'

const utils = require('./utils')
const config = require('../config')
const is_production = process.env.NODE_ENV === 'production'
const source_map_enabled = is_production ? config.build.productionSourceMap : config.dev.cssSourceMap

module.exports = {
	loaders: utils.cssLoaders({
		sourceMap: source_map_enabled,
		extract: is_production
	}),
	cssSourceMap: source_map_enabled,
	cacheBusting: config.dev.cacheBusting,
	transformToRequire: {
		video: ['src', 'poster'],
		source: 'src',
		img: 'src',
		image: 'xlink:href'
	}
}
