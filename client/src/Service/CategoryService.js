import api from "./axiosConfig";

export const addCategory = async (formData) => {
  return await api.post("/admin/categories", formData);
};

export const deleteCategory = async (categoryId) => {
  return await api.delete(`/admin/categories/${categoryId}`);
};

export const fetchCategories = async () => {
  return await api.get("/admin/categories");
};