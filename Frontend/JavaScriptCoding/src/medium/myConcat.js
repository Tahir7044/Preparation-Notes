/**
 * @template T
 * @param {...(T | Array<T>)} items
 * @return {Array<T>}
 */
Array.prototype.myConcat = function (...items) {
  const array = Array.from(this);
  let n = array.length;
  items.forEach((e) => {
    if (Array.isArray(e)) {
      const len = e.length;
      let k = 0;
      while (k < len) {
        const exists = Object.hasOwn(e, k);
        if (exists) {
          const subElement = e[k];
          array[n] = subElement;
        }
        n += 1;
        k += 1;
      }
    } else {
      array[n] = e;
      n += 1;
    }
  });
  return array;
};
