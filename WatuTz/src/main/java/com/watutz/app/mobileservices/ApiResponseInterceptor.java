package com.watutz.app.mobileservices;

import com.watutz.app.GeneralCommands;
import com.watutz.app.SharedObjects;

import okhttp3.Interceptor;
import okhttp3.Response;
import java.io.IOException;

public class ApiResponseInterceptor implements Interceptor {
   @Override
   public Response intercept(Chain chain) throws IOException {
      // Get the request
      okhttp3.Request request = chain.request();

      // Perform the request, get the response
      Response response = chain.proceed(request);

      // Log the response
      logResponse(response);

      // Return the response
      return response;
   }

   private void logResponse(Response response) throws IOException {
      // Extract relevant information from the response
      try{

         String requestUrl = response.request().url().toString();
         String responseBody = response.body().string();
         String requestStatus=""+response.code();

         // Log the information as needed
         System.out.println("Boz-Request URL: " + requestUrl);
//      System.out.println("Boz-Response Body: " + responseBody);
//      System.out.println("Boz-Request BOdy: " + requestBody);

         GeneralCommands.logEvent(SharedObjects.context, "api_request",requestUrl+":"+requestStatus);

         // Re-create the response body for further use
         okhttp3.ResponseBody newResponseBody = okhttp3.ResponseBody.create(
                 response.body().contentType(), responseBody);

         // Build a new response with the modified body
         Response newResponse = response.newBuilder().body(newResponseBody).build();

         // Print the modified response
         System.out.println("Modified Response: " + newResponse.toString());

      }catch(Exception e){}

   }
}
