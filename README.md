# Proximity Service Project (Chapter 1)

This project implements a high-performance **Proximity Service** designed to handle 100M Daily Active Users (DAU) and 5,000 Queries Per Second (QPS). The service allows users to discover nearby businesses with sub-100ms latency.

## 🚀 Key Features
- **Nearby Search**: Discover businesses within a specified radius using Geohash-based spatial indexing.
- **Business Management**: Manage business metadata (Name, Address, Location) with persistent storage.
- **Performance Optimized**: Sub-100ms response times achieved via multi-layered caching (Redis) and parallel grid lookups.

## 🏗️ Architecture
- **Framework**: Spring Boot 3.4.x (Java 21)
- **Data Layer**: PostgreSQL (Source of Truth) + Redis (Spatial Index & Cache)
- **Design Pattern**: Layered Architecture (Controller-Service-Repository)
- **Concurrency**: Optimistic Locking for consistency.

## 🧭 Development Environment (Agent OS)
The development process is guided by detailed rules and skills located in the [`.agent/`](./.agent/AGENT.md) directory.

### Core Documentation
- [**API Specification**](./api-spec.md): Detailed endpoint definitions.
- [**Technical Specification**](./01.proximity-service-spec.md): High-level system design and requirements.
- [**Navigation Index**](./.agent/AGENT.md): Guide to all internal rules and workflows.

## 🛠️ Getting Started
### 1. Local Infrastructure
Ensure Docker is running and spin up the required containers:
```bash
docker-compose up -d
```
*Note: Default credentials for PostgreSQL are `test/test`.*