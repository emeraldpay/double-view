import "fast-text-encoding"; // org.graalvm.polyglot.PolyglotException: ReferenceError: TextEncoder is not defined
import React from "react";
import ReactDOMServer from 'react-dom/server';

import {App} from "./App";

export {
    // Needs to export all the components that may be used at the top (i.e., the entry point) of the React app
    // In this case it's just <App/> but a larger app must reference all the entry points
    App,
    // And export the React itself because it's used by the GraalVM to render the SSR
    React, ReactDOMServer
};