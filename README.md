# 🛒 Hyperium: Online Store Information System 🛒

## 🌐 Overview | Resumen 🌐

**Hyperium** is a comprehensive **Information System** designed to manage an **online store**. This project is divided into two main components: **TIENDA_FRONT** and **TIENDA_BACK**, both seamlessly integrated using **Spring Boot** and **JavaFX** technologies. Hyperium provides a user-friendly interface and robust operations, including **CRUD (Create, Read, Update, Delete)** functionality, role-based access control (**👩‍💻 Administrator, 🛍️ Client, 🏷️ Seller**), and secure **🔐 Login/SignUp** features. It's the perfect solution for managing products, orders, users, and more.

**Hyperium** es un completo **Sistema de Información** diseñado para gestionar una **tienda online**. Este proyecto está dividido en dos componentes principales: **TIENDA_FRONT** y **TIENDA_BACK**, ambos integrados de forma eficiente utilizando las tecnologías **Spring Boot** y **JavaFX**. Hyperium ofrece una interfaz fácil de usar y operaciones sólidas, incluyendo funcionalidad **CRUD (Crear, Leer, Actualizar, Eliminar)**, control de acceso basado en roles (**👩‍💻 Administrador, 🛍️ Cliente, 🏷️ Vendedor**) y funciones seguras de **🔐 Login/Registro**. Es la solución perfecta para gestionar productos, pedidos, usuarios y más.

---

## 🛠️ Features | Funcionalidades 🛠️

### TIENDA_FRONT
- 🎨 **Modern User Interface**: Developed with **JavaFX** to provide an attractive and user-friendly experience.
- 🧑‍💻 **Role-Based Access Control**:
  - 👩‍💻 **Administrator**: Full control over users, products, and orders.
  - 🏷️ **Seller**: Manage products and view orders.
  - 🛍️ **Client**: Browse products, place orders, and manage their profile.
- 🔐 **Login and SignUp**: Secure authentication and user registration system.
- 🔄 **Real-Time Data Updates**: Seamlessly integrated with the backend for dynamic updates.
- 📦 **Product Management**: View, add, update, and delete products.
- 🧾 **Order Processing**: Manage orders efficiently with intuitive workflows.
- 👥 **User Management**: Registration and login features with validation.
- 📊 **Statistics Dashboard**: Visualize store performance with charts and insights.

### TIENDA_BACK
- 🚀 **Robust Backend**: Powered by **Spring Boot** for high performance and scalability.
- 🌐 **Comprehensive APIs**: RESTful APIs to handle all store operations.
- 🛡️ **Role Management**: Assign roles (Administrator, Seller, Client) for secure access.
- 💾 **Data Persistence**: Fully integrated with a database for secure and reliable data storage.
- ✏️ **CRUD Operations**: Simplified management for all entities, including products, users, and orders.
- 🔒 **Security**: Implements authentication, authorization, and password encryption.

---

## 🚀 Technologies | Tecnologías 🚀

- ☕ **Java 21**: Core programming language.
- 🎨 **JavaFX**: Frontend framework for rich client applications.
- 🌱 **Spring Boot**: Backend framework for building APIs and managing business logic.
- 🗃️ **Oracle**: Database for persistent data storage.
- 📦 **Maven**: Dependency management and build tool.
- 🖍️ **FXML**: Markup language for JavaFX user interfaces.
- 🎨 **CSS**: Custom styling for an attractive UI.

---

## 📸 Screenshots | Capturas de pantalla 📸

