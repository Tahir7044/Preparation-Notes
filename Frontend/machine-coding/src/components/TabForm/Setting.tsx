import React from "react";

const SettingsConfig = [
  {
    name: "Theme",
    options: ["dark", "light"],
    selectedValue: "light",
  },
];

export const Setting = ({ state, setState, errors }) => {
  const { settings } = state;

  const handleChange = (name, option) => {
    setState((pre) => ({
      ...pre,
        settings:{
            ...pre?.settings,
            [name]: option
        }
    }));
  };

  return (
    <form style={{ display: "flex", flexDirection: "column", gap: "16px" }}>
      {SettingsConfig.map((item) => (
        <>
          <h4>{item.name}</h4>
          {item.options.map((option) => (
            <div key={option}>
              <label>
                <input
                  style={{ width: "auto", height: "auto" }}
                  type="radio"
                  name={option}
                  checked={option === settings?.[item.name]}
                  onChange={()=> handleChange(item.name, option)}
                />
                {option}
              </label>
            </div>
          ))}
        </>
      ))}
      {errors.settings && <span className="error"> {errors.settings} </span>}
    </form>
  );
};
