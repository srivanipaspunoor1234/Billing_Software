import React from "react";

const SearchBox = ({ searchText, setSearchText }) => {

  const handleInputChange = (e) => {
    setSearchText(e.target.value);
  };

  return (
    <div className="input-group input-group-sm">
      <input
        type="text"
        className="form-control"
        placeholder="Search items.."
        value={searchText}
        onChange={handleInputChange}
      />

      <span className="input-group-text bg-warning">
        <i className="bi bi-search"></i>
      </span>
    </div>
  );
};

export default SearchBox;