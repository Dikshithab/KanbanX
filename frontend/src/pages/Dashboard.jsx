import { useEffect, useState } from "react";
import api from "../api/axios";

import Sidebar from "../components/Sidebar";
import Navbar from "../components/Navbar";
import AddBoard from "../components/AddBoard";
import BoardCard from "../components/BoardCard";
import StatsCard from "../components/StatsCard";
import DashboardCharts from "../components/DashboardCharts";
import ConfirmDialog from "../components/ConfirmDialog";

import { toast } from "react-toastify";
import {
  FaClipboardList,
  FaTasks,
  FaCheckCircle,
  FaClock,
} from "react-icons/fa";

import { CircularProgressbar } from "react-circular-progressbar";
import "react-circular-progressbar/dist/styles.css";

import Skeleton from "react-loading-skeleton";
import "react-loading-skeleton/dist/skeleton.css";

import Confetti from "react-confetti";
import "../styles/dashboard.css";

function Dashboard() {
  const [boards, setBoards] = useState([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState("");
  const [deleteBoardId, setDeleteBoardId] = useState(null);
  const [showConfetti, setShowConfetti] = useState(false);

  useEffect(() => {
    loadBoards();
  }, []);

  const loadBoards = async () => {
    setLoading(true);
    try {
      const response = await api.get("/boards");
      setBoards(response.data);
    } catch (error) {
      console.error(error);
      toast.error("Failed to load boards");
    } finally {
      setLoading(false);
    }
  };

  const createBoard = async (name) => {
    try {
      await api.post("/boards", { name });
      toast.success("Board Created");
      loadBoards();
    } catch (error) {
      console.error(error);
      toast.error("Failed to create board");
    }
  };

  const deleteBoard = async () => {
    try {
      await api.delete(`/boards/${deleteBoardId}`);
      toast.success("Board Deleted");
      setDeleteBoardId(null);
      loadBoards();
    } catch (error) {
      console.error(error);
      toast.error("Delete Failed");
    }
  };

  const filteredBoards = boards.filter((board) =>
    board.name.toLowerCase().includes(search.toLowerCase())
  );

  const totalTasks = boards.reduce(
    (count, board) => count + (board.tasks?.length || 0),
    0
  );

  const completedTasks = boards.reduce(
    (count, board) =>
      count +
      (board.tasks
        ? board.tasks.filter((task) => task.status === "DONE").length
        : 0),
    0
  );

  const pendingTasks = totalTasks - completedTasks;

  const productivity =
    totalTasks === 0 ? 0 : Math.round((completedTasks / totalTasks) * 100);

  const today = new Date().toISOString().split("T")[0];

  const dueToday = boards
    .flatMap((board) => board.tasks || [])
    .filter((task) => task.dueDate === today);

  const overdue = boards
    .flatMap((board) => board.tasks || [])
    .filter(
      (task) =>
        task.status !== "DONE" &&
        task.dueDate &&
        new Date(task.dueDate) < new Date()
    );

  useEffect(() => {
    if (productivity === 100 && totalTasks > 0) {
      setShowConfetti(true);
      const timer = setTimeout(() => {
        setShowConfetti(false);
      }, 5000);
      return () => clearTimeout(timer);
    }
  }, [productivity, totalTasks]);

  const hour = new Date().getHours();
  const greeting =
    hour < 12
      ? "Good Morning ☀️"
      : hour < 18
      ? "Good Afternoon 🌤️"
      : "Good Evening 🌙";

  return (
    <>
      {showConfetti && <Confetti />}

      <Sidebar />
      <Navbar />

      <div className="dashboard">
        {/* Hero Section */}
        <div className="hero-section">
          <div className="dashboard-hero">
            <div>
              <h1>{greeting}</h1>
              <p>
                Manage your projects, organize tasks, and boost your productivity.
              </p>
            </div>
          </div>
        </div>

        {/* Statistics Section */}
        <div className="stats-section">
          <div className="stats-grid">
            <StatsCard className="stat-card" icon={<FaClipboardList />} title="Boards" value={boards.length} />
            <StatsCard className="stat-card" icon={<FaTasks />} title="Tasks" value={totalTasks} />
            <StatsCard className="stat-card" icon={<FaClock />} title="Pending" value={pendingTasks} />
            <StatsCard className="stat-card" icon={<FaCheckCircle />} title="Completed" value={completedTasks} />
            <StatsCard className="stat-card" icon="⚠️" title="Overdue" value={overdue.length} />
          </div>
        </div>

        {/* Analytics Section */}
        <div className="analytics-section">
          {/* Left Side: Charts */}
          <div className="analytics-left">
            <div className="chart-card">
              <DashboardCharts boards={boards} />
            </div>
          </div>

          {/* Right Side: Progress & Tasks */}
          <div className="analytics-right">
            <div className="progress-card">
              <div className="progress-wrapper">
                <CircularProgressbar value={productivity} text={`${productivity}%`} />
              </div>
              <h3>Overall Productivity</h3>
            </div>

            <div className="due-card">
              <h2>📅 Due Today</h2>
              {dueToday.length === 0 ? (
                <p>No tasks due today 🎉</p>
              ) : (
                dueToday.map((task) => (
                  <div key={task.id} className="due-task">
                    <strong>{task.title}</strong>
                    <span>{task.priority}</span>
                  </div>
                ))
              )}
            </div>

            <div className="due-card">
              <h2>Recent Boards</h2>
              {boards.length === 0 ? (
                <p>No Boards Available</p>
              ) : (
                boards.slice(0, 5).map((board) => (
                  <div key={board.id} className="due-task">
                    <span>📁 {board.name}</span>
                    <strong>{board.tasks?.length || 0} Tasks</strong>
                  </div>
                ))
              )}
            </div>
          </div>
        </div>

        {/* Toolbar: Search & Actions */}
        <div className="boards-toolbar">
          <input
            type="text"
            className="search-box"
            placeholder="🔍 Search Boards..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
          <AddBoard onCreate={createBoard} />
        </div>

        {/* Board Display List */}
        <div className="boards-list">
          {loading ? (
            Array.from({ length: 4 }).map((_, index) => (
              <Skeleton key={index} height={140} borderRadius={15} />
            ))
          ) : filteredBoards.length === 0 ? (
            <div className="empty-state">
              <h1>📁</h1>
              <h2>No Boards Yet</h2>
              <p>Create your first board.</p>
            </div>
          ) : (
            filteredBoards.map((board) => (
              <BoardCard key={board.id} board={board} onDelete={setDeleteBoardId} />
            ))
          )}
        </div>

        {/* Delete Dialog Confirmation */}
        {deleteBoardId && (
          <ConfirmDialog
            title="Delete Board"
            message="This action cannot be undone."
            onConfirm={deleteBoard}
            onCancel={() => setDeleteBoardId(null)}
          />
        )}
      </div>
    </>
  );
}

export default Dashboard;