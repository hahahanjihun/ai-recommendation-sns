import { useEffect, useState } from "react";
import axios from "axios";

export default function ProfilePage({ userId, onBack }) {
  const [posts, setPosts] = useState([]);

  useEffect(() => {
    axios
      .get(`${import.meta.env.VITE_API_URL}/posts/user/${userId}`)
      .then(res => {
        console.log("내 게시글:", res.data);
        setPosts(res.data);
      })
      .catch(err => {
        console.error("게시글 불러오기 실패", err);
      });
  }, [userId]);

  return (
    <div style={{ padding: "20px" }}>
      <button onClick={onBack}>← 뒤로</button>
      <h2>내가 쓴 글</h2>

      {posts.length === 0 ? (
        <p>작성한 게시글이 없습니다.</p>
      ) : (
        posts.map(post => (
          <div
            key={post.postId}
            style={{
              border: "1px solid #ddd",
              padding: "10px",
              marginBottom: "10px",
              borderRadius: "8px"
            }}
          >
            <p style={{ fontWeight: "bold" }}>{post.content}</p>
            <div style={{ fontSize: "14px", color: "#555" }}>
              ❤️ {post.likeCount} · 👁 {post.viewCount} · 💬 {post.commentCount}
            </div>
          </div>
        ))
      )}
    </div>
  );
}