# Java CLI TOOL
A lightweight, fast Java CLI utility built to automate testing for competitive programming platforms (such as **CSES**), comparing `.in` inputs against expected `.out` outputs. 

While primarily designed and optimized for **C and C++** workflows, it can execute binaries or scripts from other compiled or interpreted languages (such as **Rust**, **Go**, Python, etc.), making it versatile for multi-language problem solvers.

---

## 🚀 Features

* **Zero-Argument Auto-Detection:** Automatically detects local executables (matching the current folder name or located in a `build/` directory) and scans a local `tests/` folder without requiring manual flags.
* **Smart String Normalization:** Automatically trims and normalizes newlines (`\r\n` vs `\n`) and whitespace variations to prevent false failures due to formatting quirks.
* **ANSI Colored Logs:** Clear, color-coded console output (Green for `PASSED`, Red for `FAILED`/`ERROR`, Yellow for `TIMEOUT`) powered by Jansi and Picocli.
* **In-Memory Comparison:** Direct stream reading from process execution without generating cluttering `.myout` files.
* **Custom Timeouts:** Configurable test timeouts (e.g., `5s`, `2m`, `1h`).

---

## 📦 Installation & Releases

You can download pre-built JAR releases directly from the [GitHub Releases Page](https://github.com/nostronmo/cses_test_cli_tool/releases).

### Building from Source (Maven)
If you want to build or modify the tool locally:

1. Clone the repository:
   ```bash
   git clone [https://github.com/nostronmo/cses_test_cli_tool.git](https://github.com/nostronmo/cses_test_cli_tool.git)
   cd cses_test_cli_tool
Build the fat JAR using Maven:

Bash
mvn clean package
Copy the generated JAR (target/ctooltester-version.jar) into your problem working directories.

📂 Expected Directory Structure
Place the JAR file or symlink it in your project root alongside your solution and test folder:

Plaintext
my_cses_project/
├── ctooltester.jar          # The test runner JAR
├── main.c                   # Your source code (or main.go, main.rs, etc.)
├── build/                   # Compiled binary output (optional)
│   └── weird_algo
└── tests/                   # Test cases folder
    ├── 1.in
    ├── 1.out
    ├── 2.in
    └── 2.out
💻 Usage
1. Zero-Argument Execution (Auto-Detect)
If your folder is named weird_algo, your binary is in ./weird_algo or build/weird_algo, and your tests are in a tests/ folder, simply run:

Bash
java -jar ctooltester-v1.301.jar
2. Custom Arguments
You can explicitly pass an executable path, test directory, or timeout limit:

Bash
java -jar ctooltester-v1.301.jar -e build/weird_algo -td tests -tm 2s
⚙️ CLI Options Reference
Option	Alias	Description	Default
-e, --executable	-e, --e	Path to the compiled binary or script executable.	Auto-detected from folder name or build/
--td, --tdir	-td, -tdir	Path to the directory containing .in and .out test files.	Auto-detects ./tests
--tm, --time	-tm, -time	Max execution timeout duration (e.g., 500ms, 5s, 1m).	5s
-h, --help	-h, --h	Display help menu and usage instructions.	—
-v, --version	-v, --version	Display application version.	—
🛠️ Tech Stack
Java 17+

Picocli – Command-line parsing and ANSI formatting helpers.

Lombok – Boilerplate reduction (@Slf4j).

Jansi – Cross-platform ANSI terminal coloring.

📄 License
This project is open-source and available under the MIT License.
