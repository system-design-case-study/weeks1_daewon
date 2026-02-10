# 📄 Proximity Service API Specification

This document defines the detailed specifications for all APIs provided by the 'Proximity Service' backend system. Based on local environment (HTTP) and Layered Architecture.

## 1. Common Response Format
All responses follow the JSON structure below.

```json
{
  "success": boolean,
  "data": object | null,
  "error": {
    "code": string,
    "message": string
  } | null,
  "timestamp": "ISO-8601 DateTime"
}
```

---

## 2. Nearby Search API

### `GET /search/nearby`
Returns a list of businesses within a specified radius based on the user's location.

#### **Request Parameters**
| Parameter | Required | Type | Description |
| :--- | :--- | :--- | :--- |
| `lat` | Y | Decimal | Search center latitude (-90.0 ~ 90.0) |
| `lon` | Y | Decimal | Search center longitude (-180.0 ~ 180.0) |
| `radius` | N | Integer | Search radius (unit: meters, default: 5000) |
| `limit` | N | Integer | Max return count (default: 20) |

#### **Success Response (200 OK)**
```json
{
  "success": true,
  "data": {
    "total": 15,
    "businesses": [
      {
        "id": "uuid-1234",
        "name": "Starbucks Gangnam",
        "latitude": 37.4979,
        "longitude": 127.0276,
        "distance": 150.5,
        "address": "Gangnam-daero, Gangnam-gu, Seoul..."
      }
    ]
  },
  "error": null,
  "timestamp": "2024-02-07T23:00:00Z"
}
```

---

## 3. Business Management API

### `GET /business/:id`
Retrieves detailed information of a specific business. (Redis cache lookup priority)

#### **Success Response (200 OK)**
```json
{
  "success": true,
  "data": {
    "id": "uuid-1234",
    "name": "Starbucks Gangnam",
    "description": "A place to enjoy various coffee and desserts",
    "address": "Gangnam-daero, Gangnam-gu, Seoul...",
    "latitude": 37.4979,
    "longitude": 127.0276,
    "openTime": "07:00",
    "closeTime": "22:00"
  },
  "error": null,
  "timestamp": "2024-02-07T23:00:00Z"
}
```

### `POST /business`
Creates a new business. (Master DB persistence & Redis index addition)

#### **Request Body**
```json
{
  "name": "New Business",
  "latitude": 37.5,
  "longitude": 127.0,
  "address": "Seoul...",
  "description": "Description..."
}
```

### `PUT /business/:id`
Updates existing business information.

### `DELETE /business/:id`
Deletes business information. (DB deletion & Redis cache eviction)

---

## 4. Error Codes
| Code | Description |
| :--- | :--- |
| `INVALID_PARAMETER` | Input parameter is invalid |
| `BUSINESS_NOT_FOUND` | Business with the given ID does not exist |
| `GEO_INDEX_ERROR` | Error occurred during spatial indexing |
| `INTERNAL_SERVER_ERROR` | Internal server error |
