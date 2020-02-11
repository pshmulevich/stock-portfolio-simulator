import React from "react";
import { NavLink } from "react-router-dom";

const Home = () => {
  return (
    <div className="box">
      <h1>Stock Portfolio</h1>
      <div className="innerbox">
        <p>
          Now you can track your stock portfolio investments easily with this
          free open-source project!
        </p>{" "}
        <p>
          <i>It is still a work in progress.</i>
        </p>
      </div>
      <h3>About the project: </h3>
      <div className="innerbox">
        <p>
          This is a simulation stock portfolio web application written with:
        </p>
        <div>
          <p>
            <li>Java</li>
            <li>Spring Boot</li>
            <li>JPA/Postgresql</li>
            <li>JavaScript/React</li>
            <li>HTML</li>
            <li>CSS</li>
            <li>Maven</li>
            See source code{" "}
            <a
              href="https://github.com/pshmulevich/stock-portfolio-simulator"
              rel="noopener noreferrer"
              target="_blank"
            >
              {" "}
              on github
            </a>
          </p>
        </div>
      </div>

      <div className="innerbox">
        <div>
          <p>
            Take a look at your investments in the
            <NavLink exact to="/viewPortfolio">
              {" "}
              Portfolio{" "}
            </NavLink>
            Page
          </p>
        </div>
      </div>
    </div>
  );
};
export default Home;
