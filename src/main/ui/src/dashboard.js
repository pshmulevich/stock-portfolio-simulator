import React, { useContext } from "react";
import { NavLink } from "react-router-dom";

import { DataContext } from "./dataContext";
import "./App.css";

const Dashboard = props => {
  const appData = useContext(DataContext);
  // Do not render if user is logged in
  if (appData.customerId < 0) {
    return "";
  }

  return (
    <div id="dashboard">
      <div className="menu">
        <NavLink exact to="/">
          Home
        </NavLink>
        <NavLink exact to="/viewPortfolio">
          Portfolio
        </NavLink>
        <NavLink to="/stockInfoPage">Stock Info</NavLink>
        <NavLink to="/logout">Logout</NavLink>
      </div>
      <div className="content">{props.children}</div>
    </div>
  );
};
export default Dashboard;
