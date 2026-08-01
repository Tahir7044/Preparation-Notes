/**
 * @param {any} context
 * @param {...*} argArray
 * @return {any}
 */
Function.prototype.myCall = function (context, ...argArray) {

    const callingFunction = this;
    if(typeof callingFunction !== 'function') {
        throw new TypeError('Function.prototype.myCall called on non-function');
    }
    if(!context) {
        context = {}
    }
    const symbol = Symbol("function");
    context[symbol] = callingFunction;
    return context[symbol](...argArray)
  
};