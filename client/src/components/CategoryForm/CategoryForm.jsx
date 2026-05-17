import { useState, useContext } from "react";
import { AppContext } from "../../Context/AppContext";
import { addCategory } from "../../Service/CategoryService"; // make sure this exists
import { assets } from "../../assets/assets";
import toast from "react-hot-toast";

const CategoryForm = () => {

  const { categories, setCategories } = useContext(AppContext);

  const [image, setImage] = useState(null);
  const [loading, setLoading] = useState(false);

  const [data, setData] = useState({
    name: "",
    description: "",
    bgColor: "#2c2c2c"
  });

  // Handle image selection
  const handleImageChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      setImage(file);
    }
  };

  // Handle input change
  const handleChange = (e) => {
    const { name, value } = e.target;
    setData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  // Submit handler
 const handleSubmit = async (e) => {
  e.preventDefault();

  if (!image) {
    toast.error("Select image for category");
    return;
  }

  try {
    setLoading(true);

    const formData = new FormData();

    // ✅ Wrap JSON inside Blob
    formData.append(
      "category",
      new Blob([JSON.stringify(data)], {
        type: "application/json"
      })
    );

    // ✅ Part name must be "image"
    formData.append("image", image);

    const response = await addCategory(formData);

    if (response.status === 201) {
      setCategories([...categories, response.data]);
      toast.success("Category added");

      setImage(null);
      setData({
        name: "",
        description: "",
        bgColor: "#2c2c2c"
      });
    }

  } catch (error) {
    console.error(error);
    toast.error("Error adding category");
  } finally {
    setLoading(false);
  }
};

  return (
    <div className="mx-2 mt-2">
      <div className="row">
        <div className="card col-md-12 form-container">
          <div className="card-body">

            <form onSubmit={handleSubmit}>

              {/* IMAGE UPLOAD */}
              <div className="mb-3">
                <label htmlFor="image" style={{ cursor: "pointer" }}>
                  <img
                    src={
                      image
                        ? URL.createObjectURL(image)
                        : assets.upload
                    }
                    alt="preview"
                    width={48}
                  />
                </label>

                <input
                  type="file"
                  id="image"
                  hidden
                  onChange={handleImageChange}
                />
              </div>

              {/* NAME */}
              <div className="mb-3">
                <label className="form-label">Name</label>
                <input
                  type="text"
                  name="name"
                  className="form-control"
                  value={data.name}
                  onChange={handleChange}
                />
              </div>

              {/* DESCRIPTION */}
              <div className="mb-3">
                <label className="form-label">Description</label>
                <textarea
                  rows="3"
                  name="description"
                  className="form-control"
                  value={data.description}
                  onChange={handleChange}
                />
              </div>

              {/* COLOR */}
              <div className="mb-3">
                <label className="form-label">Background Color</label><br />
                <input
                  type="color"
                  name="bgColor"
                  value={data.bgColor}
                  onChange={handleChange}
                />
              </div>

              <button type="submit" className="btn btn-warning w-100" disabled={loading}>
                {loading ? "Saving..." : "Save"}
              </button>

            </form>

          </div>
        </div>
      </div>
    </div>
  );
};

export default CategoryForm;