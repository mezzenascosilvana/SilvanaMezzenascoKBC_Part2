package GitHubHandling;

import static org.testng.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.testng.annotations.Test;

import Log.CreateLog;

public class GitHubHandlerRequest {

	CreateLog log = new CreateLog();

	/***
	 * Set a parameters from main arguments
	 * 
	 * @param args0
	 * @param args1
	 * @return
	 * @throws IOException
	 */
	@Test
	public ArrayList<String> setParameters(String args0, String args1) throws IOException {

		ArrayList<String> parameterArray = new ArrayList<String>();
		parameterArray.add(args0);
		System.out.println("The first parameter was:" + parameterArray.get(0));
		log.createLog("first parameter:" + parameterArray.get(0));
		parameterArray.add(args1);
		System.out.println("The second parameter:" + parameterArray.get(1));
		log.createLog("second parameter:" + parameterArray.get(1));
		assertTrue(parameterArray.size()>=1); // validate the array has values
		return parameterArray;
	}

	/***
	 * The HTTP client send a request with a URL and receives a response with the available repositories.
	 * @param requestURL
	 * @param USER_AGENT
	 * @return String
	 */
	public String getHttpResponse(String requestURL, String USER_AGENT) {

		String result = null;
		// Instantiating HttpClient
		HttpClient client = HttpClientBuilder.create().build();
		// Creating a Method
		HttpGet request = new HttpGet(requestURL);
		request.addHeader("User-Agent", USER_AGENT);
		HttpResponse response;
        
		try {
			System.out.println("Start.....");
			log.createLog("Start.....");
			// Execute the Method
			response = client.execute(request);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				// A Simple JSON Response Read
				InputStream instream = entity.getContent();
				// Read the Response
				result = convertStreamToString(instream);
				// now you have the string representation of the HTML request
				System.out.println("RESPONSE: " + result);
				instream.close();
			}
			// Headers
			org.apache.http.Header[] headers = response.getAllHeaders();
			for (int i = 0; i < headers.length; i++) {
				System.out.println(headers[i]);
			}
		} catch (ClientProtocolException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		return result;
	}

	/***
	 * It converts an InputStream into a stream. The string only contains the following keys:
     * name,stargazers_count,tags_url
     *
	 * @param is
	 * @return String
	 */
	@Test
	private static String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		// this part only add into the array some keys of all the repositories
		try {
			while ((line = reader.readLine()) != null) {
				boolean key1 = line.contains("\"name\":");
				boolean key2 = line.contains("\"stargazers_count\":");
				boolean key3 = line.contains("\"tags_url\":");
				if (key1 || key2 || key3)
					sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return sb.toString();
	}

}
