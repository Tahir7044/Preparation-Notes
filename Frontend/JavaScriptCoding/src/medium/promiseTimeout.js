/**
 * @template T
 * @param {Promise<T>} promise
 * @param {number} duration
 * @return {Promise<T>}
 */
export default function promiseTimeout(promise, duration) {
  return new Promise((resolve, reject) => {
    Promise.resolve(promise).then(resolve, reject);
    setTimeout(() => reject("Promise timeout"), duration);
  });
}
