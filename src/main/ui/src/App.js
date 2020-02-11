import React from "react";
import { BrowserRouter, Route, Switch, Redirect } from "react-router-dom";

import Home from "./home";
import Logout from "./logout";
import Dashboard from "./dashboard";
import WelcomeDashboard from "./welcomeDashboard";
import UserWelcome from "./userwelcome";
import RegisterWelcome from "./registerwelcome";
import StockOperations from "./stockOperations";
import ViewPortfolio from "./viewPortfolio";
import Sell from "./sell";
import BuySearch from "./buySearch";
import Header from "./header";
import Footer from "./footer";
import Counter from "./counter";
import StockInfoPage from "./stockInfoPage";
import LookUpStock from "./lookUpStock";
import { DataProvider } from "./dataContext";

import "./App.css";

const App = () => {
  return (
    <div id="app">
      <Header />
      <DataProvider>
        <BrowserRouter>
          <WelcomeDashboard>
            <Switch>
              <Route exact path="/" component={Home} />
              <Route exact path="/login" component={UserWelcome} />
              <Route exact path="/register" component={RegisterWelcome} />
              <Redirect from="/viewPortfolio" to="/login" />
              {/* If neither path matches the browser URL redirect to "/" */}
              <Redirect from="/*" to="/" />
            </Switch>
          </WelcomeDashboard>
          <Dashboard>
            <Switch>
              <Route exact path="/" component={Home} />
              <Route
                exact
                path="/stockOperations"
                component={StockOperations}
              />
              <Route exact path="/userwelcome" component={UserWelcome} />
              <Route
                exact
                path="/registerwelcome"
                component={RegisterWelcome}
              />
              <Route exact path="/viewPortfolio" component={ViewPortfolio} />
              <Route exact path="/sell" component={Sell} />
              <Route exact path="/buy" component={BuySearch} />
              <Route exact path="/counter" component={Counter} />
              <Route
                exact
                path="/stockInfoPage/:symbol"
                component={StockInfoPage}
              />
              <Route exact path="/stockInfoPage/" component={LookUpStock} />
              <Route exact path="/logout" component={Logout} />
              <Redirect from="/*" to="/" />
            </Switch>
          </Dashboard>
        </BrowserRouter>
      </DataProvider>
      <Footer />
    </div>
  );
};
export default App;
