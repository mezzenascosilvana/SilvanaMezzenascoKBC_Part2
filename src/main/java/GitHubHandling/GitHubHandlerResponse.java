package GitHubHandling;

import static org.testng.Assert.assertTrue;

import java.util.ArrayList;

import org.testng.annotations.Test;

public class GitHubHandlerResponse {
	
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
	@Test
	public  ArrayList<String> getKeys(String result, String option) {

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
		assertTrue(result !=null);
		return subpart;

	}  

	/***
	 * Search inside the string for the 'url' and return it
	 * 
	 * @param url
	 * @return String
	 */
	@Test
	private String urlParsed(String url) {

		String result = null;
		String subresult = url.substring(20);
		result = subresult.substring(0, subresult.length() - 1);
		assertTrue(result !=null);
		return result;
	}

	/***
	 * Search inside the string for the 'name' and return it
	 * 
	 * @param url
	 * @return String
	 */
	@Test   	
	private  String nameTagParsed(String name) {

		String result = null;
		String subresult = name.substring(13);
		result = subresult.substring(0, subresult.length() - 1);
		assertTrue(result !=null);
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
	@Test
	public String showLatestReleaseTag(String result, String USER_AGENT) {
        
		GitHubHandlerRequest obj1 =new GitHubHandlerRequest();
		ArrayList<String> showLastTag = null;
		String requestURL = createNewUrl(result);
		String resultTag =obj1.getHttpResponse(requestURL, USER_AGENT);
		showLastTag = getKeys(resultTag, "ReturnLatestReleaseTag");
		return showLastTag.get(0);
	}

	/***
	 * It creates the new URL to search the release tag
	 * 
	 * @param result
	 * @return String
	 */
	private  String createNewUrl(String result) {

		ArrayList<String> url = null;
		url = getKeys(result, "ReturnLastURL");
		return url.get(0);
	}
	
	/***
	 * Verify if the 2nd param given by CLI refer to an older or newer release tag
	 * (e.g. 6.13.1) and print it in a message
	 * 
	 * @param lastReleaseTag
	 * @param parameter      : 2nd param given by CLI
	 * @return Boolean
	 */
	public Boolean verifyLastedRelease(String lastReleaseTag, String parameter) {

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


}
