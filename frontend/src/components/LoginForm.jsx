import { useState } from "react";
import axios from "axios";

export default function LoginForm({ onLogin }) {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const res = await axios.post(`${import.meta.env.VITE_API_URL}/users/login`, {
        username,
        password,
      });

      onLogin(res.data);
    } catch (err) {
      console.error(err.response);
      alert("로그인 실패");
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <h3>로그인</h3>
      <input
        type="text"
        placeholder="username"
        value={username}
        onChange={(e) => setUsername(e.target.value)}
        required
      />
      <input
        type="password"
        placeholder="password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        required
      />
      <button type="submit">로그인</button>
    </form>
  );
}
