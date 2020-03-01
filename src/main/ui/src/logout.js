import { useContext } from "react";

import { DataContext } from "./dataContext";
import ApiService from "./api/apiService";
import "./App.css";

const Logout = props => {
  const appData = useContext(DataContext);
  appData.setCustomerId(-1);
  appData.setAccountId(-1);
  appData.setPortfolioId(-1);
  ApiService.logOut();
  props.history.push("/");
  return "";
};
export default Logout;
