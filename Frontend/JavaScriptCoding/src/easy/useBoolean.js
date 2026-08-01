/**
 * @param boolean initialValue
 * @return Object
 */
import { useState, useCallback } from "react";

export default function useBoolean(initialValue = false) {
  const [value, setValue] = useState(initialValue);

  const memoSetTrue = useCallback(() => {
    setValue(true);
  }, [value]);

  const memoSetFalse = useCallback(() => {
    setValue(false);
  }, [value]);

  return {
    value,
    setFalse: memoSetFalse,
    setTrue: memoSetTrue,
  };
}
