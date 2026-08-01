/**
 * @param {*} value The value to check.
 * @returns {boolean} Returns `true` if `value` is empty, else `false`.
 */
export default function isEmpty(value) {
  const type = typeof value;
  const prototype = Object.getPrototypeOf(value);
  const constructor = value.constructor;
  if (value == null) {
    return true;
  }
  if (type === "string" || Array.isArray(value)) {
    return value.length === 0;
  }

  if (prototype === Object.prototype || prototype === null) {
    return Object.keys(value).length === 0;
  }
  if (constructor === Map || constructor === Set) {
    return value.size === 0;
  }
  return true;
}
