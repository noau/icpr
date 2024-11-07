import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
// 配置element-plus按需导入
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    AutoImport({
      resolvers: [ElementPlusResolver()],
    }),
    Components({
      resolvers: [ElementPlusResolver({ importStyle: "sass" })], //importStyle配置样式引入方式，自动引入修改主题色设置此属性
    })
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
      'quill': 'quill/dist/quill.js',  // 添加 quill 别名，确保正确引入
      'quill-css': 'quill/dist/quill.snow.css' // 新增 CSS 别名
    }
  },
  server: {
    proxy: {
      '/api': {
        target: 'http://127.0.0.1:4523',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '')
      }
    }
  },
  css: {
    preprocessorOptions: {
      scss: {
        additionalData: `
        @use "@/styles/element/index.scss" as *;
        @use "@/styles/var.scss" as *;
        `
      }
    }
  },
  optimizeDeps: {
    include: ['quill']  // 确保 quill 被优化依赖处理
  }
})
