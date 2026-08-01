/**
 * @param {Function} callback
 * @param {number} delay
 * @param {...any} args
 * @returns {Function}
 */
export default function setCancellableTimeout(callback, delay, ...args) {
    const timeoutId = setTimeout(() => {
        callback(...args)
    }, delay);

    return function cancel() {
        clearTimeout(timeoutId);
    }
  }