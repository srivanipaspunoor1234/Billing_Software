import api from "./axiosConfig";

export const addUser = async (user) => {
  return await api.post("/admin/register", user);
};

export const deleteUser = async (id) => {
  return await api.delete(`/admin/users/${id}`);
};

export const fetchUsers = async () => {
  return await api.get("/admin/users");
};