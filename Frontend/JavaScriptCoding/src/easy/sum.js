/**
 * @param {number} value
 * @return {Function}
 */
export default function sum(value) {
  return function (val) {
    return val != null ? sum(val + value) : value;
  };
}
