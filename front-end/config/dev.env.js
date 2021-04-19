const {merge} = require('webpack-merge');
const prod_env = require('./prod.env');

module.exports = merge(prod_env, {
	NODE_ENV: '"development"',
	BASE_URL: '"/api"',
})
