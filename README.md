# COCTEAU

<img src="./readme_img/Logo-cocteau.png" width="300">

COCTEAU - Co-Creating The EuropeAn Union - is a crowdsourcing tool that has the goal of enabling large-scale citizen engagement and co-creation in support of policymakers. The main goals of COCTEAU align with the need of policymakers for citizen input when approaching complex governance solutions and are:

-   Collection of ideas and thoughts from communities to help policymakers to brainstorm more sustainable policies regarding modern societal problems (COVID-19, AI, Internet Policy, SDGs, etc.)
-   Enhancement of governments’ capacity to foresee solutions that have a better fit between policymakers’ preferences and people’s vision of the future
-   Creation of a spontaneous communication between (local) governments and specific communities, contributing to recover trust in public institutions and policymakers

## Method

COCTEAU underwent a design and development process inspired by the literature regarding co-creation approaches in policy making settings and in-person workshops which led to the development of a web application. The final web application implements a gamified interface focused on emotional and vision-oriented content that resulted from an iterative integration of three rounds of validation by expert and non-expert users. Through this application, policymakers design challenges for citizens to partake, cooperate and provide their input.

Challenges are expressed through Scenarios which are either distillations of societal problems, a possible solution for them or possible futures. Scenarios are grouped depending on their Topic. The textual description of the Scenario is paired with a quiz with two objectives:

-   getting users more acquainted with the Scenario and triggering some initial reactions and thought processes among users

-   two dimensions through which the Scenario can be inspected (innovation, feasibility, impact, etc.)

    

The emotional component of COCTEAU arises with the creation of Visions. COCTEAU users can create Visions to express their opinion and feelings regarding a Scenario both in visual and textual terms. A Vision is like a post on a social media platform and it comprises a collage of three to four pictures, three keywords, a subjective evaluation of the Scenario dimensions and a comment. Citizens can create Visions freely and share them with other members of the community participating in challenges.

## License  

