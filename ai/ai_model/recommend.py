import numpy as np
from typing import List
from sklearn.metrics.pairwise import cosine_similarity
from sentence_transformers import SentenceTransformer

# -------- SBERT 모델 --------
model = SentenceTransformer(
    "sentence-transformers/paraphrase-multilingual-MiniLM-L12-v2",
     cache_folder="./ai_model/sbert-model")

ACTION_WEIGHT = {
    "VIEW": 1.0,
    "LIKE": 3.0,
    "COMMENT": 4.0
}


def recommend_posts(
    actions: List[dict],
    candidate_posts: List[dict],
    top_k: int
):
    """
    actions: 유저가 행동한 게시글 (취향 생성용)
    candidate_posts: 추천 후보 게시글
    """

    # 1️⃣ 콜드스타트 방어
    if not actions or not candidate_posts:
        return []

    # -------------------------------
    # 2️⃣ 유저 취향 벡터 생성
    # -------------------------------
    action_texts = [a["content"] for a in actions]
    action_vectors = model.encode(
        action_texts,
        convert_to_numpy=True,
        normalize_embeddings=True
    )

    weights = [
        ACTION_WEIGHT.get(a["action"], 1.0)
        for a in actions
    ]

    user_profile = np.average(
        action_vectors,
        axis=0,
        weights=weights
    )

    # -------------------------------
    # 3️⃣ 후보 게시글 임베딩
    # -------------------------------
    candidate_ids = [p["postId"] for p in candidate_posts]
    candidate_texts = [p["content"] for p in candidate_posts]

    candidate_vectors = model.encode(
        candidate_texts,
        convert_to_numpy=True,
        normalize_embeddings=True
    )

    # -------------------------------
    # 4️⃣ 유사도 계산
    # -------------------------------
    scores = cosine_similarity(
        user_profile.reshape(1, -1),
        candidate_vectors
    )[0]

    # -------------------------------
    # 5️⃣ 정렬 + TopK
    # -------------------------------
    ranked = sorted(
        zip(candidate_ids, scores),
        key=lambda x: x[1],
        reverse=True
    )

    return [
        {
            "postId": post_id,
            "score": float(score),
            "rank": idx + 1
        }
        for idx, (post_id, score) in enumerate(ranked[:top_k])
    ]
