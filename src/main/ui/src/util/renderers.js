import React from "react";
import { NavLink } from "react-router-dom";

export const updateInput = (e, setter) => {
  const value = e.target.value;
  setter(value);
};

export const fieldType = {
  TEXT: "text",
  PASSWORD: "password"
};

export const renderField = (label, name, value, setter, type) => {
  // Default field type to "text" if not provided
  type = !type ? fieldType.TEXT : type;
  return (
    <div className="rTableRow">
      <div className="rTableCell">
        <label htmlFor={name}>{label}: </label>
      </div>
      <div className="rTableCell">
        <input
          value={value}
          onChange={e => updateInput(e, setter)}
          placeholder={label}
          type={type}
          id={name}
          name={name}
          autoComplete="off"
        />
      </div>
    </div>
  );
};

export const renderNavLink = (title, target) => {
  return (
    <NavLink exact to={target}>
      <button className="button" title={title}>
        <b>{title}</b>
      </button>
    </NavLink>
  );
};

export const renderButton = (title, onClickHandler, isDisabled) => {
  return (
    <button
      disabled={isDisabled}
      className="button"
      title={title}
      onClick={onClickHandler}
    >
      {title}
    </button>
  );
};
