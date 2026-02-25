# AI Recommendation SNS

Spring Boot + React + FastAPI 기반  
사용자 행동 데이터를 활용한 추천 시스템 SNS 프로젝트

---

## 📌 프로젝트 소개

사용자의 좋아요, 댓글 등의 행동 데이터를 수집하여  
AI 모델을 통해 게시글을 추천해주는 SNS 플랫폼입니다.

단순 CRUD 구현을 넘어  
추천 시스템과 Docker 기반 통합 환경 구성까지 구현했습니다.

---

## 🔥 주요 기능

- 회원가입 / 로그인 (JWT 인증)
- 게시글 CRUD
- 댓글 기능
- 사용자 행동 데이터 저장
- AI 추천 피드
- Docker 기반 전체 서비스 실행

---

## 🛠 Tech Stack

### Backend
- Spring Boot
- JPA
- MySQL
- Spring Security

### Frontend
- React
- Vite
- Axios

### AI Server
- FastAPI
- Python
- SBERT-MODEL

### Infra
- Docker
- Docker Compose

---

## 🏗 프로젝트 구조

```
sns-docker/
├── backend/ # Spring Boot
├── frontend/ # React (Vite)
├── ai/ # FastAPI recommendation server
└── docker-compose.yml
```


