#!/bin/bash

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

BASE_URL="http://localhost:8080"

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}Bank API - Testing Script${NC}"
echo -e "${BLUE}========================================${NC}\n"

# Check if application is running
echo -e "${YELLOW}Checking if application is running...${NC}"
if ! curl -s "$BASE_URL/v3/api-docs" > /dev/null; then
    echo -e "${RED}❌ Application is not running on $BASE_URL${NC}"
    echo "Please start the application with: ./mvnw spring-boot:run"
    exit 1
fi
echo -e "${GREEN}✅ Application is running${NC}\n"

# Test 1: Register a new user
echo -e "${BLUE}Test 1: Registering a new user${NC}"
REGISTER_RESPONSE=$(curl -s -X POST "$BASE_URL/api/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "testuser@example.com",
    "password": "testpass123"
  }')

TOKEN=$(echo $REGISTER_RESPONSE | grep -o '"token":"[^"]*' | cut -d'"' -f4)

if [ -z "$TOKEN" ]; then
    echo -e "${RED}❌ Registration failed${NC}"
    echo "Response: $REGISTER_RESPONSE"
    exit 1
fi

echo -e "${GREEN}✅ User registered successfully${NC}"
echo -e "Token: ${YELLOW}${TOKEN:0:50}...${NC}\n"

# Test 2: List Swagger UI
echo -e "${BLUE}Test 2: Swagger UI Accessibility${NC}"
if curl -s "$BASE_URL/swagger-ui.html" | grep -q "swagger"; then
    echo -e "${GREEN}✅ Swagger UI is accessible at $BASE_URL/swagger-ui.html${NC}\n"
else
    echo -e "${YELLOW}⚠️  Swagger UI might not be fully loaded (check in browser)${NC}"
    echo "Try accessing: $BASE_URL/swagger-ui.html\n"
fi

# Test 3: Check OpenAPI docs
echo -e "${BLUE}Test 3: OpenAPI Documentation${NC}"
API_DOCS=$(curl -s "$BASE_URL/v3/api-docs" | grep -o '"title":"[^"]*' | cut -d'"' -f4)
if [ ! -z "$API_DOCS" ]; then
    echo -e "${GREEN}✅ OpenAPI docs available: $API_DOCS${NC}\n"
fi

# Test 4: Valid transfer request
echo -e "${BLUE}Test 4: Valid Transfer Request (should succeed)${NC}"
TRANSFER_RESPONSE=$(curl -s -X POST "$BASE_URL/api/transactions/transfer" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "sourceAccountNumber": "123456",
    "destinationAccountNumber": "654321",
    "amount": 100.50
  }')

if echo $TRANSFER_RESPONSE | grep -q "Transfer successful"; then
    echo -e "${GREEN}✅ Transfer endpoint working${NC}"
    echo "Response: $TRANSFER_RESPONSE\n"
else
    echo -e "${YELLOW}⚠️  Response: $TRANSFER_RESPONSE${NC}\n"
fi

# Test 5: Invalid amount (0)
echo -e "${BLUE}Test 5: Invalid Amount Test (amount = 0)${NC}"
INVALID_AMOUNT=$(curl -s -X POST "$BASE_URL/api/transactions/transfer" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "sourceAccountNumber": "123456",
    "destinationAccountNumber": "654321",
    "amount": 0
  }')

if echo $INVALID_AMOUNT | grep -q "Amount must be greater than zero"; then
    echo -e "${GREEN}✅ Validation working: $INVALID_AMOUNT${NC}\n"
else
    echo -e "${RED}❌ Validation failed${NC}\n"
fi

# Test 6: Same account transfer
echo -e "${BLUE}Test 6: Same Account Transfer Test${NC}"
SAME_ACCOUNT=$(curl -s -X POST "$BASE_URL/api/transactions/transfer" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "sourceAccountNumber": "123456",
    "destinationAccountNumber": "123456",
    "amount": 100.00
  }')

if echo $SAME_ACCOUNT | grep -q "Cannot transfer to the same account"; then
    echo -e "${GREEN}✅ Same account validation working${NC}\n"
else
    echo -e "${RED}❌ Validation failed${NC}\n"
fi

# Test 7: Missing JWT token
echo -e "${BLUE}Test 7: Missing JWT Token Test${NC}"
NO_TOKEN=$(curl -s -X POST "$BASE_URL/api/transactions/transfer" \
  -H "Content-Type: application/json" \
  -d '{
    "sourceAccountNumber": "123456",
    "destinationAccountNumber": "654321",
    "amount": 100.00
  }')

if echo $NO_TOKEN | grep -q "error\|unauthorized\|forbidden"; then
    echo -e "${GREEN}✅ JWT authentication is working (request rejected)${NC}\n"
else
    echo -e "${YELLOW}⚠️  Response: $NO_TOKEN${NC}\n"
fi

# Test 8: Check logs
echo -e "${BLUE}Test 8: Application Logs${NC}"
if [ -f "app.log" ]; then
    RECENT_LOGS=$(tail -5 app.log)
    echo -e "${GREEN}✅ Log file exists: app.log${NC}"
    echo "Recent logs:"
    echo -e "${YELLOW}$RECENT_LOGS${NC}\n"
else
    echo -e "${YELLOW}⚠️  Log file not found at app.log${NC}\n"
fi

echo -e "${BLUE}========================================${NC}"
echo -e "${GREEN}Testing completed!${NC}"
echo -e "${BLUE}========================================${NC}"
echo -e "\nAccess Swagger UI at: ${YELLOW}$BASE_URL/swagger-ui.html${NC}"
echo -e "OpenAPI Docs at: ${YELLOW}$BASE_URL/v3/api-docs${NC}"

