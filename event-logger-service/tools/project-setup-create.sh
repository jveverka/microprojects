#!/bin/bash

echo "1. Get GLOBAL_ADMIN_ACCESS_TOKEN"
GLOBAL_ADMIN_ACCESS_TOKEN=$(curl -s --request POST "http://localhost:8080/auth/services/oauth2/iam-admins/iam-admins/token?grant_type=password&username=admin&password=secret&scope=&client_id=admin-client&client_secret=secret" \
--header "Content-Type: application/x-www-form-urlencoded" | jq -r ".access_token")
echo "  $?"
echo "  ${GLOBAL_ADMIN_ACCESS_TOKEN}"

echo "2. Create project and organization"
curl --location --request POST 'http://localhost:8080/auth/services/admin/organization/setup' \
  --header 'Authorization: Bearer '"$GLOBAL_ADMIN_ACCESS_TOKEN"'' \
  --header 'Content-Type: application/json' \
  --data-raw '{
     "organizationId": "backend-services",
     "organizationName": "Backend Services",
     "projectId": "microprojects",
     "projectName": "Microprojects",
     "adminClientId": "mp-client",
     "adminClientSecret": "mp-client-secret",
     "adminUserId": "mp-admin",
     "adminUserPassword": "mp-secret",
     "adminUserEmail": "mp-admin@microproject.one",
     "projectAudience": [],
     "redirectURL": "http://localhost:80",
     "adminUserProperties": {
       "properties": {}
     }
  }'
echo "  $?"

echo "3. Get Project admin access token."
PROJECT_ADMIN_ACCESS_TOKEN=$(curl -s --request POST 'http://localhost:8080/auth/services/oauth2/backend-services/microprojects/token?grant_type=password&username=mp-admin&password=mp-secret&scope=&client_id=mp-client&client_secret=mp-client-secret' \
--header 'Content-Type: application/x-www-form-urlencoded' | jq -r ".access_token")
echo "  $?"

echo "4. Create project user"
curl --location --request POST "http://localhost:8080/auth/services/management/backend-services/microprojects/users" \
  --header 'Authorization: Bearer '"$PROJECT_ADMIN_ACCESS_TOKEN"'' \
  --header 'Content-Type: application/json' \
  --data-raw '{
      "id": "juraj",
      "name": "Juraj Veverka",
      "defaultAccessTokenDuration": 86400000,
      "defaultRefreshTokenDuration": 172800000,
      "email": "gergej123@gmail.com",
      "password": "secret",
      "userProperties": {
          "properties": {}
      }
  }'
echo "  $?"

echo "5. Create project role - full access"
curl --location --request POST 'http://localhost:8080/auth/services/management/backend-services/microprojects/roles' \
  --header 'Authorization: Bearer '"$PROJECT_ADMIN_ACCESS_TOKEN"'' \
  --header 'Content-Type: application/json' \
  --data-raw '{
    "id": "event-logger-full-role",
    "name": "Full Access Event-Logger Role",
    "permissions": [
        {
            "service": "event-logger",
            "resource": "DataRecord",
            "action": "all"
        },
        {
            "service": "event-logger",
            "resource": "DataSeries",
            "action": "all"
        }
    ]
  }'

echo "6. Assign project role to user"
curl --location --request PUT 'http://localhost:8080/auth/services/management/backend-services/microprojects/users/juraj/roles/event-logger-full-role' \
--header 'Authorization: Bearer '"$PROJECT_ADMIN_ACCESS_TOKEN"''
echo "  $?"

echo "7. Get user's access token"
USER_ACCESS_TOKEN=`curl -s --request POST 'http://localhost:8080/auth/services/oauth2/backend-services/microprojects/token?grant_type=password&username=juraj&password=secret&scope=&client_id=mp-client&client_secret=mp-client-secret' \
--header 'Content-Type: application/x-www-form-urlencoded' | jq -r ".access_token"`
echo "  $?"
echo "USER_ACCESS_TOKEN=${USER_ACCESS_TOKEN}"
