import React from "react";
import ReactDOM from "react-dom/client";

import "./index.scss";
import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import Header from "./Components/Header";
import Login from "./Components/Login";

const RemoteUsersApp = React.lazy(() => import("users/UsersApp"));

const App = () => (
    <Router>
        <Header/>
        <Routes>
            <Route exact path="/" element={<Login/>}/>
            <Route path="/users/*" element={<RemoteUsersApp/>}/>
        </Routes>
    </Router>
);
const rootElement = document.getElementById("app")
if (!rootElement) throw new Error("Failed to find the root element")

const root = ReactDOM.createRoot(rootElement)

root.render(<App/>)