# domain-registrar

This is a cli application that can be used to calculate the total cost of domain ownership for a business/person based on a pre-defined (configurable) cost dataset.

## Pre-requisites

* Java JDK 1.8+ (dev)
* Gradle (dev)

## Usage

To run the cli application follow the below steps.

* Get the pre-compiled `jar` file of the program
* Run the `jar` file using the below command
  ```
  java -jar domain-registrar-all.jar
  ```

## Development

To futher develop the application, clone the git repository to your local machine.

```
$ git clone https://github.com/navyaranjan/domain-registrar
$ cd domain-registrar
```

### Build the project
You can run a development version of the application by building a uber jar using the `shadowJar`  gradle task.

```
$ ./gradlew shadowJar
```
This will build the `jar` with dependencies into `build/libs/`

You can run the jar using

```
$ java -jar build/libs/domain-registrar-all.jar
```

or using gradle.

```
$ gradle run
```