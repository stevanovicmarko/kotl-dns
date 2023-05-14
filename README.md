DNS resolver in Kotlin

# A simple toy DNS resolver written in Kotlin

* Small kotlin experiment of writing my own DNS resolver. It's not complete, but it works for most of the cases.
* To build it, first do you need to have JDK installed. After that do:

```shell
./gradlew clean build
```
* To ran it you can:


Use gradlew:
```shell
./gradlew run --args="example.com"
```
Or you can run it as a fat jar:
```shell
java -jar build/libs/dns-resolver-1.0-SNAPSHOT.jar example.com
```


