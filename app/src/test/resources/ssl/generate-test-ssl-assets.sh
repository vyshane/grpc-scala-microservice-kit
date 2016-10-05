#!/bin/bash
set -e

../../../../../util/generate-self-signed-ssl-assets.sh localhost .

mv localhost-key.pem localhost-server-key.pem
mv localhost-cert.pem localhost-server-cert.pem
mv localhost-ca-cert.pem localhost-server-ca-cert.pem

../../../../../util/generate-self-signed-ssl-assets.sh localhost .

mv localhost-key.pem localhost-client-key.pem
mv localhost-cert.pem localhost-client-cert.pem
mv localhost-ca-cert.pem localhost-client-ca-cert.pem

