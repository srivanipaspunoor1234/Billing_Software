import { useContext, useState } from 'react';
import './Explore.css';
import { AppContext } from '../../Context/AppContext';
import DisplayCategory from '../../components/DisplayCategory/DisplayCategory';
import DisplayItems from '../../components/DisplayItems/DisplayItems';
import CustomerForm from '../../components/CustomerForm/CustomerForm';
import CartItems from "../../components/CartItems/CartItems.jsx";
import CartSummary from "../../components/CartSummary/CartSummary.jsx";
const Explore = () => {

    const { categories } = useContext(AppContext);
console.log("Explore page rendered");
    const [selectedCategory, setSelectedCategory] = useState("");
    const [customerName, setCustomerName] = useState("");
    const [mobileNumber, setMobileNumber] = useState("");

    return (
        
        <div className="explore-container text-light">

            {/* LEFT COLUMN */}
            <div className="left-column">

                <div className="first-row">
                    <DisplayCategory
                        selectedCategory={selectedCategory}
                        setSelectedCategory={setSelectedCategory}
                        categories={categories}
                    />
                </div>

                <hr className="horizontal-line" />

                <div className="second-row">
                    <DisplayItems selectedCategory={selectedCategory} />
                </div>

            </div>


            {/* RIGHT COLUMN */}
            <div className="right-column">

                <div className="customer-form-container">
                    <CustomerForm
                        customerName={customerName}
                        mobileNumber={mobileNumber}
                        setCustomerName={setCustomerName}
                        setMobileNumber={setMobileNumber}
                    />
                </div>

                <hr className="my-3 text-light" />

                <div className="cart-items-container">
                    <CartItems />
                </div>

                <CartSummary
                    customerName={customerName}
                    mobileNumber={mobileNumber}
                    setCustomerName={setCustomerName}
                    setMobileNumber={setMobileNumber}
                />

            </div>

        </div>
    );
};

export default Explore;