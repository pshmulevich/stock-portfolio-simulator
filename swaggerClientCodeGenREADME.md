# Generating axios REST API client code using OpenAPI/Swagger code generator:

Pre-requisite: a Swagger-enabled REST service (in this case: https://portfolio-management-app.herokuapp.com)

1) Download OpenAPI Generator jar:
```
wget https://repo1.maven.org/maven2/org/openapitools/openapi-generator-cli/4.2.3/openapi-generator-cli-4.2.3.jar \
    -O openapi-generator-cli.jar
```
2) Run the following command:
```
java -jar .\openapi-generator-cli.jar generate \
    -i https://portfolio-management-app.herokuapp.com/v2/api-docs \
    -g typescript-axios \
    -o typescript-axios-client
```
This will generate the client-side code for your application in `typescript-axios-client` folder

3) Transfer the generated .ts files to your UI IDE (in this case, codesandbox.io)

4) Import the generated API into your JavaScript UI code and invoke it as appropriate

5) Example of useage with React: 
* Soucecode: https://codesandbox.io/s/portfolioappswagger-1bvzw (see `App.js` for more details)
* UI View: https://1bvzw.csb.app/

