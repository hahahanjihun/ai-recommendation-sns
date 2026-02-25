# Python 3.11 slim 이미지
FROM python:3.11-slim

# 작업 디렉토리 생성
WORKDIR /app

# 의존성 복사 및 설치
COPY ai_model/requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# AI 모델과 코드 복사
COPY ai_model/ .

# 컨테이너 외부 포트
EXPOSE 8000

# 실행 명령
CMD ["uvicorn", "app:app", "--host", "0.0.0.0", "--port", "8000"]