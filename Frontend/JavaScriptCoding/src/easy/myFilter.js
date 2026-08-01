/**
 * @template T
 * @param { (value: T, index: number, array: Array<T>) => boolean } callbackFn
 * @param {any} [thisArg]
 * @return {Array<T>}
 */
Array.prototype.myFilter = function (callbackFn, thisArg = {}) {
  const array = this;
  const len = array.length;
  let result = [];
  const bindFunction = callbackFn.bind(thisArg);
  for (let i = 0; i < len; i++) {
    if (Object.hasOwn(array, i) && bindFunction(array[i], i, array)) {
      result.push(array[i]);
    }
  }
  return result;
};
