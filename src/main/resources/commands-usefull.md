## commands to build the jar files

- mvn clean package -U -e
- mvn clean generate-sources

### how to skip tests

**-Dmaven.test.skip=true**

### to resume just the module in the command

**mvn clean package -U -e -rf :pix-soap-api**