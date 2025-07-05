import { defineConfig, mergeConfig } from 'vite'
import { baseConfig } from './vite.base'

export default mergeConfig(baseConfig, defineConfig({
  build: {
    lib: {
      entry: './src/WebContextTest.tsx',
      name: 'doubleview-test-web-context',
    },
    rollupOptions: {
      output: {
        entryFileNames: 'web-context-test.js',
      }
    },
  }
}))