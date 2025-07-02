import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import { nodePolyfills } from 'vite-plugin-node-polyfills'

// https://vitejs.dev/config/
export default defineConfig({
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
    lib: {
      entry: './src/HelloWorld.tsx',
      name: 'doubleview-test-hello-world',
    },
    outDir: './build',
    sourcemap: false,
    ssr: true,
    rollupOptions: {
      output: {
        entryFileNames: 'hello-world.js',
      }
    },
    commonjsOptions: {
      include: [/node_modules/],
    },
  }
})