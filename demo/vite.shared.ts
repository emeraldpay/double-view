import { defineConfig } from 'vite'

let SharedConfig = defineConfig({
    mode: 'development',
    build: {
        sourcemap: true,
        rollupOptions: {
            output: {
                // see https://rollupjs.org/configuration-options/#output-assetfilenames
                assetFileNames: 'assets/styles.css',
                entryFileNames: 'assets/[name].js',
                chunkFileNames: 'assets/chunk-[name].js',
            }
        },
        commonjsOptions: {
            include: [/node_modules/],
        },
    }
});

export { SharedConfig };
