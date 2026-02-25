import { useEffect, useState } from "react";
import axios from "axios";

export default function PostDetail({ postId, userId, onBack }) {
    const [post, setPost] = useState(null);
    const [likeCount, setLikeCount] = useState(0);
    const [liked, setLiked] = useState(false);

    // 💬 댓글 관련 상태
    const [comments, setComments] = useState([]);
    const [commentCount, setCommentCount] = useState(0); // 댓글 수 상태 추가
    const [commentContent, setCommentContent] = useState("");

    // 상세 데이터를 가져오는 로직을 함수로 분리 (재사용을 위해)
    const fetchDetail = () => {
        axios
            .get(`http://localhost:8080/posts/${postId}`, { params: { userId } })
            .then(res => {
                console.log("서버에서 온 데이터:", res.data); // 👈 여기서 정확한 필드명을 확인하세요!
                setPost(res.data);
                setLikeCount(res.data.likeCount);
                setLiked(res.data.likedByCurrentUser);
                setComments(res.data.comments || []);
                setCommentCount(res.data.commentCount); // 서버에서 준 초기 댓글 수 설정
            });
    };

    useEffect(() => {
        fetchDetail();
    }, [postId, userId]);

    const handleLike = async () => { /* 기존 좋아요 로직 동일 */ };

    // 💬 댓글 작성 및 UI 업데이트
    const handleCommentSubmit = async () => {
        if (!commentContent.trim()) return;

        try {
            // ✅ 1. 숫자를 먼저 올림 (낙관적 업데이트) - 사용자에게 바로 반응 보여주기
            setCommentCount(prev => prev + 1);

            // 1. 서버에 댓글 저장 요청
            const res = await axios.post(`http://localhost:8080/comments`, {
                userId: userId,
                postId: postId,
                content: commentContent
            });

            // 2. 입력창 초기화
            setCommentContent("");

            // ✅ 핵심: 리스트를 아예 새로 고침 (서버의 최신 ID가 포함된 리스트)
            fetchDetail();

        } catch (err) {
            console.error("댓글 작성 실패", err);
            setCommentCount(prev => prev - 1);
            alert("댓글 작성에 실패했습니다.");    
        }
    };

    if (!post) return <p>로딩중...</p>;

    return (
        <div style={{ padding: "10px" }}>
            <button onClick={onBack}>← 뒤로</button>

            <p style={{ fontSize: "18px", fontWeight: "bold", marginTop: "15px" }}>{post.content}</p>

            <div style={{ fontSize: "14px", color: "#555", marginBottom: "20px" }}>
                <span onClick={handleLike} style={{ cursor: "pointer", fontSize: "20px" }}>
                    {liked ? "❤️" : "🤍"}
                </span>
                {likeCount} · 👁 {post.viewCount} · 💬 {commentCount} {/* ✅ 여기를 상태값으로 변경 */}
            </div>

            <hr />

            {/* 댓글 리스트... (기존과 동일) */}
            <div style={{ marginTop: "20px" }}>
                <h4>댓글 ({commentCount})</h4> {/* ✅ 제목 옆 숫자도 동기화 */}
                {comments.map((c) => (
                    <div key={c.commentId} style={{ marginBottom: "10px", borderBottom: "1px solid #f0f0f0" }}>
                        <span style={{ fontWeight: "bold", marginRight: "10px" }}>{c.username}</span>
                        <span>{c.content}</span>
                    </div>
                ))}
            </div>

            {/* 댓글 입력창... (기존과 동일) */}
            <div style={{ marginTop: "20px", display: "flex", gap: "10px" }}>
                <input
                    value={commentContent}
                    onChange={(e) => setCommentContent(e.target.value)}
                    placeholder="댓글을 입력하세요..."
                    style={{ flex: 1, padding: "8px" }}
                />
                <button onClick={handleCommentSubmit} style={{ padding: "8px 15px" }}>등록</button>
            </div>
        </div>
    );
}