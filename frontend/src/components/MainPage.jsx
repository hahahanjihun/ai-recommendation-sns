import { useState } from "react";
import Header from "./Header";
import RecommendationFeed from "./RecommendationFeed";
import WritePost from "./WritePost";
import PostDetail from "./PostDetail";
import ProfilePage from "./ProfilePage";


export default function MainPage({ userId, onLogout }) {
  const [page, setPage] = useState("feed");
  const [selectedPostId, setSelectedPostId] = useState(null);

  const goToDetail = (postId) => {
    setSelectedPostId(postId);
    setPage("detail");
  };

  const goBack = () => {
    setSelectedPostId(null);
    setPage("feed"); // 또는 이전 페이지 기억해도 됨
  };

  return (
    <div>
      <Header
        onWrite={() => setPage("write")}
        onProfile={() => setPage("profile")}
        onLogout={onLogout}
      />

      {page === "write" && (
        <WritePost
          userId={userId}
          onSuccess={() => setPage("feed")}
          onCancel={() => setPage("feed")}
        />
      )}

      {page === "feed" && (
        <RecommendationFeed
          userId={userId}
          onSelectPost={goToDetail}
        />
      )}

      {page === "profile" && (
        <ProfilePage
          userId={userId}
          onBack={goBack}
          onSelectPost={goToDetail}
        />
      )}

      {page === "detail" && selectedPostId && (
        <PostDetail
          postId={selectedPostId}
          userId={userId}
          onBack={goBack}
        />
      )}
    </div>
  );
}