# Banking Application

## Overview

This is a **medium-sized Java banking application** built using Maven. It demonstrates core banking functionalities such as account management, deposits, withdrawals, transfers, and transaction history tracking. The application follows a layered architecture with clear separation of concerns:

- **Model Layer:** Domain entities like `Account` and `Transaction`.
- **Repository Layer:** In-memory data storage abstraction.
- **Service Layer:** Business logic and validation.
- **Controller Layer:** Console-based interface for user interaction.
- **Unit Tests:** Comprehensive test coverage using JUnit 5.

---

## Features

- Create bank accounts with initial balance and account holder information.
- Deposit and withdraw money with validation (no negative amounts, no overdrafts).
- Transfer funds between accounts with validation.
- Maintain detailed transaction history per account.
- Handle invalid inputs and error scenarios gracefully.
- Unit tests covering both valid and invalid cases.

---

## Technologies Used

- Java 17
- Maven for build and dependency management
- JUnit 5 for unit testing


---

## Setup and Running the Application

### Prerequisites

- Java 17 or higher installed
- Maven installed

### Build the Project

Open a terminal in the project root directory and run:

`mvn clean compile`


### Run the Application

Run the main class using:

`mvn exec:java -Dexec.mainClass="com.example.banking.BankingApplication"`


You will see sample operations such as account creation, deposits, withdrawals, transfers, and transaction history printed to the console.

---

## Running Unit Tests

To execute the unit tests, run:

`mvn test`


The tests cover:

- Successful account creation, deposits, withdrawals, and transfers.
- Invalid input scenarios such as negative amounts, null or empty account numbers, overdrafts, and transfers to non-existing accounts.


---

## Future Enhancements

- **Persistence:** Integrate a database (e.g., MySQL, H2) for data storage.
- **Security:** Add authentication and authorization using Spring Security.
- **Web Interface:** Build REST APIs with Spring Boot and a frontend using React or Angular.
- **Business Rules:** Add complex rules using a rule engine like Drools.
- **Loan and Card Management:** Extend domain model to support loans, credit/debit cards, and payments.
- **Logging and Auditing:** Implement detailed logging for compliance and troubleshooting.

---

## Contributing

Contributions are welcome! Please fork the repository and submit pull requests for bug fixes or new features.

---

## License

This project is licensed under the MIT License.

---

If you need help with any enhancements or deployment, feel free to ask!






