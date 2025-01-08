import { hydrateRoot } from 'react-dom/client';

import {App} from "./App";

type DoubleView = {
    component: string,
    props: any,
}

// Once the HTML and JS are loaded it's time to hydrate the React app to enable dynamic interaction on the client side
addEventListener("load", () => {
    // @ts-ignore
    let rw: DoubleView = window.doubleView;
    let component: React.ReactNode;

    // For this Demo App we have only `<App/>` as the entry point
    // But for a larger app with multiple entry points the code should check which exact component was rendered on the server
    if (rw.component === "App") {
        component = <App {...rw.props}/>
    } else {
        console.warn("Unknown component", rw.component);
        component = <div/>
    }

    // @ts-ignore
    hydrateRoot(document.getElementById("react"), component);
});