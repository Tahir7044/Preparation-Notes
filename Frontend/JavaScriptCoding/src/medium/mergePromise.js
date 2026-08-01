/**
 * @param {Promise} p1
 * @param {Promise} p2
 * @return {Promise<any>}
 */

function isPlainObject(value) {
  if (value == null) {
    return null;
  }
  const prototype = Object.getPrototypeOf(value);
  if (prototype === null || prototype === Object.prototype) {
    return true;
  }
}

export default function promiseMerge(p1, p2) {
  return new Promise(async (resolve, reject) => {
    try {
      const res1 = await p1;
      const res2 = await p2;
      const type1 = typeof res1;
      const type2 = typeof res2;
      if (type1 != type2) {
        reject("Unsupported data types");
      }
      if (type1 === "string" || type1 === "number") {
        resolve(res1 + res2);
      }
      if (Array.isArray(res1) && Array.isArray(res2)) {
        resolve([...res1, ...res2]);
      }
      if (isPlainObject(res1) && isPlainObject(res1)) {
        resolve({ ...res1, ...res2 });
      }
      reject("Unsupported data types");
    } catch (err) {
      reject(err);
    }
  });
}
