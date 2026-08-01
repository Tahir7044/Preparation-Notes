/**
 * @param {string} selector
 * @return {{css: Function}}
 */
export default function $(selector) {
  const element = document.querySelector(selector);
  return {
    css: function (property, value) {
      if (value === undefined) {
        if (element == null) {
          return undefined;
        }
        const value = element.style[property];
        return value === "" ? undefined : value;
      }
      if (element != null) {
        element.style[property] = value;
      }
      return this;
    },
  };
}
