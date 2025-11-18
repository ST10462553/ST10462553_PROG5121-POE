# ST10462553_PROG5121-POE

---

# 📌 **README – QuickChat Messaging Application (PROG5121 POE)**

## 🧑‍💻 **Student Information**

**Name:** Siyolise Ndololwana
**Module:** PROG5121 – Programming
**Assessment:** Portfolio of Evidence (Part 1, Part 2, Part 3)
**Year:** 2025

---

# 💬 **QuickChat – Messaging Application**

QuickChat is a Java-based messaging application developed as a three-part Portfolio of Evidence (POE) project.
The project demonstrates:

* Java fundamentals
* GUI components using **JOptionPane**
* Object-oriented programming
* File handling
* JSON processing using **Gson**
* Maven project structure
* Unit testing using **JUnit 4**
* Automated testing via **GitHub Actions**

---

# 🎯 **Project Objectives**

## **Part 1 – User Login System**

✔ Implement user registration
✔ Validate username (must contain underscore + ≤ 5 characters)
✔ Validate password complexity (upper-case, number, special char, ≥ 8 chars)
✔ Validate cellphone number (+27…)
✔ Display success/failed feedback
✔ Implement login functionality
✔ Create **JUnit tests** for login logic

---

## **Part 2 – Messaging System**

✔ Add a message-sending feature
✔ Create **Message** and **MessageSender** classes
✔ Use **JOptionPane pop-ups** for all input/output
✔ Store each message as a **JSON file** using **Gson**
✔ Auto-generate message IDs
✔ Include message timestamps
✔ Create **JUnit tests** for messaging features

---

## **Part 3 – Task Manager Extension**

✔ Add task creation functionality (Task class)
✔ Capture: task name, creator, description, status, and duration
✔ Display task details using JOptionPane
✔ Add validation and error-handling
✔ Store tasks in JSON
✔ Build complete menu/navigation using JOptionPane
✔ Implement **JUnit tests** for Task features
✔ Add automated testing workflow with **testJava.yml**
✔ Ensure GitHub pipeline builds and tests the project

---

# 🧱 **Project Structure**

```
/src
  /main/java/za/ac/iie/prog5121
      Login.java
      Message.java
      MessageSender.java
      Task.java
      Main.java

  /test/java/za/ac/iie/prog5121
      LoginTest.java
      MessageTest.java
      TaskTest.java

pom.xml
.github/
   workflows/
      testJava.yml
README.md
```

---

# 🛠 **Technologies Used**

* Java 17
* Maven
* Gson (JSON handling)
* JUnit 4 (unit testing)
* Swing (JOptionPane)
* GitHub Actions (automated CI testing)

---

# ▶️ **How to Run the Application**

### **Using Maven**

```
mvn clean package
mvn exec:java -Dexec.mainClass="za.ac.iie.prog5121.Main"
```

### **Using NetBeans**

1. Open the project folder
2. Ensure Maven loads dependencies
3. Right-click **Main.java**
4. Select *Run File*

---

# 🧪 **Running Unit Tests**

### Run all tests:

```
mvn test
```

### GitHub Actions

All tests run automatically on every push through:

```
.github/workflows/testJava.yml
```

---

# 📸 **Screenshots (Program Output)**




```





```

---<img width="197" height="91" alt="text summary" src="https://github.com/user-attachments/assets/5b9fcc6d-b2b2-4398-a6d3-b75c73357c1d" />
<img width="217" height="89" alt="id finder(error)" src="https://github.com/user-attachments/assets/953e9553-8bef-450d-88fa-d6b350feeb76" />


<img width="199" height="92" alt="sender name prompt" src="https://github.com/user-attachments/assets/79bc4eb0-7317-433d-aade-1583ab5eb8f6" />

<img width="370" height="91" alt="quickchat menu" src="https://github.com/user-attachments/assets/5a54a5f1-628a-4e53-bcb4-02af07013a05" />

<img width="203" height="137" alt="messaging works!" src="https://github.com/user-attachments/assets/f01197e2-772d-4e7d-8db8-d86d332f6b34" />

<img width="257" height="95" alt="message store function" src="https://github.com/user-attachments/assets/c9830bd3-cfe9-4157-9644-4f65be1395ef" />

<img width="273" height="119" alt="messages history" src="https://github.com/user-attachments/assets/531e4a32-2f17-45ec-9764-465ad1fec9de" />



# 📦 **JSON File Outputs**

Each sent message or task is stored as a `.json` file in the root directory.

Example message output:

```json
{
  "messageID": "MSG-001",
  "sender": "Siyolise",
  "receiver": "Buddy",
  "content": "Hello!",
  "timestamp": "2025-11-18T10:23:00"
}
```

---

# 📚 **References**

### 📌 AI Assistance

OpenAI. 2025. *ChatGPT (GPT-5.1)*. Available at: [https://chat.openai.com/](https://chat.openai.com/) (Accessed: 18 November 2025).

### 📌 W3Schools (General Java Reference)

W3Schools. 2025. *Java Tutorial*. Available at: [https://www.w3schools.com/java/](https://www.w3schools.com/java/) (Accessed:18 November 2025).

---

# ✅ **Academic Integrity Note**

This repository reflects my implementation of the PROG5121 POE components.
AI tools were used **only as a learning assistant**, not for generating final solutions without my own understanding.

---


