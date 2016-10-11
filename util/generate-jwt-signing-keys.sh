#!/bin/bash
set -e

function print_usage {
    cat << EOF
Usage: $0 keyname destination_directory

Generate a private and public key pair for signing JSON Web Tokens using the RS256 algorithm

The following files will be created:

   * keyname-private.pem
   * keyname-public.pem

Requires OpenSSL
EOF
}

function create_jwt_signing_key_pair {
    local keyname=$1
    local destination_directory=$2
    local working_directory="/tmp/${keyname}-workdir"

    mkdir -p $working_directory

    openssl genrsa \
        -out ${working_directory}/${keyname}-private.rsa \
        2048

    openssl pkcs8 \
        -topk8 \
        -inform pem \
        -in ${working_directory}/${keyname}-private.rsa \
        -outform pem \
        -nocrypt \
        -out ${working_directory}/${keyname}-private.pem

    openssl rsa \
        -in ${working_directory}/${keyname}-private.rsa \
        -pubout \
        -outform pem \
        -out ${working_directory}/${keyname}-public.pem

    cp ${working_directory}/${keyname}-private.pem $destination_directory
    cp ${working_directory}/${keyname}-public.pem $destination_directory

    rm -rf $working_directory
}

if [ "$#" -ne 2 ]; then                                                         
    print_usage                                                                 
    exit 1
fi

create_jwt_signing_key_pair $1 $2

