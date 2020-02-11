import React, { useState, useContext } from "react";
import axios from "axios";

import { serviceEndpoint } from "./configuration";
import { DataContext } from "./dataContext";
import {
  renderField,
  renderButton,
  renderNavLink,
  fieldType
} from "./util/renderers";

const UserWelcome = props => {
  const appData = useContext(DataContext);

  const [userName, setUserName] = useState("");
  const [password, setPassword] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  const handleLogin = () => {
    const loginData = {
      userName,
      password
    };
    axios.post(serviceEndpoint + "login", loginData).then(
      response => {
        console.log("response: ", response);
        const { customerId, accountId, portfolioId } = response.data;
        appData.setCustomerId(customerId);
        appData.setAccountId(accountId);
        appData.setPortfolioId(portfolioId);
        props.history.push("/viewPortfolio");
      },
      error => {
        console.error(error);
        const responseStatus = error.response.status;
        if (responseStatus === 401) {
          setErrorMessage("Username or Password did not match");
        } else {
          setErrorMessage("Unexpected Error");
        }
      }
    );
  };

  return (
    <div className="box">
      <h1>Welcome to Econopulse!</h1>
      <h3>Please Sign In</h3>
      <div className="innerbox">
        <div className="rTable">
          {renderField("Username", "userName", userName, setUserName)}
          {renderField(
            "Password",
            "password",
            password,
            setPassword,
            fieldType.PASSWORD
          )}
        </div>
        <div className="errorMessage">{errorMessage}</div>
        {renderButton("Sign in", handleLogin, !(userName && password))}
      </div>
      <h3>Not registered? Create an account!</h3>
      {renderNavLink("Register", "/register")}
    </div>
  );
};
export default UserWelcome;
