import { useState } from "react";
import { addUser, fetchUsers } from "../../Service/UserService";
import toast from "react-hot-toast";

const UserForm = ({ setUsers }) => {

  const [data, setData] = useState({
    name: "",
    email: "",
    password: "",
    role: "ROLE_USER",
  });

  const [loading, setLoading] = useState(false);

  // ✅ Handle input change
  const onChangeHandler = (e) => {
    const { name, value } = e.target;
    setData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  // ✅ Handle form submit
  const onSubmitHandler = async (e) => {
    e.preventDefault();

    try {
      setLoading(true);

      await addUser(data);

      toast.success("User added successfully");

      // refresh users list
      const response = await fetchUsers();
      setUsers(response.data);

      // reset form
      setData({
        name: "",
        email: "",
        password: "",
        role: "ROLE_USER",
      });

    } catch (error) {
      console.error(error);
      toast.error("Unable to add user");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="mx-2 mt-2">
      <div className="row">
        <div className="card col-md-12 form-container">
          <div className="card-body">
            <form onSubmit={onSubmitHandler}>

              <div className="mb-3">
                <label htmlFor="name" className="form-label">Name</label>
                <input
                  type="text"
                  id="name"
                  name="name"
                  className="form-control"
                  placeholder="John Doe"
                  value={data.name}
                  onChange={onChangeHandler}
                  required
                />
              </div>

              <div className="mb-3">
                <label htmlFor="email" className="form-label">Email</label>
                <input
                  type="email"
                  id="email"
                  name="email"
                  className="form-control"
                  placeholder="yourname@example.com"
                  value={data.email}
                  onChange={onChangeHandler}
                  required
                />
              </div>

              <div className="mb-3">
                <label htmlFor="password" className="form-label">Password</label>
                <input
                  type="password"
                  id="password"
                  name="password"
                  className="form-control"
                  placeholder="********"
                  value={data.password}
                  onChange={onChangeHandler}
                  required
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
  );
};

export default UserForm;