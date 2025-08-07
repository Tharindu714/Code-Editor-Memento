# 🌒 Code-Editor-Memento

> A sleek, dark-themed Java Swing code editor with robust **Undo/Redo** powered by the **Memento** Pattern.

---

## 📝 Scenario

> You are building an **online code editor** that lets users write and edit code in real-time. It needs **Undo** and **Redo** functionality without exposing internal state or hogging memory.

<p align="center">
<img width="1000" alt="image" src="https://github.com/user-attachments/assets/f7914b49-94fe-4a11-a040-b6aea28d32b9" />
</p>

---

## ⚙️ Features

* **Dark Mode UI**: Eye-friendly dark background with light-colored monospaced font.
* **Memento-based History**: Encapsulated state snapshots enable efficient undo/redo.
* **Stack-based Navigation**: Unlimited undo/redo operations via two history stacks.
* **Fluent GUI Controls**: Toolbar buttons for seamless user interaction.

---

## 📸 GUI Screenshot

<p align="center">
<img width="1366" height="723" alt="image" src="https://github.com/user-attachments/assets/70003de1-8088-4545-bfac-ca338f2b23ea" />
</p>
---

## 📐 UML Class Diagram

<p align="center">
  <img width="183" height="435" alt="image" src="https://github.com/user-attachments/assets/cb0ed9d3-1535-48be-ad37-b5cfefcc52ea" />
</p>

> *Generate and paste UML diagram here.*

---

## 📖 Memento Pattern Theory

The **Memento** Pattern provides the ability to restore an object to its previous state without exposing its internal structure. It involves three key participants:

1. **Originator** 🏭

   * Holds the current state of the editor document (`String state`).
   * Can create a **Memento** capturing its state and restore from it.

2. **Memento** 🎁

   * A simple, immutable value object that stores the internal state snapshot.
   * Does **not** expose its internals beyond what the Originator needs.

3. **Caretaker** 🤵

   * Manages saving and retrieving Memento objects.
   * Maintains two stacks: one for **undo** and one for **redo** history.
   * Controls when to clear redo history to preserve correct behavior.

### How It Works in Our App

1. **On Every Edit**: A new Memento is saved to the **undo** stack. The **redo** stack is cleared.
2. **Undo** 🔙: Pop the top Memento from undo → push it to redo → restore the previous snapshot.
3. **Redo** 🔜: Pop the top Memento from redo → restore it → push it back on undo.
4. **Encapsulation**: The editor’s text content is never exposed; only the Originator knows its state.

---

## 🚀 Usage

```bash
# Clone the repo
git clone https://github.com/Tharindu714/Code-Editor-Memento.git
# Open in your favorite IDE
gradle run   # or mvn compile exec:java
```

---

## 🤝 Contributing

Pull requests and issues are welcome! Feel free to improve docs, add features, or optimize performance.

---

## 📄 License

This project is licensed under the MIT License. See [LICENSE](LICENSE) for details.


