package Main;

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

import Log.CreateLog;

/***
 * B) Develop a small application, preferably in Java or Groovy that
 * communicates with Github API and does the following: a) It’s taking input
 * through the command line CLI (1st param = Se-arch string, 2nd param = release
 * tag) b) Search Github for repositories that match the given search string
 * “TestNG” c) Sort the results by the number of stars in a descending order and
 * print the top result name and stars d) For the top result repository read the
 * latest release tag and verify if the 2nd param given by CLI refer to an older
 * or newer release tag (e.g. 6.13.1) and print it in a message e) Add tests for
 * your project to provide good code coverage
 * 
 * @author Automation
 *
 */

public class Draf {

	public static CreateLog log = new CreateLog();

	static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36";
	static final String REQUESTURL = "https://api.github.com/search/repositories";

	public static void main(String[] args) throws IOException {
		
		ArrayList<String> parameters = setParameters(args[0], args[1] );
		String newRequestUrl = REQUESTURL + "?q=" + parameters.get(0) + "&sort=stars&order=desc";
		System.out.println(REQUESTURL);
		log.createLog("RESPONSE: " + newRequestUrl);
		String result = getHttpResponse(newRequestUrl, USER_AGENT);
		ArrayList<String> nameAndStarResult = getKeys(result, "ShowFirstResultName&Star");
		log.createLog("RESPONSE: " + nameAndStarResult.get(0));
		log.createLog("RESPONSE: " + nameAndStarResult.get(1));
		String lastReleaseTagResult = showLatestReleaseTag(result, USER_AGENT);
		log.createLog("RESPONSE SECOND URL: " + lastReleaseTagResult);
		System.out.println(lastReleaseTagResult);
		verifyLastedRelease(lastReleaseTagResult, parameters.get(1));

	}
	
   /***
    * Set a parameters from main arguments
    * @param args0
    * @param args1
    * @return
    * @throws IOException
    */
   private static  ArrayList<String> setParameters(String args0, String args1 ) throws IOException {
		
	   ArrayList<String> parameterArray = new ArrayList<String>();; 
	    parameterArray.add(args0);
		System.out.println("The first parameter was:" + parameterArray.get(0));
		log.createLog("first parameter:" + parameterArray.get(0));
		 parameterArray.add(args1);
		System.out.println("The second parameter:" + parameterArray.get(1));
		log.createLog("second parameter:" + parameterArray.get(1));
		 return parameterArray;
	}

	/***
	 * Verify if the 2nd param given by CLI refer to an older or newer release tag
	 * (e.g. 6.13.1) and print it in a message
	 * 
	 * @param lastReleaseTag
	 * @param parameter      : 2nd param given by CLI
	 * @return Boolean
	 */

	private static Boolean verifyLastedRelease(String lastReleaseTag, String parameter) {

		Boolean result = false;
		if (lastReleaseTag.equals(parameter)) {
			result = true;
			System.out.println("Lastest release tag is: " + lastReleaseTag);
		} else {
			System.out.println("NO MATCH ---->" + "Lastest release tag is: " + lastReleaseTag + "  and it was entered: "
					+ parameter);
		}
		return result;
	}

	/***
	 * For the top result repository read the latest release tag and verify if the
	 * 2nd param given by CLI refer to an older or newer release tag (e.g. 6.13.1)
	 * and print it in a message
	 * 
	 * @param result
	 * @param USER_AGENT
	 * @return
	 */

	private static String showLatestReleaseTag(String result, String USER_AGENT) {

		ArrayList<String> showLastTag = null;
		String requestURL = createNewUrl(result);
		String resultTag = getHttpResponse(requestURL, USER_AGENT);
		showLastTag = getKeys(resultTag, "ReturnLatestReleaseTag");
		return showLastTag.get(0);
	}

	/***
	 * It creates the new URL to search the release tag
	 * 
	 * @param result
	 * @return String
	 */
	private static String createNewUrl(String result) {

		ArrayList<String> url = null;
		url = getKeys(result, "ReturnLastURL");
		return url.get(0);
	}

	/***
	 * The HTTP client send a request with a URL and receives a response with the available repositories.

	 * @param requestURL
	 * @param USER_AGENT
	 * @return String
	 */
	private static String getHttpResponse(String requestURL, String USER_AGENT) {

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

	private static String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
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

	/***
	 * Shows an array of String depending on the chosen option: 
	 * ShowBySort: Shows the stars in descending order of all the repositories found
	 * ShowFirstResultName&Star : Shows the key name and key star of the repository array that
       was previously sorted from highest to lowest
	 * ReturnLastURL : Shows the url of the repository array that was previously sorted from largest to smallest, 
	 * to find the release tag
	 * ReturnLatestReleaseTag: Shows the last release tag
	 * @param result
	 * @param option
	 * @return String[]
	 */
	private static ArrayList<String> getKeys(String result, String option) {

		String[] parts = result.split(",");
		int t = 0;
		ArrayList<String> subpart = new ArrayList<String>();
		switch (option) {
		case "ShowBySort":
			for (int i = 0; i < parts.length; i++) {
				if (parts[i].contains("\"stargazers_count\":")) {
					subpart.add(parts[i]);
					System.out.println(parts[i]);
				}
			}

			break;
		case "ShowFirstResultName&Star":
			for (int i = 0; i < parts.length; i++) {
				boolean key1 = parts[i].contains("\"name\":");
				boolean key2 = parts[i].contains("\"stargazers_count\":");
				if (key1 || key2) {
					subpart.add(parts[i]);
					System.out.println(parts[i]);
				}
				t = subpart.size();
				if (t >= 2)
					break;
			}

			break;
		case "ReturnLastURL":
			for (int i = 0; i < parts.length; i++) {
				boolean key3 = parts[i].contains("\"tags_url\":");
				if (key3) {
					subpart.add(urlParsed(parts[i]));
					System.out.println(parts[i]);
				}
				t = subpart.size();
				if (t >= 1)
					break;
			}

			break;
		case "ReturnLatestReleaseTag":
			for (int i = 0; i < parts.length; i++) {
				boolean key1 = parts[i].contains("\"name\":");
				if (key1) {
					subpart.add(nameTagParsed(parts[i]));
					System.out.println(parts[i]);
				}
				t = subpart.size();
				if (t >= 1)
					break;
			}

			break;
		default:

		}

		return subpart;

	}

	/***
	 * Search inside the string for the 'url' and return it
	 * 
	 * @param url
	 * @return String
	 */
	private static String urlParsed(String url) {

		String result = null;
		String subresult = url.substring(20);
		result = subresult.substring(0, subresult.length() - 1);
		return result;
	}

	/***
	 * Search inside the string for the 'name' and return it
	 * @param url
	 * @return String
	 */
	private static String nameTagParsed(String name) {

		String result = null;
		String subresult = name.substring(13);
		result = subresult.substring(0, subresult.length() - 1);
		return result;
	}
}
