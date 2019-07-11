package example;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;
import java.util.Iterator;
import java.util.Map;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Map;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;





public class Slack_Notification implements RequestStreamHandler
{

  JSONParser parser = new JSONParser();

   @Override
   public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException
   {
      LambdaLogger logger = context.getLogger();
      logger.log("Loading Java Lambda handler of ProxyWithStream");

      BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
      StringBuilder stringBuilder = new StringBuilder();
	    String line = null;

      while ((line = reader.readLine()) != null) {
      			stringBuilder.append(line);
      }
      try{
            JSONObject event = (JSONObject)parser.parse(stringBuilder.toString());
            JSONArray arr = (JSONArray)event.get("Records");

            JSONObject s = (JSONObject)arr.get(0);
            JSONObject SNS = (JSONObject)s.get("Sns");
            String ARNS = (String)SNS.get("TopicArn");

            String message = (String)SNS.get("Message");
            JSONObject messageObj = (JSONObject)parser.parse(message);

            String reason = (String)messageObj.get("NewStateReason");
            String Region = (String)messageObj.get("Region");
            String UnUrl = (String)SNS.get("UnsubscribeUrl");
            String text = "ALARM!!!" + "\n" + "ALARM for ARNS(Amazon Resource Number): " + ARNS + "  " + "Region: " + Region + "\n" +
             "Reason for ALARM: " + reason + "\n" + "Action:> Unsubscribe url: " + UnUrl;
            JSONObject json = new JSONObject();
         		json.put("text", text);

         		CloseableHttpClient httpClient = HttpClientBuilder.create().build();

         		try {
             		HttpPost request = new HttpPost("https://hooks.slack.com/services/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");
             		StringEntity params = new StringEntity(json.toString());
             		request.addHeader("content-type", "application/json");
             		request.setEntity(params);
             		httpClient.execute(request);
         		}
            catch (Exception ex) {
                ex.printStackTrace();

         		}
            finally {
             		httpClient.close();
         		}

      }
      catch(Exception ex){
        ex.printStackTrace();

      }


     }


}
