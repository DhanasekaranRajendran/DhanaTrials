##MyRetail REST API

MyRetail RESTful service provides the client application ability to:

    1. Retrieve Product and Price information by Product Id

    2. Send request to modify the price information in the database
    
    3. Delete Product info by Product Id


##Get Product Information:

###Input: 
The client application does a GET request at the path "myretail/products/{id}" for a product 

###Internal Working: 
When the API receives the request, it sends a request to "redsky.target.com" and retrieves the 
product information. The Product Name is extracted from redSky APi response and the price information is retrieved
from Database. The product name is updated in the Pricing information and sent back to user as response.

###Output: 
For a product with product id '13860428', the sample JSON output is as shown below

{"id":13860428,"name":"The Big Lebowski (Blu-ray) (Widescreen)","current_price":{"value": 13.49,"currency_code":"USD"}}

###Errors/Validations: 
Appropriate error messages are provided after validating the data. More information is available in 
the below sections. The client application can use the message in the response to display the same to the user appropriately.


##Update Product Price in the DB:

###Input: 
The user/client application can do a PUT request with input similar to the response received in GET and should be able
to modify the price in the datastore. The request is done at the same path "/products/{id}"

####Sample Input: 
JSON Body - {"id":13860428,"name":"The Big Lebowski (Blu-ray) (Widescreen)","current_price":{"value": 15.67,"currency_code":"USD"}}

###Internal Working: 
When the API receives PUT request, it does some validations to see if the product is available. If it is, 
it ensures that the id in the URL and the JSON body is similar and if that looks same, the price for the product is modified 
in the data store.

###Output: 
Success message is returned if the price modification is done.

###Errors/Validations: 
Appropriate error messages are provided after validating the data. More information is available in 
the below sections. The client application can use the message in the response to display the same to the user appropriately.

##Technologies Used
-----------------

1. Spring Boot - https://projects.spring.io/spring-boot/
2. MongoDB - https://www.mongodb.com/
3. Swagger - http://swagger.io/
4. Gradle - https://gradle.org
5. Spock - http://spockframework.org/

##Instructions to Setup
---------------------
1. Install MongoDB in your system - https://docs.mongodb.com/manual/installation/
2. Install Gradle - https://gradle.org/gradle-download/
3. Run MongoDB - Run 'mongod.exe' in order to start Mongodb 
4. Clone the code from git repository - https://github.com/Gayathri16/MyRetail.git
5. Make sure you are in the MyRetail directory
6. Run the following command to start
`./gradlew bootRun`
7. Open browser and visit Swagger.
`http://localhost:8080/swagger-ui.html`
8. Swagger documentation explains the expected request and response for GET and PUT requests.

##Testing

The testcases are present in the folder 'src\test\groovy\'. 

The test cases can be executed by running the command './gradlew test'

##Swagger UI:

Swagger displays the following information for an API method by default. Please refer to images 'Default_GetProductInfo.png'
and 'Default_PutRequest.png' to see how the default information for an API method looks like in Swagger.

  1. Type of request(GET/PUT/DELETE) and the path of request
  2. Status and format of the response
  3. Response Content Type
  4. Parameters list
  5. Possible Failure Responses with HTTP code

The user can modify the values in the fields provided and can do "Try it out!" at the bottom. Please refer to the images 
'Sample_GET_Success.png' and 'Sample_PUT_Success.png' to see what a sample GET and PUT requests look like.

More information about the API methods and the responses is provided below.

##API Requests and Responses

## PUT Request:

Following PUT request will store information of productID:53536820 in NOSQL database

###Request:

`curl -X PUT --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{ "id": "53536820", "productName": "Apple iPad 9.7-inch Wi-Fi Only (2018 Model, 6th Generation)", "current_price": { "value": 499.99, "currency_code": "USD" }} \ 
  ' 'http://localhost:8080/products/53536820'`
  
###Response:

    {
        "id": "53536820",
        "productName": "Apple iPad 9.7-inch Wi-Fi Only (2018 Model, 6th Generation)",
        "current_price": {
            "value": 499.99,
            "currency_code": "USD"
        }
    }
 
* When productId in request url and body is different it will return 400 Bad Request

###Request:

`curl -X PUT --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{"id":1011,"name":"The Big Lebowski (Blu-ray) (Widescreen)","current_price":{"value": 13.49,"currency_code":"USD"}} \ 
  ' 'http://localhost:8080/myretail/products/53536820'`
  
###Response:

    {
        "error": "Product id doesn't match with body and path variable"
    }
    
* When a user tries to a new ProductID which is not present in DB, and if its a valid one from Redsky, then its inserted into DB.

###Request:

`curl -X PUT --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{ "id": "54252241", "productName": "Apple iPad 9.7-inch Wi-Fi Only (2018 Model, 6th Generation)", "current_price": { "value": 499.99, "currency_code": "USD" }} ' 'http://localhost:8080/myretail/products/53536820'`
  
###Response:

    {
        "error": "Product id doesn't match with body and path variable"
    }

 
## GET Request:
 
### Request:
 
 `curl -X GET --header 'Accept: application/json' 'http://localhost:8080/myretail/products/53536820'`
 
 ### Response:
 
    {
        "id": "53536820",
        "productName": "Apple iPad 9.7-inch Wi-Fi Only (2018 Model, 6th Generation)",
        "current_price": {
            "value": 429.99,
            "currency_code": "USD"
            }
    }
 
 * When you give a productID that doesn't exist you will get 404 Product not found.
 
 ### Request:
 
 `curl -X GET --header 'Accept: application/json' 'http://localhost:8080/myretail/products/1011'`
 
 ### Response:
 
    {
        "error": "Product not found in RedSky Service"
    } 
