import "./CartSummary.css";
import { useContext, useState } from "react";
import { AppContext } from "../../Context/AppContext";
import ReceiptPopup from "../ReceiptPopup/ReceiptPopup";

import { createOrder } from "../../Service/OrderService";
import { createRazorpayOrder, verifyPayment } from "../../Service/PaymentService";

import toast from "react-hot-toast";
import { AppConstants } from "../../util/constants";

const CartSummary = ({ customerName, mobileNumber, setCustomerName, setMobileNumber }) => {

  console.log("CartSummary component rendered");

  const { cartItems, clearCart } = useContext(AppContext);

  const [isProcessing, setIsProcessing] = useState(false);
  const [orderDetails, setOrderDetails] = useState(null);
  const [showPopup, setShowPopup] = useState(false);
  const [paymentMode, setPaymentMode] = useState("");

  const totalAmount = cartItems.reduce(
    (total, item) => total + item.price * item.quantity,
    0
  );

  const tax = totalAmount * 0.01;
  const grandTotal = totalAmount + tax;

  const clearAll = () => {
    setCustomerName("");
    setMobileNumber("");
    clearCart();
  };

  const placeOrder = async () => {

    if (!customerName || !mobileNumber) {
      toast.error("Please enter customer details");
      return;
    }

    if (cartItems.length === 0) {
      toast.error("Your cart is empty");
      return;
    }

    if (!paymentMode) {
      toast.error("Please select payment mode");
      return;
    }

    const orderData = {
      customerName,
      phoneNumber: mobileNumber,
      cartItems,
      subtotal: totalAmount,
      tax,
      grandTotal,
      paymentMethod: paymentMode.toUpperCase()
    };

    setIsProcessing(true);

    try {

      const response = await createOrder(orderData);
      const savedData = response.data;

      if (response.status === 201) {

        if (paymentMode === "cash") {

          toast.success("Order placed");

          setOrderDetails(savedData);
          setShowPopup(true);
          clearAll();
        }

        if (paymentMode === "upi") {

          const razorpayResponse = await createRazorpayOrder(savedData.id);

          const options = {
            key: AppConstants.RAZORPAY_KEY,
            amount: razorpayResponse.data.amount,
            currency: "INR",
            name: "Retail Billing",
            description: "Order Payment",
            order_id: razorpayResponse.data.id,

            handler: async function (response) {

              try {

                await verifyPayment(response);

                toast.success("Payment Successful");

                setOrderDetails(savedData);
                setShowPopup(true);
                clearAll();

              } catch (error) {

                toast.error("Payment verification failed");
                console.error(error);

              }

            },

            prefill: {
              name: customerName,
              contact: mobileNumber
            },

            theme: {
              color: "#3399cc"
            }

          };

          const rzp = new window.Razorpay(options);
          rzp.open();

        }

      }

    } catch (error) {

      console.error(error);
      toast.error("Order failed");

    } finally {

      setIsProcessing(false);

    }

  };

  return (

    <div className="cart-summary-container">

      <div className="cart-summary-details">

        <div className="d-flex justify-content-between mb-2">
          <span className="text-light">Items :</span>
          <span className="text-light">₹{totalAmount.toFixed(2)}</span>
        </div>

        <div className="d-flex justify-content-between mb-2">
          <span className="text-light">Tax (1%) :</span>
          <span className="text-light">₹{tax.toFixed(2)}</span>
        </div>

        <div className="d-flex justify-content-between mb-3">
          <span className="text-light fw-bold">Total :</span>
          <span className="text-light fw-bold">₹{grandTotal.toFixed(2)}</span>
        </div>

      </div>

      <div className="d-flex gap-3 mb-2">

        <button
          className={`btn flex-grow-1 fw-bold ${paymentMode === "cash" ? "btn-success" : "btn-outline-success"}`}
          onClick={() => setPaymentMode("cash")}
        >
          Cash
        </button>

        <button
          className={`btn flex-grow-1 fw-bold ${paymentMode === "upi" ? "btn-primary" : "btn-outline-primary"}`}
          onClick={() => setPaymentMode("upi")}
        >
          UPI
        </button>

      </div>

      <button
        className="btn btn-warning w-100 fw-bold"
        onClick={placeOrder}
        disabled={isProcessing}
      >
        Place Order
      </button>

      {showPopup && (
        <ReceiptPopup
          orderDetails={orderDetails}
          setShowPopup={setShowPopup}
        />
      )}

    </div>

  );

};

export default CartSummary;