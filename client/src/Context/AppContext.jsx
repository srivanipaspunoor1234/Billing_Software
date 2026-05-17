import { createContext, useEffect, useState } from "react";
import { fetchCategories } from "../Service/CategoryService";
import { fetchItems } from "../Service/ItemService";

export const AppContext = createContext(null);

export const AppContextProvider = ({ children }) => {

  // ================= AUTH =================
  const [authData, setAuthState] = useState({
    token: localStorage.getItem("token"),
    role: localStorage.getItem("role"),
  });

  const setAuthData = (token, role) => {

    if (token && role) {
      localStorage.setItem("token", token);
      localStorage.setItem("role", role);
    } else {
      localStorage.removeItem("token");
      localStorage.removeItem("role");
    }

    setAuthState({ token, role });
  };

  const logout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("role");
    setAuthState({ token: null, role: null });
  };

  // ================= CATEGORIES =================
  const [categories, setCategories] = useState([]);

  const loadCategories = async () => {
    try {
      const response = await fetchCategories();
      setCategories(response.data);
    } catch (error) {
      console.error("Error loading categories", error);
    }
  };

  // ================= ITEMS =================
  const [itemsData, setItemsData] = useState([]);

  const loadItems = async () => {
    try {
      const response = await fetchItems();
      setItemsData(response.data);
    } catch (error) {
      console.error("Error loading items", error);
    }
  };

  // ================= CART =================
  const [cartItems, setCartItems] = useState([]);

  const addToCart = (item) => {

    const existingItem = cartItems.find(
      (cartItem) => cartItem.itemId === item.itemId
    );

    if (existingItem) {

      setCartItems(
        cartItems.map((cartItem) =>
          cartItem.itemId === item.itemId
            ? { ...cartItem, quantity: cartItem.quantity + 1 }
            : cartItem
        )
      );

    } else {

      setCartItems([
        ...cartItems,
        { ...item, quantity: 1 }
      ]);

    }
  };

  const increaseQuantity = (itemId) => {

    setCartItems(
      cartItems.map((item) =>
        item.itemId === itemId
          ? { ...item, quantity: item.quantity + 1 }
          : item
      )
    );

  };

  const decreaseQuantity = (itemId) => {

    setCartItems(
      cartItems.map((item) =>
        item.itemId === itemId
          ? { ...item, quantity: item.quantity - 1 }
          : item
      )
    );

  };

  const removeFromCart = (itemId) => {

    setCartItems(
      cartItems.filter((item) => item.itemId !== itemId)
    );

  };

  // ================= LOAD DATA =================
  useEffect(() => {
    loadCategories();
    loadItems();
  }, []);

  // ================= CONTEXT VALUE =================
  const contextValue = {

    authData,
    setAuthData,
    logout,
    isAuthenticated: !!authData.token,

    categories,
    loadCategories,

    itemsData,
    loadItems,

    cartItems,
    addToCart,
    increaseQuantity,
    decreaseQuantity,
    removeFromCart

  };

  return (
    <AppContext.Provider value={contextValue}>
      {children}
    </AppContext.Provider>
  );
};