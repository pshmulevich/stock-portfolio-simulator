import React, { useContext } from "react";
import { DataContext } from "./dataContext";

// TODO: not in use.
const Settings = () => {
  const appData = useContext(DataContext);
  return (
    <div>
      <h1>Settings</h1>
      <div>
        Project Name: <span>{appData.projectName}</span>
      </div>
      <div>
        Project Description: <span>{appData.projectDescription}</span>
      </div>
    </div>
  );
};
export default Settings;
