# gRPC Scala Microservice Kit

A starter kit for building [microservices](https://en.wikipedia.org/wiki/Microservices) using [gRPC](http://www.grpc.io) and [Scala](http://www.scala-lang.org).

The gRPC server is [set up to use TLS](https://github.com/grpc/grpc-java/blob/master/SECURITY.md#transport-security-tls) out of the box. [Mutual authentication](https://en.wikipedia.org/wiki/Transport_Layer_Security#Client-authenticated_TLS_handshake) is also implemented.

User sessions are propagated as [JSON Web Tokens](https://jwt.io) through the `Authorization` HTTP header using the `Bearer` schema. JWTs are signed and verified using RS256.

# Configuration

The application can be [configured](app/src/main/resources/application.conf) through environment variables.

[Utility scripts](util/) are provided to generate keys and SSL assets.

# Building

To build [Docker](https://www.docker.com/what-docker) images for the microservice:

```text
make
```

To build the Docker images and push them to the registry:

```text
make push
```

# Running Tests

```text
make test
```

# Deployment

A [Helm chart](deploy/echod/) is provided for deployment to a [Kubernetes](http://kubernetes.io) cluster. To run the deployment against your current Kubernetes context:

```text
make deploy
```

