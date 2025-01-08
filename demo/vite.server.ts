import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import { nodePolyfills } from 'vite-plugin-node-polyfills'
import { SharedConfig } from './vite.shared';

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
      react(),
      nodePolyfills({
        include: ['util', 'stream', 'events', 'buffer']
      })
  ],
  optimizeDeps: {
    ...SharedConfig.optimizeDeps,
  },
  ssr: {
    noExternal: true,
    target: 'webworker'
  },
  build: {
    ...SharedConfig.build,
    lib: {
      entry: './src/main/react/server.tsx',
      name: 'doubleview-demo-todo',
    },
    outDir: './build/doubleview/server',
    sourcemap: false,
    ssr: true,
  }
})
