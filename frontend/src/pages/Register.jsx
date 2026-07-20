import { useState } from "react";
import { Link } from "react-router-dom";
import { FaUser, FaEnvelope, FaLock, FaUserPlus } from "react-icons/fa";
import "../styles/Register.css";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import api from "../api/axios";

function Register() {
  const [formData, setFormData] = useState({
    username: "",
    email: "",
    password: "",
    confirmPassword: "",
  });
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
  e.preventDefault();

  if (formData.password !== formData.confirmPassword) {
    toast.error("Passwords do not match");
    return;
  }

  try {
    const response = await api.post("/users/register", {
      name: formData.username,
      email: formData.email,
      password: formData.password,
    });

    console.log(response.data);

    toast.success("Registration Successful");

    navigate("/");
  } catch (error) {
    console.log(error);
    console.log(error.response);

    toast.error(error.response?.data || "Registration Failed");
  }
};

  return (
    <div className="register-container">
      {/* Left side: Branding/Visuals */}
      <div className="register-brand-section">
        <div className="brand-overlay"></div>
        <div className="brand-content">
          <h1>🚀 Get Started</h1>
          <p>Create an account to organize tasks, manage custom dashboards, and boost your daily workflow efficiency.</p>
        </div>
      </div>

      {/* Right side: Interactive Register Form */}
      <div className="register-form-section">
        <div className="form-wrapper">
          <h2>Create Account</h2>
          <p className="subtitle">Join us and start organizing today</p>

          <form onSubmit={handleSubmit} className="auth-form">
            {/* Username input */}
            <div className="input-group">
              <FaUser className="input-icon" />
              <input
                type="text"
                name="username"
                placeholder="Username"
                value={formData.username}
                onChange={handleChange}
                required
              />
            </div>

            {/* Email input */}
            <div className="input-group">
              <FaEnvelope className="input-icon" />
              <input
                type="email"
                name="email"
                placeholder="Email Address"
                value={formData.email}
                onChange={handleChange}
                required
              />
            </div>

            {/* Password input */}
            <div className="input-group">
              <FaLock className="input-icon" />
              <input
                type="password"
                name="password"
                placeholder="Password"
                value={formData.password}
                onChange={handleChange}
                required
              />
            </div>

            {/* Confirm Password input */}
            <div className="input-group">
              <FaLock className="input-icon" />
              <input
                type="password"
                name="confirmPassword"
                placeholder="Confirm Password"
                value={formData.confirmPassword}
                onChange={handleChange}
                required
              />
            </div>

            <button type="submit" className="submit-btn">
              <FaUserPlus /> Register
            </button>
          </form>

          <div className="auth-footer">
            <p>
              Already have an account? 
              <Link to="/">Sign In</Link>
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Register;