#!/bin/bash

echo "1. Get Project admin access token."
PROJECT_ADMIN_ACCESS_TOKEN=$(curl -s --request POST 'http://localhost:8080/auth/services/oauth2/backend-services/microprojects/token?grant_type=password&username=mp-admin&password=mp-secret&scope=&client_id=mp-client&client_secret=mp-client-secret' \
--header 'Content-Type: application/x-www-form-urlencoded')
echo "  $?"
echo "${PROJECT_ADMIN_ACCESS_TOKEN}"

curl --location --request GET 'http://localhost:8080/auth/services/discovery/backend-services/microprojects' \
--header 'Authorization: Bearer '"$PROJECT_ADMIN_ACCESS_TOKEN"''
echo "  $?"

curl --location --request GET 'http://localhost:8080/auth/services/discovery/backend-services/microprojects/users/mp-admin' \
--header 'Authorization: Bearer '"$PROJECT_ADMIN_ACCESS_TOKEN"''
echo "  $?"

curl --location --request GET 'http://localhost:8080/auth/services/discovery/backend-services/microprojects/users/juraj' \
--header 'Authorization: Bearer '"$PROJECT_ADMIN_ACCESS_TOKEN"''
echo "  $?"

