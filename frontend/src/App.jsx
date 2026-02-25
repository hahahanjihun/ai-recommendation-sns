import { useState, useEffect } from "react";
import LoginForm from "./components/LoginForm";
import RegisterForm from "./components/RegisterForm";
import MainPage from "./components/MainPage";
import axios from "axios";
import "./App.css";

export default function App() {
  const [userId, setUserId] = useState(null);

  const handleLogout = () => {
    setUserId(null);
  };

  if (!userId) {
    return (
      <div className="app-layout">
        <RegisterForm onRegister={setUserId} />
        <hr />
        <LoginForm onLogin={setUserId} />
      </div>
    );
  }

  // ✅ 바로 메인 진입
  return (
    <div className="app-layout">
      <MainPage userId={userId} onLogout={handleLogout} />
    </div>
  );
}
