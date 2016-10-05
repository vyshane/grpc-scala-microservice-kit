.PHONY: app
app:
	cd app; sbt docker:publishLocal

pushapp:
	cd app; sbt docker:publish

testapp:
	cd app; sbt clean coverage test

.PHONY: gateway
gateway:
	cd gateway; echo "TODO"

pushgateway:
	cd gateway; echo "TODO"

