#!/bin/bash
set -e

function print_usage {
    cat << EOF
Usage: $0 hostname destination_directory

Generate self-signed SSL certificate assets for hostname, placing the files in the destination_directory.

The following files will be created:

   * hostname-key.pem (private key in PKCS #8 format)
   * hostname-cert.pem
   * hostname-ca-cert.pem

Requires OpenSSL.
EOF
}

function create_self_signed_certificate_assets {
    local hostname=$1
    local destination_directory=$2
    local working_directory="/tmp/${hostname}-ssl-workdir"
    export RANDFILE=${working_directory}/.rnd 

    mkdir -p $working_directory
    
    openssl genrsa \
        -out ${working_directory}/${hostname}-ca-key.rsa \
        2048
    
    openssl req \
        -x509 \
        -new \
        -nodes \
        -key ${working_directory}/${hostname}-ca-key.rsa \
        -days 10000 \
        -out ${working_directory}/${hostname}-ca-cert.pem \
        -subj "/CN=node-mu-ca"
    
    cat > $working_directory/openssl.cnf <<EOL
[req]
req_extensions = v3_req
distinguished_name = req_distinguished_name
[req_distinguished_name]
[v3_req]
basicConstraints = CA:FALSE
keyUsage = nonRepudiation, digitalSignature, keyEncipherment
subjectAltName = @alt_names
[alt_names]
DNS.1 = ${hostname}
DNS.2 = localhost
EOL
    
    openssl genrsa \
        -out ${working_directory}/${hostname}-key.rsa \
        2048
    
    openssl req \
        -new \
        -key ${working_directory}/${hostname}-key.rsa \
        -out ${working_directory}/${hostname}.csr \
        -subj "/CN=$(hostname)" \
        -config ${working_directory}/openssl.cnf
    
    openssl x509 \
        -req \
        -in ${working_directory}/${hostname}.csr \
        -CA ${working_directory}/${hostname}-ca-cert.pem \
        -CAkey ${working_directory}/${hostname}-ca-key.rsa \
        -CAcreateserial \
        -out ${working_directory}/${hostname}-cert.pem \
        -days 10000 \
        -extensions v3_req \
        -extfile ${working_directory}/openssl.cnf

    openssl pkcs8 \
        -topk8 \
        -inform pem \
        -in ${working_directory}/${hostname}-key.rsa \
        -outform pem \
        -nocrypt \
        -out ${working_directory}/${hostname}-key.pem
    
    chmod 600 ${working_directory}/${hostname}-ca-cert.pem
    chmod 600 ${working_directory}/${hostname}-key.pem
    chmod 600 ${working_directory}/${hostname}-cert.pem

    cp ${working_directory}/${hostname}-ca-cert.pem $destination_directory
    cp ${working_directory}/${hostname}-key.pem $destination_directory
    cp ${working_directory}/${hostname}-cert.pem $destination_directory

    rm -rf $working_directory
}

if [ "$#" -ne 2 ]; then                                                         
    print_usage                                                                 
    exit 1
fi

create_self_signed_certificate_assets $1 $2
