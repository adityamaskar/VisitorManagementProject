# Visitor Management Projects

---

## 1️⃣ Visitor Management Project with Eureka Discovery Server and Gateway

### 📝 Description
This application enhances security and streamlines visitor management for large societies and towers. It allows users to:
- **Register and submit visit requests.**
- **Approve or reject requests** as owners.
- **Track visitor lifecycles** in real-time, including visit times, scheduled dates, and exit times.
- Specify **visit types** and **vehicle information** for improved safety and management.

---

### 💻 Tech Stack

#### 🔧 Backend
- **Java 17** with **Spring Boot 3.2.2**
- **JWT** for login authentication
- **Lombok** for simplifying code
- **MySQL** Database
- **Netflix Eureka Discovery Server** and **Gateway**
- **Kafka Messaging** for notifications
- **Docker** for Broker and Zookeeper

#### 🎨 Frontend
- **React 18.2.0** with **TypeScript**
- **Vite** for optimized builds
- **Axios** for API communication
- **Zod** for validations

---

### 🛠️ Development Tools
- **IDE:** IntelliJ and Visual Studio

---

### 🎥 Project Showcase
- **Frontend Flow Video:** [Explore User Interface and Interactions](https://www.linkedin.com/posts/adityamaskar_fullstack-visitormanagement-techinnovation-activity-7189261897890746368-m7tk?utm_source=share&utm_medium=member_desktop)
- **Backend Flow Video:** [Understand the High-Level Structure](https://www.linkedin.com/posts/adityamaskar_fullstack-visitormanagement-techinnovation-activity-7189261897890746368-m7tk?utm_source=share&utm_medium=member_desktop)

---

### 🔗 Microservices Repositories
1. **VisitorManagementProject (Main Component):** [GitHub Link](https://github.com/adityamaskar/VisitorManagementProject)
2. **VP-Gateway:** [GitHub Link](https://github.com/adityamaskar/VP-Gateway)
3. **VP-DiscoveryServer:** [GitHub Link](https://github.com/adityamaskar/VP-DiscoveryServer)
4. **VP-Notification-Service:** [GitHub Link](https://github.com/adityamaskar/VP-Notification-Service)

**Note:** UI repository will be added soon.

---

## 2️⃣ Visitor Management Project on Kubernetes/Docker (AKS)

### 🚀 Enhanced Without Eureka Discovery Server and Gateway

---

### 📝 Description
This upgraded version delivers superior scalability and deployment efficiency. By leveraging **Docker** and **Kubernetes**, it eliminates the need for Eureka Service and Gateway, utilizing Kubernetes for service discovery and load balancing.

The application is fully containerized and deployable on **Azure Kubernetes Service (AKS)**, offering a robust and scalable infrastructure suitable for modern cloud platforms.

---

### 💻 Tech Stack

#### 🔧 Backend
- **Java 17** with **Spring Boot 3.2.2**
- **JWT** for secure login authentication
- **Lombok** for code simplification
- **MySQL** Database
- **Kafka** for real-time notifications
- **Docker** for containerization

#### 🎨 Frontend
- **React 18.2.0** with **TypeScript**
- **Vite** for faster builds
- **Axios** for seamless API communication
- **Zod** for validation

#### 📦 Deployment
- **Kubernetes** for orchestration and service discovery
- **Azure Kubernetes Service (AKS)** for cloud deployment

---

### 📂 Kubernetes Changes
The Kubernetes changes for containerization and deployment are available in the following branches:
1. **VisitorManagementProject (Main Component):**  
   [GitHub Link - dockerizing-the-app branch](https://github.com/adityamaskar/VisitorManagementProject/tree/dockerizing-the-app)

2. **VP-Notification-Service:**  
   [GitHub Link - dockerizing-app branch](https://github.com/adityamaskar/VP-Notification-Service/tree/dockerizing-app)

---

### 📂 Additional Details
This application is designed for:
- **Seamless Deployment**: Fully containerized for consistent and efficient operations.
- **Environment Flexibility**: Supports separate environments for development, testing, and production.
- **Scalability**: Effortlessly handles increasing workloads with AKS integration.

---

> **✨ Pro Tip:** The project can also be deployed on other cloud providers, offering flexibility across platforms.

---

💡 **Have questions or suggestions?** Feel free to [reach out on LinkedIn](https://www.linkedin.com/in/adityamaskar/) or open an issue in the GitHub repository!  
