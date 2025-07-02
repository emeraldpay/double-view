import "fast-text-encoding"; // org.graalvm.polyglot.PolyglotException: ReferenceError: TextEncoder is not defined
import React from "react";
import ReactDOMServer from 'react-dom/server';

type HelloWorldProps = {
    message?: string;
    name?: string;
}

export const HelloWorld: React.FC<HelloWorldProps> = (props) => {
    const message = props.message || "Hello";
    const name = props.name || "World";
    
    return (
        <div className="hello-world">
            <h1>{message}, {name}!</h1>
            <p>This is a simple test component for DoubleView rendering.</p>
        </div>
    );
};

export {
    // Export React itself because it's used by GraalVM to render the SSR
    React, ReactDOMServer
};