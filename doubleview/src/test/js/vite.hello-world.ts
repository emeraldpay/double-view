import { defineConfig, mergeConfig } from 'vite'
import { baseConfig } from './vite.base'

export default mergeConfig(baseConfig, defineConfig({
  build: {
    lib: {
      entry: './src/HelloWorld.tsx',
      name: 'doubleview-test-hello-world',
    },
    rollupOptions: {
      output: {
        entryFileNames: 'hello-world.js',
      }
    },
  }
}))