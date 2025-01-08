import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import { SharedConfig } from './vite.shared';

export default defineConfig({
  plugins: [
      react(),
  ],
  optimizeDeps: {
    ...SharedConfig.optimizeDeps,
  },
  define: {
    //
    // Please make sure you have `production` when building for production
    //
    'process.env': {NODE_ENV: "development"},
  },
  build: {
    ...SharedConfig.build,
    lib: {
      entry: './src/main/react/client.tsx',
      name: 'doubleview-demo-todo',
    },
    outDir: './build/doubleview/client',
    ssr: false,

    //
    // Make sure you configure the two following for production too
    //
    sourcemap: true,
    minify: false,
  },
})
