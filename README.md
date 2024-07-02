# VDL-RoboCup-Visualizer 🤖
The match visualization tool for VDL robocup football match held.

## Installing Maven

Maven is a build automation tool used primarily for Java projects. This application was created using this tool thus it needs to be installed for the compilation process.
The installation instructions vary depending on your operating system:

### Windows

1. Download the binary zip archive from the [Maven downloads page](https://maven.apache.org/download.cgi).
2. Extract the archive to a directory of your choice.
3. Add the `bin` directory from the extracted folder to your `PATH` environment variable.
4. Open a command prompt and type `mvn -v` to verify the installation.

### Linux

For Ubuntu/Debian-based systems:

```bash
sudo apt update
sudo apt install maven
```

## Building the application
To build the project, navigate to the directory containing your project's pom.xml file and execute:
```bash
mvn clean install
```
This will generate a `target` folder with the compiled application.

## Running the application
To run the application you must first navigate to the `target` folder and based on you operating system you proceed:

### Windows
- Double-click on the `runme.bat` file.

### Linux
Run the following in your command line:
```bash
chmod +x runme.sh
bash runme.sh

```



