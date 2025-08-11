# Java CLI Banking Application - Assignment Setup

This project structure has been set up to help you complete the Banking Application assignment.

## Project Structure

The directory structure follows the recommended package structure from the assignment:

```
src/main/java/com/bank/
├── cli/                    # Entry point and CLI handling
│   └── display/           # CLI output, menu, printing classes
├── auth/                  # Authentication business logic
├── user/                  # Customer profile management
├── account/               # Bank account management
├── transaction/           # Banking transaction logic
├── logging/               # Logging logic
├── db/                    # Database interaction layer
│   ├── auth/             # DB access for auth data
│   ├── user/             # DB access for user data
│   ├── account/          # DB access for account data
│   ├── transaction/      # DB access for transaction data
│   └── logging/          # DB access for logs
├── dto/                   # Data Transfer Objects
├── mapper/                # Entity ↔ DTO mappers
├── orchestrator/          # Service coordinators
└── exception/             # Custom exception classes
```

## Database Layer

A simple database layer has been provided in `com.bank.db.DatabaseManager` that exposes a single function:
- `query(String sql)` - Returns an array of rows as `List<Map<String, Object>>`

## Getting Started

1. Implement the main CLI entry point in `com.bank.cli.Main`
2. Create your DTOs in the `dto` package
3. Implement services in their respective packages
4. Use the provided database layer for data persistence
5. Create custom exceptions in the `exception` package

## Key Requirements to Remember

- Use DTOs to transfer data between layers
- Implement Mappers to convert between Entities and DTOs
- Use Orchestrators for complex business workflows
- Handle all exceptions gracefully
- Log all important actions using the database
- Follow Java naming conventions

## Database Schema

You'll need to create tables for:
- Users/Customers
- Accounts (Savings, Fixed Deposit)
- Transactions
- Logs

The database layer is ready to use - just pass your SQL queries to the `query()` method.

Good luck with your assignment!
