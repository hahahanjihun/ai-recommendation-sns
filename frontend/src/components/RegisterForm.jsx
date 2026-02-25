import { useState } from "react";
import axios from "axios";

export default function RegisterForm({ onRegister }) {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const res = await axios.post(`${import.meta.env.VITE_API_URL}/users`, {
        username,
        password,
      });
      alert("회원가입 완료! ID: " + res.data);
      onRegister(res.data);
      // 바로 로그인 상태로 전환
    } catch (err) {
      console.error(err);
      alert("회원가입 실패");
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <h3>회원가입</h3>
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
      <button type="submit">회원가입</button>
    </form>
  );
}
