/**
 * Calls the function, substituting the specified object for the this value of the function, and the specified array for the arguments of the function.
 * @param context The object to be used as the this object.
 * @param argArray A set of arguments to be passed to the function.
 * @return {any}
 */
Function.prototype.myApply = function (context, argArray=[]) {
    const callingFunction = this;
    if(typeof callingFunction !== 'function') {
        throw new TypeError('Function.prototype.myApply called on non-function');
    }
    if(!context) {
        context = {}
    }
    const symbol = Symbol("function");
    context[symbol] = callingFunction;
    return context[symbol](...argArray)
  };