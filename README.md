# MagCloud Backend
### 프로젝트 소개
이 프로젝트는 매지구름 프로젝트의 백엔드 서버입니다. SpringBoot 3.0과 MVC 패턴을 사용하여 개발되었습니다.

### 프로젝트 특징
- Dockerfile을 통해 Docker 이미지를 만들 수 있습니다.
- Github Actions를 통해 Amazon ECS에 배포할 수 있습니다.

### 프로젝트 기능
- 사용자(혹은 oAuth) 회원가입, 로그인(토큰 발급)
- 일기 등록, 수정, 조회
- 사용자 장치 등록 및 푸쉬 알림 발송
- AI 인퍼런스를 통한 자동 감정 분석

### 프로젝트 빌드 및 실행
1. 프로젝트를 클론합니다.
```shell
git clone https://github.com/magcloud/redbean-backend.git
```
2. 프로젝트를 빌드합니다.
```shell
cd redbean-backend && ./gradlew bootJar
```
3. Dockerfile을 빌드합니다 (직접 실행할 경우 이 단계부터 건너뛰어도 됩니다)
```shell
docker build -t <YOUR_TAG_NAME> .
```
4. 필요한 환경 변수를 설정한 후 Docker 컨테이너를 실행합니다.
```shell
docker run -p 8080:8080 -e <YOUR_ENV_VAR> <YOUR_TAG_NAME>
```

### 환경 변수
|이름| 설명                                           |
|---|----------------------------------------------|
|SERVER_PORT| 서버 포트입니다. 기본값은 8080입니다.|
|FLYWAY_URL| 데이터베이스 URL입니다.                               |
|MYSQL_USERNAME| 데이터베이스 사용자 이름입니다.                            |
|MYSQL_PASSWORD| 데이터베이스 사용자 비밀번호입니다.                          |
|INFERENCE_URL| AI 인퍼런스 서버 URL입니다.                           |
|NAVER_CLIENT_ID| 네이버 oAuth 클라이언트 ID입니다.                       |
|NAVER_CLIENT_SECRET| 네이버 oAuth 클라이언트 비밀번호입니다.                     |
|KAKAO_CLIENT_ID| 카카오 oAuth 클라이언트 ID입니다.                       |
|KAKAO_REDIRECT_URL| 카카오 oAuth 리다이렉트 URL입니다.                      |
|APPLE_KEY_ID| 애플 oAuth 키 ID입니다.                            |
|APPLE_KEYFILE_value| 애플 oAuth 키 파일입니다. 내용 전체를 Base64 인코딩해서 제출합니다. |
|APPLE_TEAM_ID| 애플 oAuth 팀 ID입니다.                            |
|APPLE_CLIENT_ID| 애플 oAuth 클라이언트 ID입니다.                       |
|APPLE_NATIVE_CLIENT_ID| 애플 oAuth 네이티브 클라이언트 ID입니다.                  |
|GOOGLE_FIREBASE_SECRET_VALUE| Google Firebase 서비스 계정 키 파일입니다. 내용 전체를 Base64 인코딩해서 제출합니다. |
|AUTH_SECRET| JWT 토큰 암호화에 사용되는 시크릿 키입니다.                     |
