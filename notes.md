# My notes

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
|----------------------------| ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |
`mvn clean install`

The Java String class has a bunch of methods that help manipulate strings. Here is a sampling of some of the more commonly used ones.

Method |	Purpose
length |	Get the length of the string
charAt |	Get the character at the given index
startsWith |	Does the string start with the given substring
indexOf |	Get the index of the given substring
substring |	Return the substring at the given index
format |	Create a string from a format template and parameters
toLowerCase | 	Drop all the character case
split |	Split the string into an array based on the given substring
replace |	Replace the substring in the string

git add notes.md
git commit -am " "
git push