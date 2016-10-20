package main

import (
	"crypto/tls"
	"crypto/x509"
	"io/ioutil"
	"net/http"
	"os"
	"strings"

	"github.com/golang/glog"
	"github.com/gorilla/handlers"
	"github.com/grpc-ecosystem/grpc-gateway/runtime"
	"golang.org/x/net/context"
	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials"

	echodGrpc "gateway/generated/echod"
)

var (
	clientCertPath       = os.Getenv("SSL_CLIENT_CERTIFICATE_PATH")
	clientPrivateKeyPath = os.Getenv("SSL_CLIENT_PRIVATE_KEY_PATH")
	serverCaCertPath     = os.Getenv("SSL_CA_CERTIFICATE_PATH")
	backend              = os.Getenv("BACKEND_HOST") + ":" + os.Getenv("BACKEND_PORT")
	swaggerDir           = "generated/echod"
)

func run() error {
	ctx := context.Background()
	ctx, cancel := context.WithCancel(ctx)
	defer cancel()

	mux := runtime.NewServeMux()
	dialOptions, err := getDialOptions(serverCaCertPath, clientCertPath, clientPrivateKeyPath)
	if err != nil {
		return err
	}
	err = echodGrpc.RegisterEchoServiceHandlerFromEndpoint(ctx, mux, backend, dialOptions)
	if err != nil {
		return err
	}

	allowedMethods := handlers.AllowedMethods([]string{"OPTIONS", "DELETE", "GET", "HEAD", "POST", "PUT"})
	allowedOrigins := getAllowedOriginsFromConfig("CORS_ALLOWED_ORIGINS")
	allowedHeaders := handlers.AllowedHeaders([]string{"Authorization", "Origin", "Content-Type"})

	http.ListenAndServe(":"+os.Getenv("GATEWAY_PORT"), handlers.CORS(allowedMethods, allowedOrigins, allowedHeaders)(mux))
	return nil
}

func main() {
	defer glog.Flush()
	if err := run(); err != nil {
		glog.Error(err)
	}
}

func getDialOptions(serverCaCertPath string, clientCertPath string, clientPrivateKeyPath string) ([]grpc.DialOption, error) {
	clientCert, err := tls.LoadX509KeyPair(clientCertPath, clientPrivateKeyPath)
	if err != nil {
		return nil, err
	}
	serverCaCert, err := ioutil.ReadFile(serverCaCertPath)
	if err != nil {
		return nil, err
	}
	caCertPool := x509.NewCertPool()
	caCertPool.AppendCertsFromPEM(serverCaCert)
	transportCredentials := credentials.NewTLS(&tls.Config{
		Certificates: []tls.Certificate{clientCert},
		RootCAs:      caCertPool,
	})
	return []grpc.DialOption{grpc.WithTransportCredentials(transportCredentials)}, err
}

func getAllowedOriginsFromConfig(env string) handlers.CORSOption {
	configuredOrigins := strings.Split(os.Getenv(env), ",")
	for key, url := range configuredOrigins {
		configuredOrigins[key] = strings.TrimSpace(url)
	}
	return handlers.AllowedOrigins(configuredOrigins)
}
