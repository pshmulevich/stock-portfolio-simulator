import React from "react";
import { BrowserRouter, Route, Switch } from "react-router-dom";

import Home from "./home";
import Dashboard from "./dashboard";
import StockOperations from "./stockOperations";
import ViewPortfolio from "./viewPortfolio";
import Sell from "./sell";
import BuySearch from "./buySearch";
import Header from "./header";
import Footer from "./footer";
import Counter from "./counter";
import StockInfoPage from "./stockInfoPage";
import LookUpStock from "./lookUpStock";

import Notfound from "./notfound";
import { DataProvider } from "./dataContext";

import "./App.css";

const App = () => {
  return (
    <div id="app">
      <Header />
      <DataProvider>
        <BrowserRouter>
          <Dashboard>
            <Switch>
              <Route exact path="/" component={Home} />
              <Route
                exact
                path="/stockOperations"
                component={StockOperations}
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
              <Route component={Notfound} />
            </Switch>
          </Dashboard>
        </BrowserRouter>
      </DataProvider>
      <Footer />
    </div>
  );
};
export default App;
