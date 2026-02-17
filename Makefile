APP_BASE_URL ?= http://localhost:5173

.PHONY: start test build

start:
	docker compose up -d

app-logs:
	docker compose logs app

test:
	APP_BASE_URL=$(APP_BASE_URL) ./gradlew test

build:
	./gradlew build -x test
