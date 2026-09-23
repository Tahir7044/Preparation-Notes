import React from "react";

export const Profile = ({ state, setState, errors }) => {
  const { name, age, email } = state;

  const handleChange = (e) => {
    const key = e.target.name;
    let value = e.target.value;

    if(key=="age"){
      value = value.replace(/\D/g, '');
    }

    setState((pre) => ({
      ...pre,
      [key]: value,
    }));
  };

  return (
    <form style={{ display: "flex", flexDirection: "column", gap: "16px" }}>
      <div style={{ display: "flex", flexDirection: "column" }}>
        <label htmlFor="name">Name 
            <input
            id="name"
            className="form-input"
            type="text"
            name="name"
            value={name}
            onChange={handleChange}
            />
        </label>
        {errors.name && <span className="error"> {errors.name} </span>}
      </div>
      <div style={{ display: "flex", flexDirection: "column" }}>
        <label htmlFor="age">Age 
        <input
          id="age"
          className="form-input"
          type="text"
          name="age"
          value={age}
          onChange={handleChange}
        />
        </label>
          {errors.age && <span className="error"> {errors.age} </span>}
      </div>
      <div style={{ display: "flex", flexDirection: "column" }}>
        <label htmlFor="email">Email 
        <input
          id="email"
          className="form-input"
          required
          type="email"
          name="email"
          value={email}
          onChange={handleChange}
        />
        </label>
        {errors.email && <span className="error"> {errors.email} </span>}
      </div>
    </form>
  );
};
