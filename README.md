# Job Portal API

A professional-grade backend RESTful service built with **Spring Boot** and **Java**, designed to handle the complexities of job recruitment and candidate management. This project demonstrates proficiency in modern backend architecture, security, and containerized deployment.

---

## 🚀 Key Features

* **Role-Based Security:** Secure endpoints using **JWT (JSON Web Tokens)** with specific access levels for Recruiters, and Candidates.
* **Dynamic Job Filtering:** Advanced search capabilities using **JPA Specifications** for filtering by location, salary, and tech stack.
* **Data Persistence:** Robust relational mapping with PostgreSQL and automated schema management.

---

## 🛠️ Tech Stack

* **Language:** Java 21+
* **Framework:** Spring Boot 4.x (Security, Data JPA, AOP)
* **Database:** PostgreSQL
* **DevOps:** Docker, Docker Compose
* **API Testing:** Postman

---

## 📦 Getting Started

### Prerequisites
* **Docker** and **Docker Compose** installed on your machine.
* **Postman** (for testing the API endpoints).

### Installation & Deployment

1.  **Clone the repository:**
    ```bash
    git clone [git url]
    cd jobportal-project
    ```

2.  **Environment Setup:**
    Ensure your `application.properties` files are configured for the database.

3.  **Run with Docker:**
    Use Docker Compose to build and start the entire stack (API, Database) simultaneously:
    ```bash
    docker-compose up --build
    ```

4.  **Access the API:**
    The server will be live at `http://localhost:8080`.

---

## 🚦 API Testing with Postman

You can test the API endpoints directly:

[![Run in Postman](https://run.pstmn.io/button.svg)](https://www.postman.com/universal-meadow-295050/job-portal-api/collection/18299817-87313610-e99a-4b30-aac0-7608d9741264?action=share&source=copy-link&creator=18299817)


(or) I have included a pre-configured **Postman Collection** to simplify the testing process.

1.  Locate the `docs/Job Portal.postman_collection.json` file in the root directory.
2.  Open **Postman** and click **Import**.
3.  Drag and drop the `.json` file into the import window.
4.  **How to use:**
    * Set the environment variable `{{baseUrl}}` to `http://localhost:8080`.
    * First, use the **Login** or **Register** endpoint.
    * The collection is scripted to automatically save the JWT token to a variable, which is then used in the **Authorization** header for all other requests.

---

## 🏗️ Technical Architecture

* **Multi-Stage Docker Builds:** Optimized for performance and small image sizes.
* **Global Exception Handling:** Implemented `@ControllerAdvice` to provide consistent, user-friendly error messages.

---

## 📄 License

This project is open-source and available under the [MIT License](LICENSE).