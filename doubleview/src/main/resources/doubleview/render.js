export async function ssr(context, moduleName, componentName, props, webContext, callback) {
    let App = globalThis[moduleName][componentName];
    let ReactDOMServer = globalThis[moduleName].ReactDOMServer;
    let React = globalThis[moduleName].React;

    // Get or create WebContext - use globalThis.doubleView.WebContext for shared access
    if (!globalThis.doubleView) {
        globalThis.doubleView = {};
    }
    
    let WebContext = globalThis.doubleView.WebContext;
    if (!WebContext) {
        WebContext = React.createContext(null);
        // remember the context in the global scope, so it can be reused
        globalThis.doubleView.WebContext = WebContext;
    }
    
    // Create a provider component that wraps the app with webContext
    const AppWithContext = React.createElement(
        WebContext.Provider,
        { value: webContext },
        React.createElement(App, props, null)
    );

    const html = await ReactDOMServer.renderToString(AppWithContext, {});
    callback.setHTML(html);
}