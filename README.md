# **X-Men - Find a Mutant**
# AWS API GateWay(Proxy) + AWS Lambda Runtime + DynamoDB + Java11 + Micronaut - Proxy 
This is an exercise proposed to compute a DNA that verifies if is a human or a mutant based on these characteristics:
* It's a mutant if find 2 or more series of 4 characteres based on letters 'A','C'.'T','G' in horizontal, vertical and oblique otherwise is a human

Not Mutant:

| - | - | - | - | - | - |
| --- | --- | --- | --- | --- | --- |
| A | T | G | C | G | A |
| C | A | G | T | G | C |
| T | T | A | T | T | T |
| A | G | A | C | G | G |
| G | C | G | T | C | A |
| T | C | A | C | T | G |
| - | - | - | - | - | - |

Mutant:

| - | - | - | - | - | - |
| --- | --- | --- | --- | --- | --- |
| **A** | T | G | C | **G** | A |
| C | **A** | G | T | **G** | C |
| T | T | **A** | T | **G** | T |
| A | G | A | **A** | **G** | G |
| **C** | **C** | **C** | **C** | T | A |
| T | C | A | C | T | G |
| - | - | - | - | - | - |

Dependencies:
* To run this Lambda Locally you have to install [SAM](https://github.com/awslabs/aws-sam-cli/) and can use the file `sam-local.sh`
* [Docker](https://www.docker.com/)
* AWS Account with privileges IAM, Lambda, DynamoDB, API Gateway

Create a table with billing-mode PAY_PER_REQUEST because we don't know the income traffic to the lambda
```bash
$ aws dynamodb create-table --table-name stats --attribute-definitions AttributeName=id,AttributeType=S --key-schema AttributeName=id,KeyType=HASH --billing-mode PAY_PER_REQUEST
```

The `Dockerfile` contains the build to build the image it can be built with:

```bash
#!/bin/sh
./gradlew clean build
docker build . -t mutant
sam local start-api -t sam.yaml -p 3000
```
The following Endpoints exposed:
* Host: http://localhost:3000
* URI: /mutant
    * Method: POST
    * Body Format: application/json 
    * Body Request Mutant:
        ```json
        { 
            "dna":["ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"] 
        }
        ```
    * Body Human:
        ```json
        { 
            "dna":["ATGCGA","CAGTGC","TTATTT","AGACGG","GCGTCA","TCACTG"] 
        }
        ```
* URI: /stats
    * Method: GET
    * Body Response Format: application/json
        ```json
      {
          "count_human_dna": 31,
          "count_mutant_dna": 10,
          "ratio": 0.3226
      }
        ```
    
To test your local application use:
```bash
curl -i -H "Accept: application/json" -H "Content-Type: application/json" -X GET http://localhost:3000/stats
```
Response:
```bashHTTP/2 200 
date: Fri, 26 Jun 2020 02:15:29 GMT
content-type: application/json
content-length: 59
x-amzn-requestid: 5d6ff20b-8946-4272-a9a2-10724453784b
x-amz-apigw-id: OtsKOFRBIAMFbsA=
x-amzn-trace-id: Root=1-5ef55a41-9546ca252491771d22425560;Sampled=0

{"count_human_dna":32,"count_mutant_dna":11,"ratio":0.3438}%
``` 

Steps to Deploy your .jar file:
1. Create a Rol and grant access to: 
    * Lambda(Execution Policie)
    * DynamoDB(Read and Put Policies)
2. Create a Lambda Function with Java11:
    * Handler: mutant.StreamLambdaHandler::handleRequest
    * Memory: 512MB
    * Timeout: 15s
3. Create a Proxy ApiRest in API GateWay:
    * Create a Resource and configure like proxy
    * Fill the Lambda text area with the name of your created Lambda
    * Test your Lambda: 
        * Method: GET
        * {proxy}: /stats
        * Press Test
            ```json
             {
                "count_human_dna": 31,
                "count_mutant_dna": 10,
                "ratio": 0.3226
             }
           ```
To verify my Lambda, you can go to:
* https://sym0zsedw2.execute-api.us-east-1.amazonaws.com/Prod/mutant
* https://sym0zsedw2.execute-api.us-east-1.amazonaws.com/Prod/stats
