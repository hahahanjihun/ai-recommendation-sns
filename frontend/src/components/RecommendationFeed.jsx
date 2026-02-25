import { useEffect, useState } from "react";
import axios from "axios";

export default function RecommendationFeed({ userId, onSelectPost }) {
  const [posts, setPosts] = useState([]);

  useEffect(() => {
    if (!userId) return;

    axios
      .get(`http://localhost:8080/recommendations?userId=${userId}`)
      .then(res => setPosts(res.data))
      .catch(err => console.error(err));
  }, [userId]);

  const toggleLike = async (postId) => {
    // ✅ 즉시 UI 반영
    setPosts(prev =>
      prev.map(post =>
        post.postId === postId
          ? {
            ...post,
            likedByCurrentUser: !post.likedByCurrentUser,
            likeCount: post.likedByCurrentUser
              ? post.likeCount - 1
              : post.likeCount + 1
          }
          : post
      )
    );

    try {
      await axios.post(
        `http://localhost:8080/posts/${postId}/like?userId=${userId}`
      );
    } catch (e) {
      console.error("좋아요 실패", e);
    }
  };

  return (
    <div>
      <h3>추천 피드</h3>

      {posts.map(post => (
        <div
          key={post.postId}
          style={{
            cursor: "pointer",
            border: "1px solid #ddd",
            padding: "10px",
            marginBottom: "8px"
          }}
          onClick={() => onSelectPost(post.postId)}

        >

          {/* 작성자 영역 추가 */}
          <div style={{
            fontWeight: "bold",
            marginBottom: "5px",
            fontSize: "14px"
          }}>
            👤 {post.authorName}
          </div>
          <p
            style={{ cursor: "pointer" }}
            onClick={() => onSelectPost(post.postId)}
          >
            {post.content}
          </p>

          <div style={{ fontSize: "14px" }}>
            <span
              onClick={(e) => {
                e.stopPropagation();
                toggleLike(post.postId);
              }}
              style={{
                cursor: "pointer",
                fontSize: "16px"
              }}
            >
              {post.likedByCurrentUser ? "❤️" : "🤍"}
            </span>

            <span style={{ marginLeft: "5px", color: "black" }}>
              {post.likeCount}
            </span>

            {" · "}
            👁 {post.viewCount}
            {" · "}
            💬 {post.commentCount}
          </div>
        </div>
      ))}
    </div>
  );
}