The research and development of COCTEAU are partially supported by the European Commission under the H2020 framework, within project 822735 [TRIGGER](https://trigger-project.eu) (TRends in Global Governance and Europe's Role) and project 101016233 [PERISCOPE](https://periscopeproject.eu/) (Pan-European Response to the Impacts of COVID-19 and future Pandemics and Epidemics).

The main contributors of this project are [Politecnico di Milano](https://polimi.it) and [CEPS](https://www.ceps.eu).

| <img src="./readme_img/Logo-Polimi.png" width="500"> | <img src="./readme_img/Logo-CEPS.jpg" width="500"> |
| ------------- | ------------ |

The content of this project is licensed under the [Creative Commons Attribution 3.0 Unported license](https://creativecommons.org/licenses/by/3.0/), and the underlying source code used to format and display that content is licensed under the [MIT license](LICENSE.md).

## Run it on your machine

### Stack

COCTEAU has been implemented as a Spring Boot web application.

The backend has been implemented in Java whilst the frontend has been implemented using [Thymeleaf](https://www.thymeleaf.org), Javascript, CSS and HTML.

Persistent data is stored in a MySQL database. Version `5.6.46` is in use and version `5.7.17` was tested and works as well.

### Project Dependencies

The dependencies for this project are managed via Maven.

We report the depencies as defined in the `pom.xml` file:

| Dependency Name                  | Version in use | Managed |
| -------------------------------- | -------------- | ------- |
| `spring-boot-starter-data-jpa`   | `2.2.4`        | &check; |
| `spring-boot-starter-thymeleaf`  | `2.2.4`        | &check; |
| `spring-boot-starter-web`        | `2.2.4`        | &check; |
| `spring-boot-starter-test`       | `2.2.4`        | &check; |
| `spring-boot-starter-validation` | `2.2.4`        | &check; |
| `spring-boot-starter-security`   | `2.2.4`        | &check; |
| `spring-boot-devtools`           | `2.2.4`        | &check; |
| `spring-security-crypto`         | `5.2.1`        | &check; |
| `spring-security-test`           | `5.2.1`        | &check; |
| `mysql-connector-java`           | `8.0.19`       |         |
| `commons-collections4`           | `4.4`          |         |
| `commons-lang3`                  | `3.9`          | &check; |
| `commons-fileupload`             | `1.3.3`        |         |
| `opencsv`                        | `4.1`          |         |
| `json`                           | `20190722`     |         |
| `bcprov-jdk15on`                 | `1.67`         |         |
| `bootstrap`                      | `4.4.1`        |         |
| `jquery`                         | `3.4.1`        |         |
| `popper.js`                      | `1.14.1`       |         |

### Installing and Running the tool

#### Setup MySQL

It is first necessary to create the MySQL database COCTEAU is going to connect to. 

To do so, run from terminal:

~~~bash
mysql -u your_username -p database_name < dump/cocteau.sql
~~~

_You may have to create the empty database first._

We already provide a basic version of the Database with pre-defined Achievements and a **root** Administrator. A dummy user is also provided. Do not remove it from the Database since they are the owner of all the Visions created by the Administrators when configuring the Scenario.

As an alternative, you can import the DB in MySQL Workbench. The following steps must be performed to correctly import the Database:
- Create an empty Schema
- In MySQL look for "Server" > "Data Import"
    - "Import from Self-Contained File" and select dump/cocteau.sql
    - Select the Schema you created in "Default Target Schema"
    - Press "Start Import"
If everything went fine, you should have your basic Database imported.

#### Setup the application properties

Spring automatically loads the `application.properties` from the project classpath. Make sure the file is presented under the `src/main/resources` directory.


| Property Name                         | Description                                                                             |
| ------------------------------------- | --------------------------------------------------------------------------------------- |
| `spring.datasource.url`               | URL to the running MySQL server                                                         |
| `spring.datasource.username`          | MySQL user authentication                                                               |
| `spring.datasource.password`          | MySQL user authentication                                                               |
| `spring.datasource.driver-class-name` | Datasource connector. Default value: `com.mysql.cj.jdbc.Driver`                         |
| `server.port`                         | Port on which COCTEAU will run                                                          |
| `security.require-ssl`                | Flag expressing whether or not a secure channel is required                             |
| `server.ssl.key-store`                | Path to the keystore                                                                    |   
| `server.ssl.key-store-password`       | Password of the keystore                                                                |
| `server.ssl.keyStoreType`             | Type of the keystore. Default value: `PKCS12`                                           |
| `server.ssl.keyAlias`                 | Keystore alias                                                                          |
| `unsplash.accesskey`                  | API key for Unsplash. Need to create an account                                         |
| `gcptranslate.key`                    | API key for Google Cloud Translation API. Need to create a GCP account and project      |
| `postWidth`                           | COCTEAU Vision Width. Default value: `600` px                                           |
| `postHeight`                          | COCTEAU Vision Height. Default value: `600` px                                          |
| `fakeUserCookie`                      | Cookie of the user to attribute initial vision to. Default value: `00000000000000000000`|

#### Setup Experiments
Experiments on COCTEAU are run by means of Scenarios. Only administrators can create Scenarios via COCTEAU itself.
By default an administrator account with root privileges is provided with the following credentials:

~~~bash
username: dummy_admin
password: dummy_admin
~~~

Having root privileges means that, using that account, new administrator accounts can be created.

**To avoid problems it is strongly advised to create another administrator account using** `dummy_admin`**, setting the property** `root = 1` **on the database and deleting** `dummy_admin` **before proceeding with the creation of any Scenario.**

#### Setup HTTPS (optional)
By default COCTEAU works on HTTPS and needs a certificate. You can run COCTEAU in plain HTTP by modifying the `application.properties`.
Please note that you may also need to change the `SecurityConfig` class accordingly.

For the purpose of this project, [certbot](https://certbot.eff.org) was used.
By default certbot generated certificates and private keys in [PEM](https://datatracker.ietf.org/doc/html/rfc1421) format. Spring Boot applications however support PKCS12 and JKS formats. One way to convert the certificate generated by certbot is to use OpenSSL:

~~~bash
openssl pkcs12 -export -in fullchain.pem \
                -inkey privkey.pem \
                -out <keystore_name>.p12 \
                -name <alias> \
                -CAfile chain.pem \
                -caname root
~~~

Remember to change `<keystore_name>` and `<alias>`!

Once the certificate has been converted update the `application.properties` accordingly:

~~~properties
server.port = 443
security.require-ssl = true
server.ssl.key-store = <path to converted keystore>
server.ssl.key-store-password = <your password>
server.ssl.keyStoreType = PKCS12
server.ssl.keyAlias = <alias>
~~~

## Documentation
The documentation for the codebase can be found in the `doc` folder and accessible by viewing the `index.html` file.
