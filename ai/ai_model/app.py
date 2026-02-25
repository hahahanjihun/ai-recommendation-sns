from fastapi import FastAPI
from pydantic import BaseModel
from typing import List
from recommend import recommend_posts

app = FastAPI()


# -------- DTO --------
class Action(BaseModel):
    postId: int
    action: str
    content: str


class CandidatePost(BaseModel):
    postId: int
    content: str


class RecommendRequest(BaseModel):
    userId: int
    actions: List[Action]
    candidates: List[CandidatePost]
    topK: int = 5


class RecommendItem(BaseModel):
    postId: int
    score: float


class RecommendResponse(BaseModel):
    userId: int
    recommendations: List[RecommendItem]


@app.post("/recommend", response_model=RecommendResponse)
def recommend(req: RecommendRequest):

    items = recommend_posts(
        actions=[a.model_dump() for a in req.actions],
        candidate_posts=[c.model_dump() for c in req.candidates],
        top_k=req.topK
    )

    return RecommendResponse(
        userId=req.userId,
        recommendations=items
    )
