import React, { useContext } from "react";
import { NavLink } from "react-router-dom";

import { DataContext } from "./dataContext";
import "./App.css";

const WelcomeDashboard = props => {
  const appData = useContext(DataContext);
  // Do not render if user is not logged in
  if (appData.customerId > 0) {
    return "";
  }

  return (
    <div id="dashboard">
      <div className="menu">
        <NavLink exact to="/">
          Home
        </NavLink>
        <NavLink exact to="/login">
          Login
        </NavLink>
        <NavLink exact to="/register">
          Register
        </NavLink>
      </div>
      <div className="content">{props.children}</div>
    </div>
  );
};

export default WelcomeDashboard;
