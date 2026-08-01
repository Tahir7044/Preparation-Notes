export function isBoolean(value) {
  return typeof value === "boolean";
}

export function isNumber(value) {
  return typeof value === "number";
}

export function isNull(value) {
  return value === null;
}

export function isString(value) {
  return typeof value === "string";
}

export function isSymbol(value) {
  return typeof value === "symbol";
}

export function isUndefined(value) {
  return typeof value === "undefined";
}

export function isArray(value) {
  return Array.isArray(value);
}

export function isFunction(value) {
  return typeof value === "function";
}

export function isObject(value) {
  if (value == null) {
    return false;
  }
  return typeof value === "object" || isFunction(value);
}

export function isPlainObject(value) {
  if (value == null) {
    return false;
  }
  const prototype = Object.getPrototypeOf(value);
  return prototype === null || prototype === Object.prototype;
}
