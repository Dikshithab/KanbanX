import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import api from "../api/axios";
import "../styles/login.css"; // Make sure this path correctly points to your CSS file

function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();

    try {
      const response = await api.post("/users/login", {
        email,
        password,
      });

      if (response.data === "Invalid Credentials") {
        alert("Invalid Email or Password");
        return;
      }
      
      localStorage.setItem("token", response.data);
      alert("Login Successful");
      navigate("/dashboard");
    } catch (error) {
      console.error(error);

      if (error.response) {
        alert("Status: " + error.response.status);
        console.log(error.response.data);
      } else if (error.request) {
        alert("No response from backend (CORS or Backend Down)");
      } else {
        alert(error.message);
      }
    }
  };

  return (
    <div className="login-page">
      <div className="login-card">
        <h2>Kanban Login</h2>
        <p className="login-subtitle">Welcome back! Please enter your details.</p>

        <form onSubmit={handleLogin} className="login-form">
          <div className="input-group">
            <input
              type="email"
              placeholder="Email Address"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
          </div>

          <div className="input-group">
            <input
              type="password"
              placeholder="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </div>

          <button type="submit" className="login-btn">
            Sign In
          </button>
        </form>

        <div className="login-footer">
          Don't have an account?{" "}
          <Link to="/register" className="login-link">
            Register here
          </Link>
        </div>
      </div>
    </div>
  );
}

export default Login;