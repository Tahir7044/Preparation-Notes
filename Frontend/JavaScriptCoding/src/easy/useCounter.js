/**
 * @param number initialValue
 * @return Object
 */
import { useCallback, useState } from "react";
export default function useCounter(initialValue = 0) {
  const [count, setCounter] = useState(initialValue);

  const increment = useCallback(() => {
    setCounter((prev) => prev + 1);
  }, []);

  const decrement = useCallback(() => {
    setCounter((prev) => prev - 1);
  }, []);

  const reset = useCallback(() => {
    setCounter(initialValue);
  }, []);

  const setCount = useCallback((val) => {
    setCounter(val);
  }, []);

  return {
    count,
    increment,
    decrement,
    reset,
    setCount,
  };
}
