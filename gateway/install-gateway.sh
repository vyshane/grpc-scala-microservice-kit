#!/bin/bash
APP=echod
GOPATH=$(pwd):$GOPATH

set -e

go get -u github.com/grpc-ecosystem/grpc-gateway/protoc-gen-grpc-gateway
go get -u github.com/grpc-ecosystem/grpc-gateway/protoc-gen-swagger
go get -u github.com/golang/protobuf/protoc-gen-go

function generate_stubs {
    local proto_file=$1
    # gRPC stub
    protoc -I. \
        -I$GOPATH/src \
        -I$GOPATH/src/github.com/grpc-ecosystem/grpc-gateway/third_party/googleapis \
        --go_out=Mgoogle/api/annotations.proto=github.com/grpc-ecosystem/grpc-gateway/third_party/googleapis/google/api,plugins=grpc:. \
        $proto_file
    # Reverse proxy
    protoc -I. \
        -I$GOPATH/src \
        -I$GOPATH/src/github.com/grpc-ecosystem/grpc-gateway/third_party/googleapis \
        --grpc-gateway_out=logtostderr=true:. \
        $proto_file
    # Swagger definitions
    protoc -I. \
        -I$GOPATH/src \
        -I$GOPATH/src/github.com/grpc-ecosystem/grpc-gateway/third_party/googleapis \
        --swagger_out=logtostderr=true:. \
        $proto_file
}

pushd src/gateway/generated/$APP/
generate_stubs "*.proto"
popd

pushd src/gateway/
go get -d -v
go install -v
