docker:
	sbt docker:publishLocal

dockerpush:
	sbt docker:publish

test:
	sbt clean coverage test

