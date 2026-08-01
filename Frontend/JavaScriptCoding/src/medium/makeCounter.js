/**
 * @param {number} initialValue
 * @return {{get: Function, increment: Function, decrement: Function, reset: Function }}
 */
export default function makeCounter(initialValue = 0) {
  let value = initialValue;
  const increment = () => {
    value = value + 1;
    return value;
  };

  const decrement = () => {
    value = value - 1;
    return value;
  };

  const reset = () => {
    value = initialValue;
    return value;
  };

  const get = () => {
    return value;
  };

  return {
    increment,
    decrement,
    reset,
    get,
  };
}
