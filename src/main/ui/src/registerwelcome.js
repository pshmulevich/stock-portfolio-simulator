import React, { useState } from "react";

import { renderField, renderButton, fieldType } from "./util/renderers";
import ApiService from "./api/apiService";

const RegisterWelcome = props => {
  const [userName, setUserName] = useState("");
  const [password, setPassword] = useState("");
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [email, setEmail] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  const handleRegister = () => {
    const registerationData = {
      firstName,
      lastName,
      email,
      userName,
      password
    };

    ApiService.signup(registerationData).then(
      response => {
        console.log("response: ", response);
        ApiService.setCsrfHeader(response);
        props.history.push("/login");
      },
      error => {
        const responseStatus = error.response && error.response.status;
        if (responseStatus === 409) {
          console.log(error.response.data.message);
          setErrorMessage(error.response.data.message);
        } else {
          setErrorMessage("Unexpected Error");
          console.error(error);
        }
      }
    );
  };

  return (
    <div className="box">
      <h1>Welcome to Econopulse!</h1>
      <h3>Register today! Its absolutely free.</h3>
      <div className="innerbox">
        <div className="rTable">
          {renderField("First Name", "firstname", firstName, setFirstName)}
          {renderField("Last Name", "lastname", lastName, setLastName)}
          {renderField("Email", "email", email, setEmail)}
          {renderField("Username", "username", userName, setUserName)}
          {renderField(
            "Password",
            "password",
            password,
            setPassword,
            fieldType.PASSWORD
          )}
        </div>
        <div className="errorMessage">{errorMessage}</div>
        {renderButton(
          "Register",
          handleRegister,
          !(userName && password && email && firstName && lastName)
        )}
      </div>
    </div>
  );
};
export default RegisterWelcome;
