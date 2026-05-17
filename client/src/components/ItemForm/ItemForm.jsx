import { useState, useContext } from "react";
import { AppContext } from "../../Context/AppContext";
import { addItem } from "../../Service/ItemService";
import toast from "react-hot-toast";
import { assets } from "../../assets/assets";

const ItemForm = () => {

  const { categories, loadItems, loadCategories } = useContext(AppContext);

  const [image, setImage] = useState(null);
  const [loading, setLoading] = useState(false);

  const [data, setData] = useState({
    name: "",
    categoryId: "",
    price: "",
    description: ""
  });

  const onChangeHandler = (e) => {
    const { name, value } = e.target;

    setData((prev) => ({
      ...prev,
      [name]: value
    }));
  };

  const onSubmitHandler = async (e) => {
    e.preventDefault();

    if (!image) {
      toast.error("Select image");
      return;
    }

    if (!data.categoryId) {
      toast.error("Select category");
      return;
    }

    try {
      setLoading(true);

      const formData = new FormData();
      formData.append("item", JSON.stringify(data));
      formData.append("file", image);

      await addItem(formData);

      // 🔥 Reload everything
      await loadItems();
      await loadCategories();

      toast.success("Item added successfully");

      // Reset form
      setData({
        name: "",
        categoryId: "",
        price: "",
        description: ""
      });

      setImage(null);

    } catch (error) {
      console.error(error);
      toast.error("Unable to add item");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="item-form-container" style={{ height: "100vh", overflow: "auto" }}>
      <div className="mx-2 mt-2">
        <div className="row">
          <div className="card col-md-12 form-container">
            <div className="card-body">

              <form onSubmit={onSubmitHandler}>

                {/* Image Upload */}
                <div className="mb-3">
                  <label htmlFor="image" className="form-label">
                    <img
                      src={image ? URL.createObjectURL(image) : assets.upload}
                      alt="upload"
                      width={60}
                      style={{ cursor: "pointer" }}
                    />
                  </label>

                  <input
                    type="file"
                    id="image"
                    hidden
                    accept="image/*"
                    onChange={(e) => setImage(e.target.files[0])}
                  />
                </div>

                {/* Name */}
                <div className="mb-3">
                  <label className="form-label">Name</label>
                  <input
                    type="text"
                    name="name"
                    value={data.name}
                    onChange={onChangeHandler}
                    className="form-control"
                    required
                  />
                </div>

                {/* Category */}
                <div className="mb-3">
                  <label className="form-label">Category</label>
                  <select
                    name="categoryId"
                    value={data.categoryId}
                    onChange={onChangeHandler}
                    className="form-control"
                    required
                  >
                    <option value="">---SELECT CATEGORY---</option>
                    {categories.map((category) => (
                      <option
                        key={category.categoryId}
                        value={category.categoryId}
                      >
                        {category.name}
                      </option>
                    ))}
                  </select>
                </div>

                {/* Price */}
                <div className="mb-3">
                  <label className="form-label">Price</label>
                  <input
                    type="number"
                    name="price"
                    value={data.price}
                    onChange={onChangeHandler}
                    className="form-control"
                    required
                  />
                </div>

                {/* Description */}
                <div className="mb-3">
                  <label className="form-label">Description</label>
                  <textarea
                    rows="4"
                    name="description"
                    value={data.description}
                    onChange={onChangeHandler}
                    className="form-control"
                  />
                </div>

                <button
                  type="submit"
                  className="btn btn-warning w-100"
                  disabled={loading}
                >
                  {loading ? "Saving..." : "Save"}
                </button>

              </form>

            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ItemForm;