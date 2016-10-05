all: app gateway

push: pushapp pushgateway

test: testapp testgateway

clean: cleanapp cleangateway

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
	cd gateway;  # TODO: Build Docker image for gateway

pushgateway:
	cd gateway;  # TODO: Build and push Docker image for gateway

testgateway:
	cd gateway;  # TODO: Run tests for gateway

cleangateway:
	cd gateway;  # TODO: Clean gateway

