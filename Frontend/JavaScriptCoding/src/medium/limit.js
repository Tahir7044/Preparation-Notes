/**
 * @callback func
 * @param {number} n
 * @return {Function}
 */
export default function limit(func, n) {
  let numberOfCalls = 0;
  let result = 0;
  return function (...args) {
    if (numberOfCalls < n) {
      numberOfCalls++;
      result = func.apply(this, args);
    }
    return result;
  };
}
