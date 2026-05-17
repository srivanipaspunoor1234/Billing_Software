import Menubar from "./components/Menubar/Menubar";
import ManageItems from "./pages/ManageItems/ManageItems";
import ManageUsers from "./pages/ManageUsers/ManageUsers";
import { Routes, Route, useLocation } from "react-router-dom";
import Dashboard from "./pages/Dashboard/Dashboard";
import ManageCategory from "./pages/ManageCategory/ManageCategory";
import Explore from "./pages/Explore/Explore";
import { Toaster } from "react-hot-toast";
import Login from "./pages/Login/Login";

const App = () => {
  const location = useLocation();
  return (
    <>
    {location.pathname!== '/login' && <Menubar />}
      
      {/* Toast Container */}
      <Toaster position="top-right" reverseOrder={false} />

      <Routes>
        <Route path="/" element={<Dashboard />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/category" element={<ManageCategory />} />
        <Route path="/users" element={<ManageUsers />} />
        <Route path="/items" element={<ManageItems />} />
        <Route path="/explore" element={<Explore />} />
        <Route path="/login" element={<Login />} />
         <Route path="/dashboard" element={<Dashboard />} />
      </Routes>
    </>
  );
};

export default App;