/**
 * @param {any} context
 * @param {...*} argArray
 * @return {Function}
 */
Function.prototype.myBind = function (context = {}, ...argArray) {
  const fn = this;
  return function (...args) {
    const symbol = Symbol();
    context[symbol] = fn;
    return context[symbol](...[...argArray, ...args]);
  };
};
