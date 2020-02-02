import React, { useState } from "react";
import useInterval from "./util/useInterval";

const Counter = () => {
  const [imageIndex, setImageIndex] = useState(0);

  useInterval(() => {
    // Your custom logic here
    const newImageIndex = imageIndex >= 3 ? 0 : imageIndex + 1;
    setImageIndex(newImageIndex);
  }, 1000);

  return <h1>{imageIndex}</h1>;
};
export default Counter;
