import { useContext, useState, useEffect } from "react";
import { AppContext } from "../../Context/AppContext";
import { deleteItem } from "../../Service/ItemService";
import toast from "react-hot-toast";

const ItemList = () => {

    const { itemsData, loadItems, loadCategories } = useContext(AppContext);
    const [searchTerm, setSearchTerm] = useState("");

    // 🔍 Filter items
    const filteredItems = itemsData.filter((item) =>
        item.name.toLowerCase().includes(searchTerm.toLowerCase())
    );

    useEffect(() => {
        console.log(itemsData);
    }, [itemsData]);

    // 🗑 Delete item
    const removeItem = async (itemId) => {
        try {
            await deleteItem(itemId);

            // 🔥 Reload both items and categories
            await loadItems();
            await loadCategories();

            toast.success("Item deleted successfully");

        } catch (error) {
            console.error(error.response?.data || error.message);
            toast.error("Unable to delete item");
        }
    };

    return (
        <div
            className="category-list-container"
            style={{ height: "100vh", overflowY: "auto" }}
        >

            {/* 🔍 Search Bar */}
            <div className="row p-2">
                <div className="input-group mb-3">
                    <input
                        type="text"
                        className="form-control"
                        placeholder="Search by keyword"
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                    />
                    <span className="input-group-text bg-warning">
                        <i className="bi bi-search"></i>
                    </span>
                </div>
            </div>

            {/* 📦 Items List */}
            <div className="row g-3 p-2">
                {filteredItems.map((item) => (
                    <div className="col-12" key={item.itemId}>
                        <div className="card p-3 bg-dark text-white">

                            <div className="d-flex align-items-center justify-content-between">

                                {/* Left Section */}
                                <div className="d-flex align-items-center">

                                    {/* Image */}
                                    <div style={{ width: "80px", marginRight: "15px" }}>
                                        <img
                                            src={`http://localhost:8080/api/v1.0/uploads/${item.imgUrl}`}
                                            alt={item.name}
                                            width="60"
                                            height="60"
                                            style={{ objectFit: "cover", borderRadius: "8px" }}
                                        />
                                    </div>

                                    {/* Details */}
                                    <div>
                                        <h6 className="mb-1">{item.name}</h6>

                                        <p className="mb-0">
                                            Category: {item.categoryName}
                                        </p>

                                        <span className="badge bg-warning text-dark mt-1">
                                            ₹{item.price}
                                        </span>
                                    </div>

                                </div>

                                {/* Delete Button */}
                                <button
                                    className="btn btn-danger btn-sm"
                                    onClick={() => removeItem(item.itemId)}
                                >
                                    <i className="bi bi-trash"></i>
                                </button>

                            </div>

                        </div>
                    </div>
                ))}
            </div>

        </div>
    );
};

export default ItemList;