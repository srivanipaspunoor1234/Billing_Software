import api from "./axiosConfig";

// GET latest orders
export const latestOrders = async () => {
  return await api.get("/orders/latest");
};

// CREATE order
export const createOrder = async (order) => {
  return await api.post("/orders", order);
};

// DELETE order
export const deleteOrder = async (id) => {
  return await api.delete(`/orders/${id}`);
};