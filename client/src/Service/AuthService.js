import api from "./axiosConfig";

export const login = async (data) => {
  return await api.post("/auth/login", data);
};