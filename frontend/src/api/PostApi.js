import axios from "axios";

// 환경변수 기반으로 기본 URL 설정
const BASE_URL = `${import.meta.env.VITE_API_URL}/posts`;

export async function getPost(postId, userId) {
  // 조회 로그 남기고 싶으면 userId 같이 보냄
  await fetch(`${BASE_URL}/${postId}?userId=${userId}`);
}

export const createPost = (userId, content) => {
  return axios.post(BASE_URL, {
    userId,
    content,
  });
};

export async function exportPosts() {
  const res = await fetch(`${BASE_URL}/export`);
  if (!res.ok) throw new Error("posts export 실패");
  return await res.json();
}