## CMPE 273- Assignment 1

Building a Web Voting application similar to the SMS Voting.

Application support JSON data format for the API. The API version for this assignment is “v1”, and the application server must be running on the default port 8080.

There is no data persistent required for the wallet, but to store the data in an in-memory data structure like HashMap/ArrayList.

##  APIs

These are the set of APIs you need to build for the system.
 
* Create Moderator

    * Resource: /moderators
    * Description: Add a moderator to the system.
    * Request: POST /moderators (with the following payload in the request body)
		* HTTP Headers: Content-type: application/json
                {
                "name": "John Smith",
 				"email": "John.Smith@Gmail.com",
 				"password": "secret"
	            }
        * Response: HTTP Code: 201
                {
                "id" : "123456",
                "name": "John Smith",
                "email": "John.Smith@Gmail.com",
                "password": "secret",
                "created_at" : "2014-09-16T13:28:06.419Z"
                }
* View Moderator
   * Resource: /moderators/{moderator_id}
   * Description: View a moderator resource
   *  Request:
        * GET /moderators/123456
        * Accept: application/json
        * Response: HTTP Code: 200
                {
                "id" : "123456",
                "name": "John Smith",
                "email": "John.Smith@Gmail.com",
                "password": "secret",
                "created_at" : "2014-09-16T13:28:06.419Z"
                 }

* Update Moderator

    * Resource: /moderators/{moderator_id}
    * Description: Update an existing moderator information.
    * Request: 
        * PUT /moderators/123456 (with the following payload in the                       request body)
        * HTTP Headers:
        * Content-type: application/json
                    {"email": "John.Smith2@Gmail.com",
                    "password": "newsecret"}
            
    * Response: HTTP Code: 200
                   { "id" : "123456",
                    "name": "John Smith",
                    "email": "John.Smith2@Gmail.com",
                    "password": "newsecret",
                    "created_at" : "2014-09-16T13:28:06.419Z" }
            
* Create a Poll

    * Resource: /moderators/{moderator_id}/polls
    * Description: Create a new poll
    * Request: 
        * POST /moderators/12345/polls (with the following payload in the                   request body)
        * HTTP Headers: Content-type: application/json
         
                    {
                    "question": "What type of smartphone do you have?",
                    "started_at": "2015-02-23T13:00:00.000Z",
                    "expired_at" : "2015-02-24T13:00:00.000Z",
                    "choice": [ "Android", "iPhone" ]
                    }
    * Response:
        * HTTP Code: 201
                {
                "id" : "1ADC2FZ",   
                "question": "What type of smartphone do you have?",
                "started_at": "2015-02-23T13:00:00.000Z",
                "expired_at" : "2015-02-24T13:00:00.000Z",
                "choice": [ "Android", "iPhone" ]
                }
           
* View a Poll Without Result
    * Resource: /polls/{poll_id}
    * Description: View a poll.
    * Request: GET /polls/1ADC2FZ
        * HTTP Headers: Content-type: application/json
                {
                "id" : "1ADC2FZ",
                "question": "What type of smartphone do you have?",
                "started_at": "2015-02-23T13:00:00.000Z",
                "expired_at" : "2015-02-24T13:00:00.000Z",
                "choice": [ "Android", "iPhone" ]
                }

* View a Poll With Result

    * Resource: /moderators/{moderator_id}/polls/{poll_id}
    * Description: View a poll with current result.
    * Request:  GET /moderators/12345/polls/1ADC2FZ
            * HTTP Headers:Content-type: application/json
                {
                "id" : "1ADC2FZ",
                "question": "What type of smartphone do you have?",
                "started_at": "2015-02-23T13:00:00.000Z",
                "expired_at" : "2015-02-24T13:00:00.000Z",
                "choice": [ "Android", "iPhone" ],
                "results": [ 500, 600 ]
                }

* List All Polls
    * Resource: /moderators/{moderator_id}/polls
    * Description: List all polls created by the given moderator.
    * Request: GET /moderators/12345/polls
            * HTTP Headers:Accept-type: application/json
            * Response: HTTP Code: 200
                [
                    {
                    "id" : "1ADC2FZ",
                    "question": "What type of smartphone do you have?",
                    "started_at": "2015-02-23T13:00:00.000Z",
                    "expired_at" : "2015-02-24T13:00:00.000Z",
                    "choice": [ "Android", "iPhone" ],
                    "results": [ 500, 600 ]
                    },
                    {
                    "id" : "2BZE91C",
                    "question": "Are you a truant?",
                    "started_at": "2015-02-23T13:00:00.000Z",
                    "expired_at" : "2015-02-24T13:00:00.000Z",
                    "choice": [ "Yes", "No" ],
                    "results": [ 30, 70 ]
                    }
                ]
            
*  Delete a Poll

    * Resource: /moderators/{moderator_id}/polls/{poll_id}
    * Description: Delete a poll
    * Request: DELETE /moderators/12345/polls/2BZE91C
    * Response: HTTP Code: 204

* Vote a Poll

    * Resource: /polls/{poll_id}?choice={choice_index}
    * Description: Vote a poll
    * Request: 
            * User's choice for the poll was "Yes" which is index 0.
            * PUT /polls/2BZE91C?choice=0     
    * Response:HTTP Code: 204

* How to test APIs?

    * You can use either curl command line tool. or GUI tools like Postman or        Advanced REST client Chrome plugin.
