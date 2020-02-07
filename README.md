# Portfolio management simulator
### This project is a simulation of a stock portfolio management application.

You can look up stock information by searching with the stock symbol, buy shares of the stock, or sell existing shares from lots purchased earlier. 

This project is still a work in progress. 

![ui thumbnail](/images/portfolio_diagram.png)

### The application consists of:
* RESTful application service that is written in Java and uses Spring Boot. 
* User interface written in Javascript/React
* Querying stock information from WorldTradingData.com

### The project utilizes:
* Maven for dependency management and building an executable .jar
* JUnit test cases written with Mockito
* Jacoco to analyze test coverage

### Building the project:
* `mvn clean package` # build Spring Boot RESTful service 
* `./scripts/buildReact.sh` # build production version of React UI

### Running the application:
* `export HEROKU_DATABASE_URL=<heroku-postgresql-connection-url>`
* `export API_TOKEN=<www.worldtradingdata.com-api-token>`
* `java -jar target/portfolio.management.app-0.0.1-SNAPSHOT.jar`

The project is so far tested in Chrome and deployed on the [Heroku cloud](https://portfolio-management-app.herokuapp.com/)

### TODO's
* Add input validation and improve error handling 
* Implement authentication and user accounts (currently there are no accounts)

------------------------------------------------------------



List of sample REST endpoint URLs for testing on localhost:
 * http://localhost:9090/api/portfolio/ownedStocksCreate
 * http://localhost:9090/api/portfolio/findAllStocks
 * http://localhost:9090/api/portfolio/findAllLots
 * http://localhost:9090/api/portfolio/findLotsByOwnedStockId/1
 * http://localhost:9090/api/portfolio/findLotsByOwnedStockSymbol/AAPL
 * http://localhost:9090/api/portfolio/findAllOwnedStocksWithLots
  * http://localhost:9090/api/portfolio/quote/GSK
