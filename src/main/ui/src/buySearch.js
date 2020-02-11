import React, { useState } from "react";
import { NavLink } from "react-router-dom";

const BuySearch = props => {
  const [stockSymbol, setStockSymbol] = useState("");
  const updateStockInfo = e => {
    setStockSymbol(e.target.value);
  };
  return (
    <div className="box">
      <div className="innerbox">
        <h3>Get info on a stock: </h3>
        <input
          onChange={updateStockInfo}
          type="text"
          title="Type a stock symbol"
        />

        <NavLink exact to={"/stockInfoPage/" + stockSymbol}>
          <button
            className="button"
            disabled={!stockSymbol}
            title="Enter valid stock symbol"
          >
            <b>Look Up</b>
          </button>
        </NavLink>
      </div>
    </div>
  );
};
export default BuySearch;
