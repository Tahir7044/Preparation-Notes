
/**
 * @return {Array<number>}
 */
// @ts-ignore
Array.prototype.square = function () {
  return this.map( item => item*item);
};