import { useState } from "react";
import { deleteUser } from "../../Service/UserService";
import toast from "react-hot-toast";

const UsersList = ({ users, setUsers }) => {

  const [searchTerm, setSearchTerm] = useState("");

  // Filter users by name
  const filteredUsers = users.filter(user =>
    user.name?.toLowerCase().includes(searchTerm.toLowerCase())
  );

  // Delete user
  const deleteByUserId = async (id) => {
    try {
      await deleteUser(id);

      // remove deleted user from state
      setUsers(prevUsers =>
        prevUsers.filter(user => user.userId !== id)
      );

      toast.success("User deleted successfully");
    } catch (error) {
      console.error(error);
      toast.error("Error deleting user");
    }
  };

  return (
    <div
      className="category-list-container"
      style={{ height: "100vh", overflowY: "auto" }}
    >
      <div className="row p-3">

        {/* 🔍 Search Box */}
        <div className="col-12 mb-3">
          <div className="input-group">
            <input
              type="text"
              placeholder="Search by name..."
              className="form-control"
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
            <span className="input-group-text bg-warning">
              <i className="bi bi-search"></i>
            </span>
          </div>
        </div>

        {/* 👥 Users List */}
        <div className="col-12">
          <div className="row g-3">
            {filteredUsers.length === 0 ? (
              <p className="text-center text-muted">No users found</p>
            ) : (
              filteredUsers.map((user, index) => (
                <div key={index} className="col-12">
                  <div className="card p-3 bg-dark text-white">
                    <div className="d-flex align-items-center justify-content-between">
                      
                      <div>
                        <h5 className="mb-1">{user.name}</h5>
                        <p className="mb-0">{user.email}</p>
                       
                      </div>

                      <button
                        className="btn btn-danger"
                        onClick={() => deleteByUserId(user.userId)}
                      >
                        <i className="bi bi-trash"></i>
                      </button>

                    </div>
                  </div>
                </div>
              ))
            )}
          </div>
        </div>

      </div>
    </div>
  );
};

export default UsersList;