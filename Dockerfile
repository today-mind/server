FROM eclipse-temurin:17-jre-jammy

# 컨테이너 내부에서 애플리케이션이 실행될 작업 디렉토리 설정
WORKDIR /app

# 보안 목적의 전용 유저 생성
RUN useradd -r -u 1001 -g root -d /app -s /sbin/nologin spring

# 로컬에서 빌드된 JAR 파일을 컨테이너로 복사
COPY --chown=spring:spring build/libs/today-mind.jar app.jar

# 이후 모든 명령은 spring 유저 권한으로 실행
USER spring

# 컨테이너 시작 시 실행될 명령
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=70.0", "-XX:+UseG1GC", "-Duser.timezone=Asia/Seoul", "-XX:+ExitOnOutOfMemoryError", "-jar", "app.jar"]