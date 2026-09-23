import React from "react";

const interestConfig = ["coding", "cricket", "football"];

export const Interest = ({ state, setState, errors }) => {
  const interests = state.interests? state.interests : [];



  const handleChange = (e) => {
    const key = e.target.name;
    const value = e.target.checked;
    const newInterest = value ? [...interests,key]: (interests|| []).filter(item=> item!==key);

    setState((pre) => ({
      ...pre,
      interests: newInterest
    }));
  };

  return (
    <form style={{ display: "flex", flexDirection: "column", gap: "16px" }}>
      {interestConfig.map((item) => (
        <div key={item}>
          <label>
            <input
              style={{ width: "auto", height: "auto" }}
              type="checkbox"
              name={item}
              checked={interests.includes(item)}
              onChange={handleChange}
            />
            {item}
          </label>
        </div>
      ))}
      {errors.interests && <span className="error"> {errors.interests} </span>}
    </form>
  );
};
