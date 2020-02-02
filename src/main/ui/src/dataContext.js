import React, { createContext, useState } from "react";
import initialData from "./initialData";
import useLocalStorage from "./useLocalStorage";

export const DataContext = createContext();

// This context provider is passed to any component requiring the context
export const DataProvider = ({ children }) => {
  // Example of sharing value via global application state
  const [projectDescription, setProjectDescription] = useState(
    initialData.projectDescription
  );
  const [currentImage, setCurrentImage] = useState(0);
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
        projectDescription,
        setProjectDescription,
        currentImage,
        setCurrentImage
      }}
    >
      {children}
    </DataContext.Provider>
  );
};
