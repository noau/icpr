// vite.config.js
import { fileURLToPath, URL } from "node:url";
import { defineConfig } from "file:///F:/code/1024/icpr/icpr/frontend/node_modules/vite/dist/node/index.js";
import vue from "file:///F:/code/1024/icpr/icpr/frontend/node_modules/@vitejs/plugin-vue/dist/index.mjs";
import AutoImport from "file:///F:/code/1024/icpr/icpr/frontend/node_modules/unplugin-auto-import/dist/vite.js";
import Components from "file:///F:/code/1024/icpr/icpr/frontend/node_modules/unplugin-vue-components/dist/vite.js";
import { ElementPlusResolver } from "file:///F:/code/1024/icpr/icpr/frontend/node_modules/unplugin-vue-components/dist/resolvers.js";
var __vite_injected_original_import_meta_url = "file:///F:/code/1024/icpr/icpr/frontend/vite.config.js";
var vite_config_default = defineConfig({
  plugins: [
    vue(),
    AutoImport({
      resolvers: [ElementPlusResolver()]
    }),
    Components({
      resolvers: [ElementPlusResolver({ importStyle: "sass" })]
      //importStyle配置样式引入方式，自动引入修改主题色设置此属性
    })
  ],
  resolve: {
    alias: {
      "@": fileURLToPath(new URL("./src", __vite_injected_original_import_meta_url)),
      "quill": "quill/dist/quill.js",
      // 添加 quill 别名，确保正确引入
      "quill-css": "quill/dist/quill.snow.css"
      // 新增 CSS 别名
    }
  },
  server: {
    proxy: {
      "/api": {
        target: "http://localhost:8080",
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, "")
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
    include: ["quill"]
    // 确保 quill 被优化依赖处理
  }
});
export {
  vite_config_default as default
};
//# sourceMappingURL=data:application/json;base64,ewogICJ2ZXJzaW9uIjogMywKICAic291cmNlcyI6IFsidml0ZS5jb25maWcuanMiXSwKICAic291cmNlc0NvbnRlbnQiOiBbImNvbnN0IF9fdml0ZV9pbmplY3RlZF9vcmlnaW5hbF9kaXJuYW1lID0gXCJGOlxcXFxjb2RlXFxcXDEwMjRcXFxcaWNwclxcXFxpY3ByXFxcXGZyb250ZW5kXCI7Y29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2ZpbGVuYW1lID0gXCJGOlxcXFxjb2RlXFxcXDEwMjRcXFxcaWNwclxcXFxpY3ByXFxcXGZyb250ZW5kXFxcXHZpdGUuY29uZmlnLmpzXCI7Y29uc3QgX192aXRlX2luamVjdGVkX29yaWdpbmFsX2ltcG9ydF9tZXRhX3VybCA9IFwiZmlsZTovLy9GOi9jb2RlLzEwMjQvaWNwci9pY3ByL2Zyb250ZW5kL3ZpdGUuY29uZmlnLmpzXCI7aW1wb3J0IHsgZmlsZVVSTFRvUGF0aCwgVVJMIH0gZnJvbSAnbm9kZTp1cmwnXHJcblxyXG5pbXBvcnQgeyBkZWZpbmVDb25maWcgfSBmcm9tICd2aXRlJ1xyXG5pbXBvcnQgdnVlIGZyb20gJ0B2aXRlanMvcGx1Z2luLXZ1ZSdcclxuLy8gXHU5MTREXHU3RjZFZWxlbWVudC1wbHVzXHU2MzA5XHU5NzAwXHU1QkZDXHU1MTY1XHJcbmltcG9ydCBBdXRvSW1wb3J0IGZyb20gJ3VucGx1Z2luLWF1dG8taW1wb3J0L3ZpdGUnXHJcbmltcG9ydCBDb21wb25lbnRzIGZyb20gJ3VucGx1Z2luLXZ1ZS1jb21wb25lbnRzL3ZpdGUnXHJcbmltcG9ydCB7IEVsZW1lbnRQbHVzUmVzb2x2ZXIgfSBmcm9tICd1bnBsdWdpbi12dWUtY29tcG9uZW50cy9yZXNvbHZlcnMnXHJcblxyXG4vLyBodHRwczovL3ZpdGVqcy5kZXYvY29uZmlnL1xyXG5leHBvcnQgZGVmYXVsdCBkZWZpbmVDb25maWcoe1xyXG4gIHBsdWdpbnM6IFtcclxuICAgIHZ1ZSgpLFxyXG4gICAgQXV0b0ltcG9ydCh7XHJcbiAgICAgIHJlc29sdmVyczogW0VsZW1lbnRQbHVzUmVzb2x2ZXIoKV0sXHJcbiAgICB9KSxcclxuICAgIENvbXBvbmVudHMoe1xyXG4gICAgICByZXNvbHZlcnM6IFtFbGVtZW50UGx1c1Jlc29sdmVyKHsgaW1wb3J0U3R5bGU6IFwic2Fzc1wiIH0pXSwgLy9pbXBvcnRTdHlsZVx1OTE0RFx1N0Y2RVx1NjgzN1x1NUYwRlx1NUYxNVx1NTE2NVx1NjVCOVx1NUYwRlx1RkYwQ1x1ODFFQVx1NTJBOFx1NUYxNVx1NTE2NVx1NEZFRVx1NjUzOVx1NEUzQlx1OTg5OFx1ODI3Mlx1OEJCRVx1N0Y2RVx1NkI2NFx1NUM1RVx1NjAyN1xyXG4gICAgfSlcclxuICBdLFxyXG4gIHJlc29sdmU6IHtcclxuICAgIGFsaWFzOiB7XHJcbiAgICAgICdAJzogZmlsZVVSTFRvUGF0aChuZXcgVVJMKCcuL3NyYycsIGltcG9ydC5tZXRhLnVybCkpLFxyXG4gICAgICAncXVpbGwnOiAncXVpbGwvZGlzdC9xdWlsbC5qcycsICAvLyBcdTZERkJcdTUyQTAgcXVpbGwgXHU1MjJCXHU1NDBEXHVGRjBDXHU3ODZFXHU0RkREXHU2QjYzXHU3ODZFXHU1RjE1XHU1MTY1XHJcbiAgICAgICdxdWlsbC1jc3MnOiAncXVpbGwvZGlzdC9xdWlsbC5zbm93LmNzcycgLy8gXHU2NUIwXHU1ODlFIENTUyBcdTUyMkJcdTU0MERcclxuICAgIH1cclxuICB9LFxyXG4gIHNlcnZlcjoge1xyXG4gICAgcHJveHk6IHtcclxuICAgICAgJy9hcGknOiB7XHJcbiAgICAgICAgdGFyZ2V0OiAnaHR0cDovL2xvY2FsaG9zdDo4MDgwJyxcclxuICAgICAgICBjaGFuZ2VPcmlnaW46IHRydWUsXHJcbiAgICAgICAgcmV3cml0ZTogKHBhdGgpID0+IHBhdGgucmVwbGFjZSgvXlxcL2FwaS8sICcnKVxyXG4gICAgICB9XHJcbiAgICB9XHJcbiAgfSxcclxuICBjc3M6IHtcclxuICAgIHByZXByb2Nlc3Nvck9wdGlvbnM6IHtcclxuICAgICAgc2Nzczoge1xyXG4gICAgICAgIGFkZGl0aW9uYWxEYXRhOiBgXHJcbiAgICAgICAgQHVzZSBcIkAvc3R5bGVzL2VsZW1lbnQvaW5kZXguc2Nzc1wiIGFzICo7XHJcbiAgICAgICAgQHVzZSBcIkAvc3R5bGVzL3Zhci5zY3NzXCIgYXMgKjtcclxuICAgICAgICBgXHJcbiAgICAgIH1cclxuICAgIH1cclxuICB9LFxyXG4gIG9wdGltaXplRGVwczoge1xyXG4gICAgaW5jbHVkZTogWydxdWlsbCddICAvLyBcdTc4NkVcdTRGREQgcXVpbGwgXHU4OEFCXHU0RjE4XHU1MzE2XHU0RjlEXHU4RDU2XHU1OTA0XHU3NDA2XHJcbiAgfVxyXG59KVxyXG4iXSwKICAibWFwcGluZ3MiOiAiO0FBQTJSLFNBQVMsZUFBZSxXQUFXO0FBRTlULFNBQVMsb0JBQW9CO0FBQzdCLE9BQU8sU0FBUztBQUVoQixPQUFPLGdCQUFnQjtBQUN2QixPQUFPLGdCQUFnQjtBQUN2QixTQUFTLDJCQUEyQjtBQVA2SSxJQUFNLDJDQUEyQztBQVVsTyxJQUFPLHNCQUFRLGFBQWE7QUFBQSxFQUMxQixTQUFTO0FBQUEsSUFDUCxJQUFJO0FBQUEsSUFDSixXQUFXO0FBQUEsTUFDVCxXQUFXLENBQUMsb0JBQW9CLENBQUM7QUFBQSxJQUNuQyxDQUFDO0FBQUEsSUFDRCxXQUFXO0FBQUEsTUFDVCxXQUFXLENBQUMsb0JBQW9CLEVBQUUsYUFBYSxPQUFPLENBQUMsQ0FBQztBQUFBO0FBQUEsSUFDMUQsQ0FBQztBQUFBLEVBQ0g7QUFBQSxFQUNBLFNBQVM7QUFBQSxJQUNQLE9BQU87QUFBQSxNQUNMLEtBQUssY0FBYyxJQUFJLElBQUksU0FBUyx3Q0FBZSxDQUFDO0FBQUEsTUFDcEQsU0FBUztBQUFBO0FBQUEsTUFDVCxhQUFhO0FBQUE7QUFBQSxJQUNmO0FBQUEsRUFDRjtBQUFBLEVBQ0EsUUFBUTtBQUFBLElBQ04sT0FBTztBQUFBLE1BQ0wsUUFBUTtBQUFBLFFBQ04sUUFBUTtBQUFBLFFBQ1IsY0FBYztBQUFBLFFBQ2QsU0FBUyxDQUFDLFNBQVMsS0FBSyxRQUFRLFVBQVUsRUFBRTtBQUFBLE1BQzlDO0FBQUEsSUFDRjtBQUFBLEVBQ0Y7QUFBQSxFQUNBLEtBQUs7QUFBQSxJQUNILHFCQUFxQjtBQUFBLE1BQ25CLE1BQU07QUFBQSxRQUNKLGdCQUFnQjtBQUFBO0FBQUE7QUFBQTtBQUFBLE1BSWxCO0FBQUEsSUFDRjtBQUFBLEVBQ0Y7QUFBQSxFQUNBLGNBQWM7QUFBQSxJQUNaLFNBQVMsQ0FBQyxPQUFPO0FBQUE7QUFBQSxFQUNuQjtBQUNGLENBQUM7IiwKICAibmFtZXMiOiBbXQp9Cg==
