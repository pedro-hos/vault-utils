# vault-utils

Simple Project Utils for Quarkus file vault

## First Step

You shuld to have a Quarkus application that connects to some database without the VAULT configured. For example [Quarkus demo: Hibernate ORM with Panache and RESTEasy](https://github.com/quarkusio/quarkus-quickstarts/tree/main/hibernate-orm-panache-quickstart)

## Keystore Step 

Add passwords to the keystore that will be used as Vault

```
keytool -importpass -alias keyclaok -keystore vault/dbpasswords.p12 -storepass storepassword -storetype PKCS12
```

## Build the Quarkus File Vault

1. Fork and Clone the   https://github.com/pedro-hos/quarkus-file-vault/tree/encrypt-secret with the changes. **The changes are on encrypt-secret branch**

2. Package the project:

```
mvn clean install -DskipTests -Dinsecure.repositories=WARN
```

## Using the Vault Utils

1. Fork and clone this project (https://github.com/pedro-hos/vault-utils)
2. Package the project:

```
mvn clean install
```
3. Encrypting the secret. You can run the `--help` paramenter, to see the options:

```
$ java -jar target/quarkus-app/quarkus-run.jar --help

Usage: Encrypt Secret Util [-hV] [-i=<iterationCount>] -p=<keystorePassword> [-s=<salt>] [-sk=<secretKey>]
  -h, --help          Show this help message and exit.
  -i, --iteration=<iterationCount> (optional) Iteration count
  -p, --keystore-password=<keystorePassword> (mandatory) Keystore password
  -s, --salt=<salt>   (optional) 8 character salt
  -sk, --secrect-key=<secretKey> (optional) Secret Key
  -V, --version       Print version information and exit.
```
The only mandatory parameter is `-p, --keystore-password` the others are optional.

You can create the mask for example:

```
$ java -jar target/quarkus-app/quarkus-run.jar -p storepassword
```
You should to see something like that at the output:

```
##################################################################################
Please add the following paramenters on your application.properties file, and replace the <key> value!
quarkus.file.vault.provider.<key>.encrypted=true
quarkus.file.vault.provider.<key>.secret=urbqHgSpI9PpcpAvIuDDog==
##################################################################################
```

Save this for the next step.

## Using the Quarkus File Vault 

1. Add the following dependencie on your project:

```
<dependency>
    <groupId>io.quarkiverse.file-vault</groupId>
    <artifactId>quarkus-file-vault</artifactId>
    <version>0.4.0</version>
    <scope>system</scope>
    <systemPath>/runtime/target/quarkus-file-vault-999-SNAPSHOT.jar</systemPath>
</dependency>
```
Change the `<systemPath></systemPath>` with the full quarkus file vault jar file **runtime** path.

2. Add the following on your `application.properties` file:

```
quarkus.datasource.credentials-provider=quarkus.file.vault.provider.db1
quarkus.file.vault.provider.db1.path=vault/dbpasswords.p12
quarkus.file.vault.provider.db1.alias=keycloak
```

**The alias is the same that you've used during the keytool step, and also, the same as the db username**

3. add the properties generated on the vault-utils step

```
quarkus.file.vault.provider.db1.encrypted=true
quarkus.file.vault.provider.db1.secret=urbqHgSpI9PpcpAvIuDDog==
```

Finally, your application.properties file should be something like:

```
quarkus.datasource.db-kind=mysql
quarkus.datasource.jdbc.url=jdbc:mysql://localhost:3306/keycloak?useSSL=false
quarkus.datasource.credentials-provider=quarkus.file.vault.provider.db1
quarkus.file.vault.provider.db1.path=vault/dbpasswords.p12
quarkus.file.vault.provider.db1.encrypted=true
quarkus.file.vault.provider.db1.secret=urbqHgSpI9PpcpAvIuDDog==
quarkus.file.vault.provider.db1.alias=keycloak
quarkus.hibernate-orm.database.generation=create-drop
```

Now, you'd be able to use the database, with vault and secret masked.





