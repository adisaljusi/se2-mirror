FROM eclipse-temurin:21-jdk

RUN apt-get update -o Acquire::Check-Valid-Until=false -o Acquire::AllowInsecureRepositories=true -o Acquire::AllowDowngradeToInsecureRepositories=true && \
    apt-get install -y --allow-unauthenticated supervisor curl && \
    curl -sL https://deb.nodesource.com/setup_22.x | bash - && \
    apt-get install -y nodejs && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /usr/src/app

COPY freelance4u/mvnw freelance4u/mvnw
COPY freelance4u/.mvn freelance4u/.mvn
COPY freelance4u/pom.xml freelance4u/pom.xml
COPY freelance4u/src freelance4u/src
COPY frontend frontend

RUN cd frontend && rm -rf node_modules package-lock.json && npm install && npm run build

RUN cd freelance4u && sed -i 's/\r$//' mvnw && chmod +x mvnw

RUN cd freelance4u && ./mvnw package -DskipTests

# Use supervisor to start frontend and backend
RUN cat > /etc/supervisor/conf.d/supervisord.conf <<'EOF'
[supervisord]
nodaemon=true
user=root

[program:backend]
command=java -jar /usr/src/app/freelance4u/target/freelance4u-0.0.1-SNAPSHOT.jar
directory=/usr/src/app/freelance4u
autostart=true
autorestart=true
stdout_logfile=/dev/stdout
stdout_logfile_maxbytes=0
stderr_logfile=/dev/stderr
stderr_logfile_maxbytes=0

[program:frontend]
directory=/usr/src/app/frontend
command=sh -c "node build"
autostart=true
autorestart=true
stdout_logfile=/dev/stdout
stdout_logfile_maxbytes=0
stderr_logfile=/dev/stderr
stderr_logfile_maxbytes=0
EOF

EXPOSE 3000

ENV NODE_ENV=production

CMD ["/usr/bin/supervisord", "-c", "/etc/supervisor/conf.d/supervisord.conf"]
