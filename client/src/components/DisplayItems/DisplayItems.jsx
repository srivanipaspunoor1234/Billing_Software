import "./DisplayItems.css";
import { useContext, useState } from "react";
import { AppContext } from "../../Context/AppContext.jsx";
import Item from "../Item/Item.jsx";
import SearchBox from "../SearchBox/SearchBox.jsx";

const DisplayItems = ({ selectedCategory }) => {

  const { itemsData } = useContext(AppContext);
  const [searchText, setSearchText] = useState("");

  const filteredItems = itemsData
    .filter((item) => {
      if (!selectedCategory) return true;
      return item.categoryId === selectedCategory;
    })
    .filter((item) =>
      item.name.toLowerCase().includes(searchText.toLowerCase())
    );

  return (
    <div className="p-3">

      {/* Search Bar */}
      <div className="d-flex justify-content-end mb-4">
        <div style={{ width: "250px" }}>
          <SearchBox
            searchText={searchText}
            setSearchText={setSearchText}
          />
        </div>
      </div>

      {/* Items Grid */}
      <div className="row g-3">

        {filteredItems.map((item) => (
          <div
            className="col-12 col-sm-12 col-md-6 col-lg-6 col-xl-6"
            key={item.itemId}
          >
            <Item
              itemName={item.name}
              itemPrice={item.price}
              itemImage={`http://localhost:8080/api/v1.0/uploads/${item.imgUrl}`}
              itemId={item.itemId}
            />
          </div>
        ))}

      </div>

    </div>
  );
};

export default DisplayItems;