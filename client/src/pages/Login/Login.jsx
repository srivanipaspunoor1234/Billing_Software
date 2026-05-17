import React, { useState, useContext } from "react";
import { useNavigate } from "react-router-dom";
import toast from "react-hot-toast";
import { login } from "../../Service/AuthService";
import { AppContext } from "../../Context/AppContext";
import "./Login.css";

const Login = () => {

  const navigate = useNavigate();
  const { setAuthData } = useContext(AppContext); // ✅ Context

  const [loading, setLoading] = useState(false);

  const [data, setData] = useState({
    email: "",
    password: "",
  });

  const onChangeHandler = (e) => {
    const { name, value } = e.target;

    setData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  const onSubmitHandler = async (e) => {
    e.preventDefault();

    if (!data.email || !data.password) {
      toast.error("Please fill all fields");
      return;
    }

    setLoading(true);

    try {
      const response = await login(data);

      const token = response.data.token;
      const role = response.data.role;

      // ✅ Save in localStorage
      localStorage.setItem("token", token);
      localStorage.setItem("role", role);

      // ✅ Save in global context
      setAuthData(token, role);

      toast.success("Login Successful");

      // ✅ Redirect based on role
      if (role === "ADMIN") {
        navigate("/admin/dashboard");
      } else {
        navigate("/dashboard");
      }

    } catch (error) {
      toast.error(
        error.response?.data?.message || "Email/Password Invalid"
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-background">
      <div className="card shadow-lg w-100" style={{ maxWidth: "480px" }}>
        <div className="card-body">

          <div className="text-center">
            <h1 className="card-title">Sign in</h1>
            <p className="card-text text-muted">
              Sign in below to access your account
            </p>
          </div>

          <div className="mt-4">
            <form onSubmit={onSubmitHandler}>

              <div className="mb-4">
                <label className="form-label text-muted">
                  Email address
                </label>
                <input
                  type="email"
                  className="form-control form-control-lg"
                  name="email"
                  value={data.email}
                  onChange={onChangeHandler}
                  required
                />
              </div>

              <div className="mb-4">
                <label className="form-label text-muted">
                  Password
                </label>
                <input
                  type="password"
                  className="form-control form-control-lg"
                  name="password"
                  value={data.password}
                  onChange={onChangeHandler}
                  required
                />
              </div>

              <div className="d-grid">
                <button
                  type="submit"
                  className="btn btn-dark btn-lg"
                  disabled={loading}
                >
                  {loading ? "Signing in..." : "Sign in"}
                </button>
              </div>

            </form>
          </div>

        </div>
      </div>
    </div>
  );
};

export default Login;