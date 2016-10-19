all: app gateway

push: pushapp pushgateway

test: testapp testgateway

clean: cleanapp cleangateway

.PHONY: deploy
deploy:
	cd deploy; helm install echod

.PHONY: app
app:
	cd app; sbt docker:publishLocal

pushapp:
	cd app; sbt docker:publish

testapp:
	cd app; sbt clean coverage test

cleanapp:
	cd app; sbt clean

.PHONY: gateway
gateway:
	cd gateway; make docker

pushgateway:
	cd gateway; make push

testgateway:
	cd gateway;  # TODO: Run tests for gateway

cleangateway:
	cd gateway;  make clean

