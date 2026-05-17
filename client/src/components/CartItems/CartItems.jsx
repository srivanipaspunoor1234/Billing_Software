import './CartItems.css';
import { useContext } from "react";
import { AppContext } from "../../Context/AppContext.jsx";

const CartItems = () => {

    const { cartItems, increaseQuantity, decreaseQuantity, removeFromCart } = useContext(AppContext);

    return (
        <div className="p-3 ">

            {cartItems.length === 0 ? (
                <p className="text-light">Your cart is empty.</p>
            ) : (
                <div className="cart-items-list">

                    {cartItems.map((item, index) => (

                        <div
                            key={index}
                            className="cart-item mb-3 p-3 bg-dark rounded"
                        >

                            {/* Item name + price */}
                            <div className="d-flex justify-content-between align-items-center mb-2">

                                <h6 className="mb-0 text-light">
                                    {item.name}
                                </h6>

                                <p className="mb-0 text-light">
                                    ₹{(item.price * item.quantity).toFixed(2)}
                                </p>

                            </div>

                            {/* Quantity controls */}
                            <div className="d-flex justify-content-between align-items-center">

                                <div className="d-flex align-items-center gap-2">

                                    <button
                                        className="btn btn-danger btn-sm"
                                        disabled={item.quantity === 1}
                                        onClick={() => decreaseQuantity(item.itemId)}
                                    >
                                        <i className="bi bi-dash"></i>
                                    </button>

                                    <span className="text-light">
                                        {item.quantity}
                                    </span>

                                    <button
                                        className="btn btn-primary btn-sm"
                                        onClick={() => increaseQuantity(item.itemId)}
                                    >
                                        <i className="bi bi-plus"></i>
                                    </button>

                                </div>

                                {/* Delete button */}
                                <button
                                    className="btn btn-danger btn-sm"
                                    style={{ width: "auto" }}
                                    onClick={() => removeFromCart(item.itemId)}
                                >
                                    <i className="bi bi-trash"></i>
                                </button>

                            </div>

                        </div>

                    ))}

                </div>
            )}

        </div>
    );
};

export default CartItems;