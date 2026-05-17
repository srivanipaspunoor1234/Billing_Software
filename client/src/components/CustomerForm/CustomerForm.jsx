import './CustomerForm.css';

const CustomerForm = ({ customerName, mobileNumber, setCustomerName, setMobileNumber }) => {

    return (
        <div className="p-3">

            {/* Customer Name */}
            <div className="mb-3">
                <div className="d-flex align-items-center gap-2">
                    <label htmlFor="customerName" className="col-4">
                        Customer name
                    </label>

                    <input
                        type="text"
                        id="customerName"
                        className="form-control form-control-sm"
                        placeholder="Enter name"
                        value={customerName}
                        onChange={(e) => setCustomerName(e.target.value)}
                    />
                </div>
            </div>

            {/* Mobile Number */}
            <div className="mb-3">
                <div className="d-flex align-items-center gap-2">
                    <label htmlFor="mobileNumber" className="col-4">
                        Mobile number
                    </label>

                    <input
                        type="text"
                        id="mobileNumber"
                        className="form-control form-control-sm"
                        placeholder="Enter mobile number"
                        value={mobileNumber}
                        onChange={(e) => setMobileNumber(e.target.value)}
                    />
                </div>
            </div>

        </div>
    );
};

export default CustomerForm;