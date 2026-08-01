/**
 * @param {Function} func
 * @return {Function}
 */
export default function curry(func) {
  return function curried(...args) {
    if (func.length <= args.length) {
      return func.apply(this, args);
    }
    return curried.bind(this, ...args);
  };
}
