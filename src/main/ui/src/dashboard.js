import React from "react";
import { NavLink } from "react-router-dom";

import "./App.css";

export default class Dashboard extends React.Component {
  render() {
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
        </div>
        <div className="content">{this.props.children}</div>
      </div>
    );
  }
}
