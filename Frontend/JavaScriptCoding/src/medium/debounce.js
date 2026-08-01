/**
 * @param {Function} func
 * @param {number} wait
 * @return {Function}
 */
export default function debounce(func, wait) {
  let timer = null;
  return function (...args) {
    clearTimeout(timer);
    timer = setTimeout(() => {
      func.apply(this, args);
    }, wait);
  };
}

//with return value

// function debounce( callback, delay ) {
//     let timer;
//     return (...args) => {
//       return new Promise( ( resolve, reject ) => {
//         clearTimeout(timer);
//         timer = setTimeout(() => {
//             try {
//               let output = callback(...args);
//               resolve(output);
//             } catch (err) {
//               reject(err);
//             }
//         }, delay);
//       })
//     }
//   }
