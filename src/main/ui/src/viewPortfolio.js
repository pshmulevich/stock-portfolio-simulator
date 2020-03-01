import React, { useState, useEffect, useContext } from "react";
import { NavLink } from "react-router-dom";

import stock_pic from "./assets/stock_pic.PNG";
import stock_pic2 from "./assets/stock_pic2.PNG";
import stock_pic3 from "./assets/stock_pic3.PNG";
import useInterval from "./util/useInterval";
import { DataContext } from "./dataContext";
import ApiService from "./api/apiService";

const otherStockData = [
  {
    id: 1,
    stockSymbol: "ABC",
    currentPrice: 321,
    upBy: 3.2
  },
  {
    id: 2,
    stockSymbol: "BCD",
    currentPrice: 345,
    upBy: 2.4
  },
  {
    id: 3,
    stockSymbol: "GSK",
    currentPrice: 432,
    upBy: 1.0
  },
  {
    id: 4,
    stockSymbol: "MSN",
    currentPrice: 765,
    upBy: 0.1
  }
];
//------------------------------------------------------------
const generateLotTable = lots => {
  return lots.map((element, index) => {
    return (
      <div className="boxitem" key={index}>
        Ticker: {element.stockSymbol},{/* Shares: {element.currentPrice},  */}
        Today's average up by at: {element.upBy}%
        <NavLink exact to={"/stockInfoPage/" + element.stockSymbol}>
          <button className="button" title="Click to buy stock">
            Buy
          </button>
        </NavLink>
      </div>
    );
  });
};
//-----------------------------------------------------------
const imageArray = [stock_pic, stock_pic2, stock_pic3];
const stockNameArray = ["AAPL", "F", "IBM"];
//-----------------------------------------------------------
const generateOwnedLotTable = lots => {
  return lots.map((element, index) => {
    return (
      <div className="boxitem" key={index}>
        <li>
          Lot ID: {element.id}, Shares: {element.sharesOwned}, Purchased at: $
          {element.purchasePrice}
        </li>
      </div>
    );
  });
};

const generateStockTable = stocks => {
  if (stocks.length === 0) {
    return (
      <div>
        <h3>No investments to show yet.</h3>
        <p>
          Go to
          <NavLink exact to={"/stockInfoPage"}>
            <button className="button" title="Click to look up info">
              Stock Info
            </button>
          </NavLink>
          tab and look up some stock to buy.
        </p>
      </div>
    );
  }
  return stocks.map((element, index) => {
    return (
      <div key={index}>
        <div>
          Stock Symbol:
          <NavLink exact to={"/stockInfoPage/" + element.stockSymbol}>
            <button className="button" title="Click to look up info">
              <b>{element.stockSymbol}</b>
            </button>
          </NavLink>
        </div>

        <div className="lots">{generateOwnedLotTable(element.lotDTOs)}</div>
      </div>
    );
  });
};

const ViewPortfolio = () => {
  const appData = useContext(DataContext);
  const [portfolioData, setPortfolioData] = useState([]);
  const portfolioUrl = `findOwnedStocksWithLotsByPortfolio/${
    appData.portfolioId
  }`;
  useEffect(() => {
    const fetchData = async () => {
      const result = await ApiService.getPortfolio(portfolioUrl);
      ApiService.setCsrfHeader(result);
      console.log("portfolioData: ", result.data);
      setPortfolioData(result.data);
    };

    fetchData();
  }, [portfolioUrl]);

  const [imageIndex, setImageIndex] = useState(0);

  const timeInterval = 4000;
  useInterval(() => {
    // Your custom logic here
    const newImageIndex =
      imageIndex >= imageArray.length - 1 ? 0 : imageIndex + 1;
    setImageIndex(newImageIndex);
    //console.log(newImageIndex);
  }, timeInterval);
  return (
    <div className="box">
      <h2 className="titlebox">
        Trending stocks: {stockNameArray[imageIndex]}
      </h2>
      <div className="slideshow-container">
        <div className="numbertext">{imageIndex} / 3</div>
        <img
          className="stockimage"
          src={imageArray[imageIndex]}
          width="100%"
          alt="img"
        />
        <div className="text">
          Last day stock chart: {stockNameArray[imageIndex]}
        </div>
      </div>
      <h3>Advertisement</h3>
      <div className="titlebox">{generateLotTable(otherStockData)}</div>
      <h1>Your Portfolio</h1>
      <div>
        <p className="titlebox">Summary of your investments:</p>
      </div>

      <div className="innerbox">{generateStockTable(portfolioData)}</div>
    </div>
  );
};
export default ViewPortfolio;
