## commands to build the jar files

- mvn clean package -U -e
- mvn clean generate-sources

### how to skip tests

**-Dmaven.test.skip=true**

### to resume just the module in the command

**mvn clean package -U -e -rf :pix-soap-api**

### to run test in one package
**mvn test -pl shared**

### to run test compiling the module
**mvn clean test -pl pix-application -am**

**mvn clean install -DskipTests**