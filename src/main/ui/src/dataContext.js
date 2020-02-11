import React, { createContext, useState } from "react";
import initialData from "./initialData";
import useLocalStorage from "./useLocalStorage";

export const DataContext = createContext();

// This context provider is passed to any component requiring the context
export const DataProvider = ({ children }) => {
  // Example of sharing value via global application state
  const [customerId, setCustomerId] = useState(-1);
  const [accountId, setAccountId] = useState(-1);
  const [portfolioId, setPortfolioId] = useState(-1);
  // Example of sharing value via global application state and persisting it in local storage
  const [projectName, setProjectName] = useLocalStorage(
    "projectName",
    initialData.projectName
  );

  return (
    <DataContext.Provider
      value={{
        projectName,
        setProjectName,
        customerId,
        setCustomerId,
        accountId,
        setAccountId,
        portfolioId,
        setPortfolioId,
      }}
    >
      {children}
    </DataContext.Provider>
  );
};
