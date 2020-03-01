import React, { useEffect, useState, useContext } from "react";
import { NavLink } from "react-router-dom";

import { DataContext } from "./dataContext";
import ApiService from "./api/apiService";

const StockInfoPage = props => {
  const appData = useContext(DataContext);

  const { params } = props.match;
  const symbol = params.symbol;

  const [quoteListData, setQuoteListData] = useState([]);
  const [lotsData, setLotsData] = useState([]);
  const [numSharesToBuy, setNumSharesToBuy] = useState("");
  const [sharesToSell, setSharesToSell] = useState([]);
  const [enteredSharesToSell, setEnteredSharesToSell] = useState(0);
  const [invalidStockSymbol, setInvalidStockSymbol] = useState(false);

  const quoteUrl = "quote/" + symbol;
  const lotsUrl = `findLotsByPortfolioAnsOwnedStockSymbol/${
    appData.portfolioId
  }/${symbol}`;

  useEffect(() => {
    const fetchQuoteData = async () => {
      const result = await ApiService.getQuote(quoteUrl);
      console.log("Quote data: ", result.data.data);
      setQuoteListData(result.data.data);
      setInvalidStockSymbol(result.data.data.length === 0);
      ApiService.setCsrfHeader(result);
    };

    const fetchLotsData = async () => {
      const result = await ApiService.getLots(lotsUrl);
      console.log("Lots data: ", result.data);
      setLotsData(result.data);
      setSharesToSell(new Array(result.data.length));
      ApiService.setCsrfHeader(result);
    };

    fetchQuoteData();
    fetchLotsData();
  }, [quoteUrl, lotsUrl]);

  const renderStockQuote = quotes => {
    if (quotes.length < 1) {
      return "";
    }
    const quote = quotes[0];
    return (
      <div className="innerbox">
        <div className="innertext">
          <div>
            Name: {quote.name}, ( {quote.symbol} )
          </div>
        </div>
        <div>
          <b>Price: </b>
          {quote.price}
        </div>
        <div>
          <b>Price Open: </b>
          {quote.price_open}
        </div>
        <div>
          <b>Day's high: </b>
          {quote.day_high}
        </div>
        <div>
          <b>Day's low: </b>
          {quote.day_low}
        </div>
        <div>
          <b>Day's change: </b>
          {quote.day_change}
        </div>
        <div>
          <b>Change percent: </b>
          {quote.change_pct}%
        </div>
      </div>
    );
  };
  const updateNumSharesToBuy = e => {
    setNumSharesToBuy(e.target.value);
  };

  /**
   * Computes the total number of shares for sale across all the lots
   */
  const computeNumSharesForSale = () => {
    if (!sharesToSell) {
      return 0;
    }
    const reducer = (accumulator, element) => accumulator + Number(element.qty);
    return sharesToSell.filter(Boolean).reduce(reducer, 0);
  };

  const updateNumSharesToSell = (e, index, lotId) => {
    //TODO: validate num shares <= owned shares AND a number
    sharesToSell[index] = { lotId: lotId, qty: e.target.value };
    setSharesToSell(sharesToSell);
    setEnteredSharesToSell(computeNumSharesForSale(sharesToSell));
  };

  const handleBuyShares = () => {
    const buyStockRequestData = {
      portfolioId: appData.portfolioId,
      stockSymbol: symbol,
      numSharesToBuy: numSharesToBuy,
      stockPrice: quoteListData[0].price
    };
    console.log("buyStockRequestData: ", buyStockRequestData);
    ApiService.buyStock(buyStockRequestData).then(
      response => {
        console.log("Buy Shares Response: ", response);
        ApiService.setCsrfHeader(response);
        props.history.push("/viewPortfolio");
      },
      error => {
        console.error(error);
      }
    );
  };

  const handleSellShares = () => {
    // sharesToSell may have empty elements for the lots that are not being sold
    // this filters those empty elements out: https://stackoverflow.com/a/281335
    const filteredSharesToSell = sharesToSell.filter(Boolean);
    const sellStockRequestData = {
      portfolioId: appData.portfolioId,
      sharesToSell: filteredSharesToSell,
      stockPrice: quoteListData[0].price
    };

    console.log("sellStockRequestData: ", sellStockRequestData);
    ApiService.sellStock(sellStockRequestData).then(
      response => {
        console.log("Sell Shares Response: ", response);
        ApiService.setCsrfHeader(response);
        props.history.push("/viewPortfolio");
      },
      error => {
        console.error(error);
      }
    );
  };

  const generateOwnedLotTable = () => {
    return lotsData.map((element, index) => {
      const sharesToSellForLot = sharesToSell[index]
        ? sharesToSell[index].qty
        : "";
      return (
        <div key={index}>
          <div className="innerbox" key={index}>
            Lot ID: {element.id}, Shares: {element.sharesOwned}, Purchased at: $
            {element.purchasePrice}
          </div>
          Number of shares to sell:
          <div>
            <input
              type="text"
              name="shares"
              placeholder="# shares"
              value={sharesToSellForLot}
              onChange={e => updateNumSharesToSell(e, index, element.id)}
              title="Number of shares"
            />
          </div>
        </div>
      );
    });
  };

  const renderSellSection = () => {
    if (lotsData.length === 0) {
      return "";
    }
    return (
      <div className="innertext">
        <div>{generateOwnedLotTable()}</div>
        <button
          disabled={enteredSharesToSell <= 0}
          className="button"
          title="Click to sell"
          onClick={handleSellShares}
        >
          Sell
        </button>
      </div>
    );
  };

  if (invalidStockSymbol) {
    return (
      <div className="box">
        <div className="innerbox">
          <div className="error">
            {`${symbol}`} is an invalid stock symbol.
            <NavLink exact to="/stockInfoPage">
              <button className="button" title="Click to look up info">
                Try again
              </button>
            </NavLink>
          </div>
        </div>
      </div>
    );
  }
  return (
    <div className="box">
      <div className="innerbox">
        <h3>Stock Info:</h3>
      </div>
      {renderStockQuote(quoteListData)}
      <div className="innerbox">
        <h3>Buying and selling options:</h3>

        <div className="innertext">
          Select number of shares to buy:
          <div>
            <input
              type="text"
              name="shares"
              placeholder="# shares"
              value={numSharesToBuy}
              onChange={updateNumSharesToBuy}
              title="Number of shares"
            />
            <button
              disabled={!numSharesToBuy || numSharesToBuy <= 0}
              className="button"
              title="Click to buy"
              onClick={handleBuyShares}
            >
              Buy
            </button>
          </div>
        </div>

        <div>{renderSellSection()}</div>
      </div>
    </div>
  );
};
export default StockInfoPage;
