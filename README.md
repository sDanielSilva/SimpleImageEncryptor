# 🔐 Simple Image Encryptor - DES/AES Encryption

Welcome to **Simple Image Encryptor**, a sleek and modern tool that allows you to **encrypt and decrypt images** using DES and AES encryption with two modes: **ECB** and **CBC**. This project is a demonstration of how DES and AES encryption works in a simple, easy-to-understand way, making it perfect for learning and experimenting with cryptography!

✨ **Key Features:**
- DES and AES encryption in **ECB** and **CBC** modes
- Encrypt and decrypt image files seamlessly
- Choose between multiple AES key sizes: **128, 192, 256**
- Intuitive and easy-to-use

## 🖼️ Example

Take a look at how the encryption process transforms your images!

| **Original Image**                | **Encrypted Image (ECB Mode)**      | **Encrypted Image (CBC Mode)**     |
|-----------------------------------|-------------------------------------|------------------------------------|
| ![Original Image](images/original.bmp) | ![Encrypted Image ECB](images/encryptedECB.bmp) | ![Encrypted Image CBC](images/encryptedCBC.bmp) |



## 🚀 Getting Started

### Prerequisites

Before running the project, ensure you have the following installed:

- Java (JDK 17 or above)
- Maven (for dependency management)
- Git (for version control)
- An image file to test encryption!

### Installing

1. **Clone the repository**:

```bash
git clone https://github.com/sDanielSilva/SimpleImageEncryptor.git
cd SimpleImageEncryptor
```

2. **Build the project** using Maven:

```bash
mvn clean install
```

3. **Run the application**:

```bash
java -jar target/SimpleImageEncryptor.jar
```

### Usage

When you run the program, you will be prompted to:

1. Choose the operation:
   - `1` for Encrypt
   - `2` for Decrypt

2. Select the encryption mode:
   - `1` for ECB
   - `2` for CBC

3. Choose the encryption algorithm:
   - `1` for AES
   - `2` for DES

4. If AES, specify the key size (128, 192, 256).
5. Enter the file paths:
   - Encryption Key, Input and Output file paths (for decryption)
   - Input and Output image file path (for encryption)

For example:

```bash
Choose operation (1 - Encrypt, 2 - Decrypt): 1
Choose mode (1 - ECB, 2 - CBC): 2
Choose algorithm (1 - AES, 2 - DES): 1
Enter AES key size (128, 192, 256): 256
Enter input file path: images/original.bmp
Enter output file path: images/encrypted.bmp
```

### Configuration

- **Key Sizes**: AES supports three key sizes: **128 bits**, **192 bits**, and **256 bits**.
- **Modes**: You can select either **ECB** (Electronic Codebook) or **CBC** (Cipher Block Chaining) mode for encryption.

## 🎯 Roadmap

Here’s a quick look at what’s coming next:

- Support for more encryption algorithms (RSA, DES)
- Enhanced UI/UX (GUI for image upload, encryption, and download)

## 🤝 Contributing

Feel free to fork the project and submit a pull request if you have ideas for improvements, new features, or bug fixes. We'd love to see your contributions!

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a pull request

## 💬 Contact

- **Developer:** [Daniel Silva](https://github.com/sDanielSilva)
- **Email:** d.m.silva@ua.pt

---

### ⭐️ Support This Project

If you like the project, please ⭐️ it! Any support is greatly appreciated.
