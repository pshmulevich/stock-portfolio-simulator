import React, { useState } from "react";
import { NavLink } from "react-router-dom";

const LookupStock = props => {
  const [stockSymbol, setStockSymbol] = useState("");
  const updateStockInfo = e => {
    const symbol = e.target.value;
    setStockSymbol(symbol && symbol.toUpperCase());
  };
  return (
    <div className="box">
      <div className="innerbox">
        <h3>Get info on a stock: </h3>
        <input
          value={stockSymbol}
          onChange={updateStockInfo}
          type="text"
          title="Enter valid stock symbol"
        />

        <NavLink exact to={"/stockInfoPage/" + stockSymbol}>
          <button
            className="button"
            disabled={!stockSymbol}
            title="Click to look up"
          >
            <b>Look Up</b>
          </button>
        </NavLink>
      </div>
    </div>
  );
};
export default LookupStock;
