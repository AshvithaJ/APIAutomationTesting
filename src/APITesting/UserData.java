package APITesting;

import java.io.IOException;
import java.text.ParseException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.log4testng.Logger;


public class UserData {

	    private String responseBody;
	    public String responseBodyPOST;
	    final static Logger logger = Logger.getLogger(UserData.class);
	    //RESTTemplate Object
	    private RestTemplate restTemplate;
	     
	    //User ID 
	    private String userId;
	    // Create Response Entity - Stores HTTPStatus Code, Response Body, etc
	    private ResponseEntity<String> response;
	    @BeforeTest
	    public void beforeTest() throws IOException, ParseException {
	        logger.info("Setting up prerequisite for test execution");
	        logger.info("Creating RestTemplate object before tests");
	        this.restTemplate = new RestTemplate(); 
	    }
	     
	    /**
	     * Test Method to add user using HTTP POST request 
	     * 
	     * Verifies POST action Status Code 
	     *  
	     * @throws IOException
	     * @throws ParseException
	     */
	    @Test
	    public void addUser() throws IOException, ParseException {
	        String addURI = "https://reqres.in/api/users/create";
	        HttpHeaders headers = new HttpHeaders();        
	        headers.add("Accept", "application/json");
	        headers.add("Content-Type", "application/json");
	          
	        logger.info("Add URL :"+addURI);
	        String jsonBody = "{\"email\":\"jane.ashton@reqres.in\",\"first_name\":\"Jane\",\"last_name\":\"Ashton\",\"avatar\":\"https://reqres.in/img/faces/7-image.jpg\"}";
	        System.out.println("\n\n" + jsonBody);
	        HttpEntity<String> entity = new HttpEntity<String>(jsonBody, headers);
	         
	        //POST Method to Add New User
	        response = this.restTemplate.postForEntity(addURI, entity, String.class);
	        responseBodyPOST = response.getBody();
	        // Write response to file
	        responseBody = response.getBody().toString();
	        System.out.println("responseBody ---------;" + responseBody);
	        // Get ID from the Response object
	        userId = getUserIdFromResponse(responseBody);
	        System.out.println("userId is :" + userId);
	        // Check if the added User is present in the response body.
	        Assert.assertTrue(responseBody.contains(userId));
	        
	        // Check if the status code is 201
	        Assert.assertEquals(response.getStatusCode(), HttpStatus.CREATED);
	        logger.info("User is Added successfully userId:"+userId);
	    }
	     
	    /**
	     * 
	     * @param json
	     * @return
	     */
	    public static String getUserIdFromResponse(String json) {
	        JSONParser parser = new JSONParser();
	        JSONObject jsonResponseObject = new JSONObject();
	        Object obj = new Object();
	        try {
	            obj = parser.parse(json);
	        } catch (org.json.simple.parser.ParseException e) {
	            e.printStackTrace();
	        }
	        jsonResponseObject = (JSONObject) obj;
	        String id = jsonResponseObject.get("id").toString();
	        return id;
	    }
	    
	    /**
	     * 
	     * @throws IOException
	     * @throws ParseException
	     */
	      @Test() 
	      void getUser() throws IOException, ParseException {     
	          String getURI = "https://reqres.in/api/users/2";
	          logger.info("Get URL :"+getURI);
	           
	          
	          //GET Method to Get existing User
	          response = restTemplate.getForEntity(getURI,String.class);
	           
	          // Write response to file
	          responseBody = response.getBody().toString();
	           
	          //Suppressing for log diffs
	          System.out.println("GET Response Body :"+responseBody);

	          // Check if the status code is 200
	          Assert.assertEquals(response.getStatusCode(), HttpStatus.OK);
	           
	          logger.info("User is retrieved successfully userId:"+userId);
	       
	      }
}
