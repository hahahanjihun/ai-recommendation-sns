import { useState } from "react";
import "./WritePost.css";
import { createPost } from "../api/PostApi";

export default function WritePost({ userId, onSuccess, onCancel }) {
  const [content, setContent] = useState("");

  const submit = async () => {
    if (!content.trim()) return;
    await createPost(userId, content);
    setContent("");
    onSuccess();
  };

  return (
    <div className="write-box">
      <textarea
        rows={4}
        placeholder="무슨 생각을 하고 있나요?"
        value={content}
        onChange={(e) => setContent(e.target.value)}
      />
      <div className="write-actions">
        <button className="cancel" onClick={onCancel}>취소</button>
        <button className="submit" onClick={submit}>등록</button>
      </div>
    </div>
  );
}
