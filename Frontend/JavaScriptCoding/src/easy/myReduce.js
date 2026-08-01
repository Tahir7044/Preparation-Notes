/**
 * @template T, U
 * @param {(previousValue: U, currentValue: T, currentIndex: number, array: T[]) => U} callbackFn
 * @param {U} [initialValue]
 * @return {U}
 */
Array.prototype.myReduce = function (callbackFn, initialValue) {
  const array = this;
  const len = array.length;
  let i = 0;
  if (len === 0 && initialValue == null) {
    throw new Error("it is an empty array with no initial value");
  }
  let accums;
  if (initialValue == null) {
    accums = array[0];
    i++;
  } else {
    accums = initialValue;
  }
  for (; i < len; i++) {
    const value = array[i];
    if (Object.hasOwn(array, i)) {
      accums = callbackFn(accums, value, i, array);
    }
  }
  return accums;
};
