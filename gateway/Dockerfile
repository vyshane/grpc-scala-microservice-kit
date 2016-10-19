FROM golang:1.7
MAINTAINER shane@node.mu
ENV PROTOBUF_VERSION 3.0.0-beta-3

RUN set -x && \
    apt-get -qq update && \
    DEBIAN_FRONTEND=noninteractive apt-get -yq install unzip && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Install protoc
RUN wget -O /tmp/protobuf.zip \
        https://github.com/google/protobuf/releases/download/v${PROTOBUF_VERSION}/protoc-${PROTOBUF_VERSION}-linux-x86_64.zip && \
    unzip /tmp/protobuf.zip && \
    mv protoc /usr/bin/

COPY install-gateway.sh /go/install-gateway.sh
COPY src/gateway/ /go/src/gateway/

WORKDIR /go/
RUN ./install-gateway.sh
CMD ["gateway"]