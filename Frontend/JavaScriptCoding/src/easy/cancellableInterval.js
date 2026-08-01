/**
 * @param {Function} callback
 * @param {number} delay
 * @param {...any} args
 * @returns {Function}
 */
export default function setCancellableInterval(callback, delay, ...args) {
    const intervalId = setInterval(() => {
        callback(...args)
    }, delay);

    return function cancel() {
        clearInterval(intervalId);
    }

  }