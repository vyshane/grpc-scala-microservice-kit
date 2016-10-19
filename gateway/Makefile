SERVICE_NAME=echo
DOCKER_REPOSITORY=vyshane

docker: clean
	mkdir -p src/gateway/generated/$(SERVICE_NAME)/
	cp ../protobuf/src/$(SERVICE_NAME).proto src/gateway/generated/$(SERVICE_NAME)/
	docker build -t $(DOCKER_REPOSITORY)/$(SERVICE_NAME)-gateway:1.0.0-SNAPSHOT .

push:
	docker push $(DOCKER_REPOSITORY)/$(SERVICE_NAME)-gateway:1.0.0-SNAPSHOT

clean:
	rm -rf src/gateway/generated/