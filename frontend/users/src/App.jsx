import React, {StrictMode} from "react";
import ReactDOM from "react-dom/client";

import "./index.scss";
import UsersPage from "./Components/UsersPage";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import UserPage from "./Components/UserPage";
import {StompSessionProvider} from "react-stomp-hooks";

const App = () => (
    <StompSessionProvider url={`${process.env.REACT_APP_API_BASE}/app/ws-endpoint`}>
        <Routes>
            <Route exact path="/" element={<UsersPage/>}/>
            <Route path="/:username" element={<UserPage/>}/>
        </Routes>
    </StompSessionProvider>
);
const rootElement = document.getElementById("app")
if (!rootElement) throw new Error("Failed to find the root element")

const root = ReactDOM.createRoot(rootElement)

export default App;

root.render(
    <StrictMode>
        <BrowserRouter basename="/users">
            <App/>
        </BrowserRouter>
    </StrictMode>
);