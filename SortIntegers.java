// Will Dahl
// 001273655
// ICSI 402
// Febuary 8th, 2018

package csi403;

// Import required java libraries
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.json.*;


// Extend HttpServlet class
public class SortIntegers extends HttpServlet {

  // Standard servlet method 
  public void init() throws ServletException
  {
      // Do any required initialization here - likely none
  }

  // Standard servlet method - we will handle a POST operation
  public void doPost(HttpServletRequest request,
                    HttpServletResponse response)
            throws ServletException, IOException
  {
      doService(request, response); 
  }

  // Standard servlet method - we will not respond to GET
  public void doGet(HttpServletRequest request,
                    HttpServletResponse response)
            throws ServletException, IOException
  {
      // Set response content type and return an error message
      response.setContentType("application/json");
      PrintWriter out = response.getWriter();
      out.println("{ 'message' : 'Use POST!'}");
  }

  private void doService(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    // Set response content type to be JSON
    response.setContentType("application/json");
    // Send back the response JSON message
    PrintWriter out = response.getWriter();
    try{
  	// Get received JSON data from HTTP request
      BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
      String jsonStr = "";
      if(br != null){
          jsonStr = br.readLine();
      }
      // Create JsonReader object
      StringReader strReader = new StringReader(jsonStr);
      JsonReader reader = Json.createReader(strReader);
      
      // Get the singular JSON object (name:value pair) in this message.    
      JsonObject obj = reader.readObject();
      // From the object get the array named "inList"
      JsonArray inArray = obj.getJsonArray("inList");
      if(inArray == null){
         out.println("{ \"message\":\"Malformed JSON\" }");       
      }
      int[] numbers = new int[inArray.size()];
      for(int i = 0; i < inArray.size(); i++)
      {
        numbers[i] = inArray.getInt(i);
      }

      // Sorts the data in the list
      JsonArrayBuilder outArrayBuilder = Json.createArrayBuilder();
      int key;
      int j;
      long start = System.currentTimeMillis();
      for(int i = 1; i < numbers.length; i++)
      {
      	key = numbers[i];
      	j = i - 1;
      	while(j >= 0 && numbers[j] > key)
      	{
      		numbers[j + 1] = numbers[j];
      		j = j - 1;
      	}

      	numbers[j + 1] = key;
      }
      long time = System.currentTimeMillis() - start;

      for(int i = 0; i < numbers.length; i++)
      {
        outArrayBuilder.add(numbers[i]);
      }

      out.println("{ \"outList\" : " + outArrayBuilder.build().toString() + " }"); 
      out.println("\"algorithm\": \"insertion-sort\"");
      out.println("\"timeMS\": " + time);
    }
    catch(Exception e){
        out.println("{ \"message\":\"Malformed JSON\" }");
    }
  }

  // Standard Servlet method
  public void destroy()
  {
      // Do any required tear-down here, likely nothing.
  }
}
