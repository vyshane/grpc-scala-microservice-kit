APP=echod
DOCKER_REPOSITORY=vyshane
PROTO_FILE=echo.proto

docker: clean
	mkdir -p src/gateway/generated/$(APP)/
	cp ../protobuf/src/$(PROTO_FILE) src/gateway/generated/$(APP)/
	docker build -t $(DOCKER_REPOSITORY)/$(APP)-gateway:1.0.0-SNAPSHOT .

push:
	docker push $(DOCKER_REPOSITORY)/$(APP)-gateway:1.0.0-SNAPSHOT

clean:
	rm -rf src/gateway/generated/