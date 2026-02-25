# Node.js 20
FROM node:20-alpine

# 작업 디렉토리
WORKDIR /app

# 의존성 설치
COPY package*.json ./       
RUN npm install

# 코드 전체 복사
COPY . .                  

# 포트
EXPOSE 5173

# 환경변수 빌드 시 주입
ARG VITE_API_URL
ENV VITE_API_URL=$VITE_API_URL

# 실행
CMD ["npm", "run", "dev", "--", "--host"]