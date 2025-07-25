import "fast-text-encoding"; // org.graalvm.polyglot.PolyglotException: ReferenceError: TextEncoder is not defined
import React from "react";
import ReactDOMServer from 'react-dom/server';

type WebContextTestProps = {
    title?: string;
}

export interface WebContextType {
  attributes: Record<string, any>
}

export function useWebContext(): WebContextType | null {
  // See the default render.js which puts it here
  const WebContext = (globalThis as any).doubleView.WebContext;
  if (!WebContext) {
    return null;
  }

  return React.useContext(WebContext)
}

export const WebContextTest: React.FC<WebContextTestProps> = (props) => {
    const title = props.title || "WebContext Test";
    const webContext = useWebContext();
    
    const userId = webContext?.attributes?.userId;
    const userRole = webContext?.attributes?.userRole;
    const requestPath = webContext?.attributes?.requestPath;
    
    return (
        <div className="web-context-test">
            <h1>{title}</h1>
            <div className="context-data">
                <p>User ID: <span className="user-id">{userId || 'N/A'}</span></p>
                <p>User Role: <span className="user-role">{userRole || 'N/A'}</span></p>
                <p>Request Path: <span className="request-path">{requestPath || 'N/A'}</span></p>
            </div>
            <p className="description">This component demonstrates accessing webContext data via React Context.</p>
        </div>
    );
};

export {
    // Export React itself because it's used by GraalVM to render the SSR
    React, ReactDOMServer
};