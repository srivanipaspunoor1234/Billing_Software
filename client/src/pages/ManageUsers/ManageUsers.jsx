import { useEffect, useState } from "react";
import { fetchUsers } from "../../Service/UserService";
import UserForm from "../../components/UserForm/UserForm";
import UsersList from "../../components/UsersList/UsersList";
import toast from "react-hot-toast";
import "./ManageUsers.css";

const ManageUsers = () => {

  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {

    const loadUsers = async () => {
      try {
        setLoading(true);
        const response = await fetchUsers();
        setUsers(response.data);
      } catch (error) {
        console.error(error);
        toast.error("Unable to fetch users");
      } finally {
        setLoading(false);
      }
    };

    loadUsers();

  }, []);

  return (
    <div className="users-container text-light">
      <div className="left-column">
        <UserForm setUsers={setUsers} />
      </div>

      <div className="right-column">
        <UsersList users={users} setUsers={setUsers} />
      </div>
    </div>
  );
};

export default ManageUsers;