import "./Item.css";
import { useContext } from "react";
import { AppContext } from "../../Context/AppContext.jsx";

const Item = ({ itemName, itemPrice, itemImage, itemId }) => {

  const { addToCart } = useContext(AppContext);

  const handleAddToCart = () => {
    addToCart({
      name: itemName,
      price: itemPrice,
      quantity: 1,
      itemId: itemId
    });
  };

  return (
    <div className="p-3 bg-dark rounded shadow-sm h-100 d-flex align-items-center item-card">

      {/* Image */}
      <div style={{ position: "relative", marginRight: "15px" }}>
        <img
          src={itemImage}
          alt={itemName}
          className="item-image"
        />
      </div>

      {/* Item Info */}
      <div className="flex-grow-1 ms-2">
        <h6 className="mb-1 text-light">{itemName}</h6>
        <p className="mb-0 text-light fw-bold">₹{itemPrice}</p>
      </div>

      {/* Actions */}
      <div className="d-flex align-items-center gap-2">

        <i
          className="bi bi-cart-plus text-warning"
          style={{ cursor: "pointer", fontSize: "20px" }}
          onClick={handleAddToCart}
        ></i>

        <button
          className="btn btn-success btn-sm"
          onClick={handleAddToCart}
        >
          <i className="bi bi-plus"></i>
        </button>

      </div>

    </div>
  );
};

export default Item;