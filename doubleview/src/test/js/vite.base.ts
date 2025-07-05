import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import { nodePolyfills } from 'vite-plugin-node-polyfills'

// Base configuration shared by all test builds
export const baseConfig = defineConfig({
  plugins: [
      react(),
      nodePolyfills({
        include: ['util', 'stream', 'events', 'buffer']
      })
  ],
  ssr: {
    noExternal: true,
    target: 'webworker'
  },
  build: {
    outDir: './build',
    sourcemap: false,
    ssr: true,
    emptyOutDir: false,
    commonjsOptions: {
      include: [/node_modules/],
    },
  }
})