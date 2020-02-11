import React from "react";
import axios from "axios";
import { serviceEndpoint } from "./configuration";

// TODO: not in use.
const StockOperations = () => {

  const handlePopulateStocks = () => {
    const serviceUrl = serviceEndpoint;

    console.log("get request to", serviceUrl, "payload:");
    axios.get(serviceUrl + "ownedStocksCreate").then(
      response => {
        console.log("Stocks Generated");
      },
      error => {
        console.error(error);
      }
    );
  };
  const handleViewStocks = () => {
    const serviceUrl = serviceEndpoint;
    axios.get(serviceUrl + "findAllOwnedStocksWithLots").then(
      response => {
        console.log("Display owned stocks");
      },
      error => {
        console.error(error);
      }
    );
  };

  const handleViewOwnedStocks = () => {
    const serviceUrl = serviceEndpoint;
    axios.get(serviceUrl + "findAllStocks").then(
      response => {
        console.log("Display all stocks");
      },
      error => {
        console.error(error);
      }
    );
  };

  return (
    <div className="box">
      <div className="innerbox">
        <h3>Debug commands to run to test data</h3>
      </div>
      <div className="innerbox">
        <div>
          <button onClick={handlePopulateStocks}>Populate Stocks</button>
        </div>
        <div>
          <button onClick={handleViewStocks}>Get Stocks</button>
        </div>
        <div>
          <button onClick={handleViewOwnedStocks}>Get Owned Stocks</button>
        </div>
      </div>
    </div>
  );
};
export default StockOperations;