### 📊 Dashboard
![image](https://github.com/user-attachments/assets/2d2c2508-ac8f-4284-9a3d-4aadb319889d)


### 📦 Product Management
![image](https://github.com/user-attachments/assets/8e204550-a25c-4c9a-8192-329ab1c8f8c7)


### 🔐 Login Page
![image](https://github.com/user-attachments/assets/1494bf77-026c-40cd-82f9-0bcf4efa8e65)

### 👥 Clients View
![image](https://github.com/user-attachments/assets/b35710d7-b052-4185-9309-c6d0fdfb73da)

### 🛒 Purchases View
![image](https://github.com/user-attachments/assets/73f50ceb-2520-4b11-a7b4-b4fc3b5b0a03)

### 🛍️ Buy View
![image](https://github.com/user-attachments/assets/b0817098-6446-42eb-9c95-148bad87b637)
![image](https://github.com/user-attachments/assets/34cd9ad3-1bc3-44fb-adc6-fea22d714811)

---

## 🗃️ Database Design | Diseño de Base de Datos 🗃️

Below is the **Entity-Relationship Model (ERM)** for the database used in Hyperium:

A continuación se muestra el **Modelo Entidad-Relación (MER)** para la base de datos utilizada en Hyperium:

![Imagen de WhatsApp 2024-11-27 a las 11 29 13_dfecb285](https://github.com/user-attachments/assets/94f07534-d97f-4d15-b446-fafa4ab83628)


**Key Entities | Entidades Clave:**
- **Users**: Stores user data and roles (Administrator, Seller, Client).
- **Products**: Contains product details such as name, price, and stock.
- **Orders**: Tracks purchase transactions.
- **Order Details**: Links products to orders with quantities and pricing.
- **Roles**: Defines user roles for access control.

**Relational Database System | Sistema de Base de Datos Relacional**: **Oracle**  
📋 **Database Design and Implementation | Diseño e Implementación de la Base de Datos**: **Angel de Jesús Quintero Rivera**



## 💻 Setup | Configuración 💻

### Prerequisites | Requisitos previos
1. ☕ **Java Development Kit (JDK)**: Version 17 or higher.
2. 📦 **Maven**: For building the project.
3. 🗃️ **Oracle**: Database server.
4. 🖥️ **IDE**: IntelliJ IDEA, Eclipse, or any Java-supporting IDE.

### Installation Steps | Pasos de instalación

1. 🌀 Clone the repository:
   ```bash
   git clone https://github.com/TheFive40/FRONT_TIENDA.git

Aquí está el texto actualizado para incluir un apartado de descargas para el **JAR del Backend**, **JAR del Frontend**, y el **Javadoc**:

---

## 📥 Downloads | Descargas 📥

### 🛠️ Backend JAR | JAR del Backend
You can download the executable backend file, which includes all the server functionalities:  
Puedes descargar el archivo ejecutable del backend, que incluye todas las funcionalidades del servidor:  
- **Backend JAR**: [Download here | Descargar aquí](https://download1501.mediafire.com/p8d9687tw4vgR_2DSuMLLcJ82kw-kBizevfdQtONbpVwVzuba8OxdzYmuphDpv_vUQC8fjVEtE_QJdCnK0LzZPqqGONyEAI8s97H5x-0SD_Bi438EnCMCEz3F3gN7uhNwOWOG4TdKKD8U_g5hij2z8MKX4Qpu2lo0koEswt5CAf4Sg/otcokczha7opsuv/BACK_TIENDA.jar)

### 🎨 Frontend JAR | JAR del Frontend
The executable frontend file includes the graphical interface developed with **JavaFX**:  
El archivo ejecutable del cliente (frontend) incluye la interfaz gráfica desarrollada en **JavaFX**:  
- **Frontend JAR**: [Download here | Descargar aquí](https://download1529.mediafire.com/w8pdlnz2cdogKiQyew0oFlPR1gsEMEMgLb1bJStlizvFY7jah2qZexaWxkFTMG_5u1TLh86dT_B0sBZRrfBPCiqhJ0DwfcG_uDj-OLMep3EXJdVbujECOOtPdanKwosxmu2Pv0crsx31ZM4Kbb6429rBAjOHloYdXSkv2Cdc3GCfxQ/k4i3tkqgdnmzqhj/Hyperium.jar)

### 📄 Javadoc
The documentation generated automatically with **Javadoc** details the classes, methods, and attributes of the project. It’s ideal for developers interested in contributing or understanding the codebase:  
La documentación generada automáticamente con **Javadoc** detalla las clases, métodos y atributos del proyecto. Es ideal para desarrolladores interesados en contribuir o entender el código base:  
- **Javadoc**: [Download here | Descargar aquí](https://download1501.mediafire.com/5m5vzh7stvigg19UsdjgFic-scepI_AKHUfLHDz3yBkKj8y2AEt-5fn7G9kI3fSY4TRjR-T1Xhu6dMusDLvlnOkIiM6Bt7XJPfygIQ9fMHXaiYBV_tcXasVbtasv2Bp-9dkSoqkdfGnb3nXgdfC3QKYQG3oYlDrpOQhet_d1CoryBg/189jt0x4wag8mz4/javadoc.rar)

---

### 📝 How to Run the JARs | Cómo Ejecutar los JARs  
1. Make sure you have **Java 17** or higher installed on your system.  
   Asegúrate de tener **Java 17** o superior instalado en tu sistema.  
2. Download the backend and frontend JAR files.  
   Descarga los archivos JAR del backend y frontend.  
3. **Run the Backend | Ejecuta el Backend**:  
   ```bash
   java -jar tienda_back.jar
   ```
   This starts the application server.  
   Esto inicia el servidor de la aplicación.  
4. **Run the Frontend | Ejecuta el Frontend**:  
   ```bash
   java -jar tienda_front.jar
   ```
   This opens the graphical user interface (GUI).  
   Esto abre la interfaz gráfica de usuario (GUI).

---


## 🛡️ License | Licencia 🛡️

This project is licensed under the **Creative Commons Attribution-NonCommercial 4.0 International (CC BY-NC 4.0)**.  
See the [LICENSE](LICENSE) file for more details.

Este proyecto está bajo la **Licencia Creative Commons Attribution-NonCommercial 4.0 International (CC BY-NC 4.0)**.  
Consulta el archivo [LICENSE](LICENSE) para más detalles.

---


## 🤝 Contributing | Contribuciones 🤝

🎉 Contributions are welcome! Feel free to open issues or submit pull requests to improve the project.

🎉 ¡Las contribuciones son bienvenidas! No dudes en abrir issues o enviar pull requests para mejorar el proyecto.

---
## 📞 Contact | Contacto 📞

✍️ **Authors | Autores**:  
- **Jean Franco Boom Bolaño** (Project Lead | Líder del Proyecto)  
📧 **Email**: [jeanf2805@gmail.com](mailto:jeanf2805@gmail.com)  

- **Angel de Jesús Quintero Rivera** (Database Designer | Diseñador de la Base de Datos)  
📧 **Email**: [angelquinteror@gmail.com](mailto:angelquintero@gmail.com)  

Feel free to reach out with any questions or feedback.  
No dudes en contactarnos para cualquier consulta o retroalimentación.


