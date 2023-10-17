const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    proxy: {
      '^/api': {
        target: 'http://localhost:2345/colab_platform_war',
        changeOrigin: true,
        logLevel: 'debug'
      },
    }
  }
})