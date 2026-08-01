/**
 * @param {Array} array The array to iterate over.
 * @param {Function} iteratee The function invoked per iteration.
 * @returns {Object} Returns the composed aggregate object.
 */
export default function countBy(array, iteratee) {
  const result = {};
  const len = array.length;
  for (let i = 0; i < len; i++) {
    const value = iteratee(array[i]);
    if (!result[value]) {
      result[value] = 0;
    }
    result[value]++;
  }
  return result;
}
