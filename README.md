# Smart File Organizer

Smart File Organizer is a Java desktop application that automatically organizes the files in a selected folder into subfolders based on their file extensions. It includes a graphical interface, undo functionality, robust error handling, and detailed logging.

---

## ✨ Overview

This project was built as an object-oriented programming (OOP) exercise. It demonstrates key software engineering principles such as modular design, encapsulation, file system interaction, and graphical user interface (GUI) development with Java Swing.

Use cases include keeping a Downloads folder clean, organizing project assets, or batch-sorting mixed files.

---

## 🚀 Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/TomyRulliat-3ICS/ErasmusTask2.git
cd smart-file-organizer
```

### 2. Compile the project

Using Java 11 or higher:

```bash
javac -d out src/*.java 
```

### 3. Launch the application

```bash
java -cp out OrganizerGUI
```

> ![alt text](images/image-1.png)

---

## 🔮 Testing the Application

### Step 1: Create a test folder
The test_folder is usually already created with the project. However, you can freely add or modify files inside it for testing purposes.

If you need to recreate it manually:
```bash
mkdir test_folder
cd test_folder
```

Then, create five files of different types.
```bash
type nul > doc.pdf
type nul > image.jpg
type nul > script.py
type nul > note.txt
type nul > archive.zip
```

> ![alt text](images/image-2.png)

### Step 2: Use the app to organize the files

1. Open the app :
```bash 
java -cp out OrganizerGUI
```
2. Click **Browse** to select `test_folder`
>![alt text](images/image-3.png)
3. Click **Start** to begin sorting
>![alt text](images/image-4.png)

The app will:

- Create subfolders for each file extension
>![alt text](images/image-5.png)
- Move each file into the appropriate subfolder
>![alt text](images/image-6.png)
- Log the actions performed
>![alt text](images/image-7.png)


### Step 3: Undo the last operation

Click **Undo** to revert the last file movement. If the folder that was created becomes empty, it will be deleted automatically.

> ![alt text](images/image-8.png)
And then we can see that the folder disappears
![alt text](images/image-9.png)

By undoing all actions, we end up where we started.
>![alt text](images/image-10.png)
And then we can see that all folders disappears
![alt text](images/image-11.png)

---

## 📚 Features

- Sort files by extension automatically
- Create folders on the fly
- Move files using Java NIO
- Track file moves with timestamps
- Undo last operation with folder cleanup
- Graphical interface using Swing
- Full error handling (permissions, name conflicts, etc.)

---

## 📃 Logging

Each file move is logged with:

- Source path
- Destination path
- Timestamp

Logs are printed to the GUI log area for easy review.

---

## 🌐 Authors

Developed by Rulliat Tomy, Aouchiche Erwan and Farkas Dorian.

