import { useEffect, useState } from "react";
import Sidebar from "../components/Sidebar";
import Navbar from "../components/Navbar";
import api from "../api/axios";
import { FaUserEdit, FaSave, FaTimes, FaTrophy, FaKey } from "react-icons/fa";
import "../styles/Profile.css";

function Profile() {
  const [user, setUser] = useState(null);
  const [editing, setEditing] = useState(false);
  const [form, setForm] = useState({ name: "", email: "" });
  const [boards, setBoards] = useState([]);
  const [passwordData, setPasswordData] = useState({ oldPassword: "", newPassword: "" });

  useEffect(() => {
    loadProfile();
    loadBoards();
  }, []);

  const loadProfile = async () => {
    try {
      const response = await api.get("/users/profile");
      setUser(response.data);
      setForm({ name: response.data.name, email: response.data.email });
    } catch (error) {
      console.error("Error loading profile details:", error);
    }
  };

  const updateProfile = async () => {
    try {
      const response = await api.put("/users/profile", form);
      setUser(response.data);
      setEditing(false);
      alert("Profile updated successfully!");
    } catch (error) {
      console.error(error);
      alert("Profile update failed.");
    }
  };

  const loadBoards = async () => {
    try {
      const response = await api.get("/boards");
      setBoards(response.data);
    } catch (error) {
      console.error(error);
    }
  };

  const changePassword = async (e) => {
    e.preventDefault();
    try {
      await api.post("/users/change-password", passwordData);
      alert("Password updated successfully!");
      setPasswordData({ oldPassword: "", newPassword: "" });
    } catch (error) {
      console.error(error);
      alert("Failed to modify password.");
    }
  };

  if (!user) {
    return (
      <div className="profile-loading">
        <h2>Loading Profile...</h2>
      </div>
    );
  }

  // Derived Project and Workflow Metrical Data
  const totalBoards = boards.length;
  const totalTasks = boards.reduce((cnt, b) => cnt + (b.tasks ? b.tasks.length : 0), 0);
  const completedTasks = boards.reduce((cnt, b) => cnt + (b.tasks ? b.tasks.filter(t => t.status === "DONE").length : 0), 0);
  const productivity = totalTasks === 0 ? 0 : Math.round((completedTasks / totalTasks) * 100);
  const today = new Date().toISOString().split("T")[0];
  const completedToday = boards.flatMap(b => b.tasks || []).filter(t => t.status === "DONE" && t.updatedAt && t.updatedAt.startsWith(today)).length;

  return (
    <>
      <Sidebar />
      <Navbar />

      <div className="profile-page">
        {/* Profile Card Context Row */}
        <div className="profile-layout-grid">
          
          <div className="profile-card">
            <div className="profile-avatar">
              {user.name.charAt(0).toUpperCase()}
            </div>

            {editing ? (
              <div className="profile-edit-inputs">
                <input
                  type="text"
                  value={form.name}
                  onChange={(e) => setForm({ ...form, name: e.target.value })}
                  placeholder="Name"
                />
                <input
                  type="email"
                  value={form.email}
                  onChange={(e) => setForm({ ...form, email: e.target.value })}
                  placeholder="Email"
                />
                <div className="edit-actions">
                  <button className="save-btn" onClick={updateProfile}><FaSave /> Save</button>
                  <button className="cancel-btn" onClick={() => setEditing(false)}><FaTimes /> Cancel</button>
                </div>
              </div>
            ) : (
              <div className="profile-details">
                <h1>{user.name}</h1>
                <p>{user.email}</p>
                <span className="role-tag">Frontend Developer</span>
                <button className="edit-toggle-btn" onClick={() => setEditing(true)}>
                  <FaUserEdit /> Edit Profile
                </button>
              </div>
            )}
          </div>

          {/* Productivity Stats Grid */}
          <div className="profile-stats">
            <div className="profile-box">
              <h2>{totalBoards}</h2>
              <p>Workspaces</p>
            </div>
            <div className="profile-box">
              <h2>{totalTasks}</h2>
              <p>Total Tasks</p>
            </div>
            <div className="profile-box">
              <h2>{productivity}%</h2>
              <p>Productivity</p>
            </div>
            <div className="profile-box">
              <h2>{completedToday}</h2>
              <p>Done Today</p>
            </div>
          </div>
        </div>

        {/* Lower Modules Section */}
        <div className="profile-modules">
          
          {/* Achievements Feature Wrapper */}
          <div className="achievement-card">
            <h2><FaTrophy className="module-icon gold" /> Badges & Achievements</h2>
            <div className="badges">
              {productivity >= 90 && <span className="badge hot">🔥 Consistent</span>}
              {completedTasks >= 20 && <span className="badge rock">🚀 Productive</span>}
              {totalBoards >= 3 && <span className="badge organizer">⭐ Organizer</span>}
              {totalTasks >= 10 && <span className="badge planner">📅 Planner</span>}
              {productivity === 0 && completedTasks === 0 && <p className="no-badges">Complete tasks to unlock milestones!</p>}
            </div>
          </div>

          {/* Secure Credential Updates Container */}
          <div className="password-card">
            <h2><FaKey className="module-icon" /> Change Password</h2>
            <form onSubmit={changePassword} className="password-form">
              <input
                type="password"
                placeholder="Current Password"
                value={passwordData.oldPassword}
                onChange={(e) => setPasswordData({ ...passwordData, oldPassword: e.target.value })}
                required
              />
              <input
                type="password"
                placeholder="New Secure Password"
                value={passwordData.newPassword}
                onChange={(e) => setPasswordData({ ...passwordData, newPassword: e.target.value })}
                required
              />
              <button type="submit" className="pwd-submit-btn">Update Password</button>
            </form>
          </div>

        </div>
      </div>
    </>
  );
}

export default Profile;