import React from "react";
import { NavLink } from "react-router-dom";

const sampleLotData = [
  {
    id: 1,
    sharesOwned: 10,
    purchasePrice: 300,
    ownedStockDTO: {
      id: 1,
      stockSymbol: "AAPL"
    }
  },
  {
    id: 2,
    sharesOwned: 50,
    purchasePrice: 314,
    ownedStockDTO: {
      id: 1,
      stockSymbol: "AAPL"
    }
  },
  {
    id: 3,
    sharesOwned: 34,
    purchasePrice: 19,
    ownedStockDTO: {
      id: 1,
      stockSymbol: "T"
    }
  },
  {
    id: 4,
    sharesOwned: 22,
    purchasePrice: 275,
    ownedStockDTO: {
      id: 1,
      stockSymbol: "F"
    }
  }
];

const generateLotTable = lots => {
  return lots.map((element, index) => {
    return (
      <div className="innerbox" key={index}>
        <div className="titlebox">
          <h3>Sell shares of: {element.ownedStockDTO.stockSymbol}</h3>
        </div>
        <div>
          <div className="boxitem">Lot ID: {element.id}</div>
          <div className="boxitem">Shares: {element.sharesOwned}</div>
          <div className="boxitem"> Purchased at: ${element.purchasePrice}</div>
          <button className="button">Sell All</button>
        </div>
        <div className="boxtext">
          <b>Or select number of shares to sell: </b>
          <input type="text" name="shares" value="Shares" />
          <button className="button">Sell!</button>
        </div>
      </div>
    );
  });
};


const Sell = () => {
  return (
    <div>
      <div className="box">
        <div className="lots">{generateLotTable(sampleLotData)}</div>
      </div>

      <div className="box">
        <p>
          Sell more stock:
          <NavLink exact to="/viewPortfolio">
            <button className="button">Select stock</button>
          </NavLink>
        </p>
      </div>
    </div>
  );
};
export default Sell;
