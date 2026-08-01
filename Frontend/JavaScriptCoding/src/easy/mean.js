/**
 * @param {Array} array - Array from which the elements are all numbers.
 * @return {Number} Returns mean.
 */
export default function mean(array) {
    const len = array.length
    if(len==0) {
        return NaN;
    }
    return array.reduce((accum, cur)=>{
        return accum+cur
    },0)/len;
}