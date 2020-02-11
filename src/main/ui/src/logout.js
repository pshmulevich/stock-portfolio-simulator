import { useContext } from "react";

import { DataContext } from "./dataContext";
import "./App.css";

// TODO: this is a simple logout implementation. Needs more robust one to terminate server session
const Logout = props => {
  const appData = useContext(DataContext);
  appData.setCustomerId(-1);
  appData.setAccountId(-1);
  appData.setPortfolioId(-1);
  props.history.push("/login");
};
export default Logout;
