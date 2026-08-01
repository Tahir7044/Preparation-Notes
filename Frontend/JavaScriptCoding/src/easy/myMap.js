/**
 * @template T
 * @param { (value: T, index: number, array: Array<T>) => T } callbackFn
 * @param {any} [thisArg]
 * @return {Array<T>}
 */
Array.prototype.myMap = function (callbackFn, thisArg = {}) {
  const array = this;
  const len = array.length;
  let result = new Array(len);
  const bindFunction = callbackFn.bind(thisArg);
  for (let i = 0; i < len; i++) {
    if (Object.hasOwn(array, i)) {
      const value = array[i];
      result[i] = bindFunction(value, i, array);
    }
  }
  return result;
};
