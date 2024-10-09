export async function ssr(context, moduleName, componentName, props, callback) {
    let App = globalThis[moduleName][componentName];
    let ReactDOMServer = globalThis[moduleName].ReactDOMServer;
    let React = globalThis[moduleName].React;

    const element = React.createElement(App, props, null);
    const html = await ReactDOMServer.renderToString(element, {});
    callback.setHTML(html);
}