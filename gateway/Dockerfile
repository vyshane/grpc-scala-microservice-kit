FROM golang:1.7
MAINTAINER shane@node.mu
ENV PROTOBUF_VERSION 3.1.0

RUN set -x && \
    apt-get -qq update && \
    DEBIAN_FRONTEND=noninteractive apt-get -yq install unzip && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Install protoc
RUN mkdir -p /tmp/protobuf/ && \
    wget -O /tmp/protobuf.zip \
        https://github.com/google/protobuf/releases/download/v${PROTOBUF_VERSION}/protoc-${PROTOBUF_VERSION}-linux-x86_64.zip && \
    unzip /tmp/protobuf.zip -d /tmp/protobuf/ && \
    mv /tmp/protobuf/bin/protoc /usr/bin/

# Install the proto files that shipped with protoc
RUN mkdir -p /go/src/gateway/generated/ && \
    cp -r /tmp/protobuf/include/. /go/src/gateway/generated/echod/

COPY install-gateway.sh /go/install-gateway.sh
COPY src/gateway/ /go/src/gateway/

WORKDIR /go/
RUN ./install-gateway.sh
CMD ["gateway"]