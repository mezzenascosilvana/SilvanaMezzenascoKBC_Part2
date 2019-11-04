package test;



import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import static org.testng.Assert.assertTrue;
import java.io.IOException;
import java.util.ArrayList;
import org.testng.annotations.BeforeTest;
import GitHubHandling.GitHubHandlerRequest;
import GitHubHandling.GitHubHandlerResponse;
import Log.CreateLog;
import ScannerParameters.SetParameters;


public class GitHubTest {

	static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36";
	static final String REQUESTURL = "https://api.github.com/search/repositories";
	public static CreateLog log = new CreateLog();
	public static String newRequestUrlTemp= null;
	public static String resultTemp= null;
	public static String lastReleaseTagResult_Temp=null;
	public static GitHubHandlerResponse obj2 = new GitHubHandlerResponse();
	public static SetParameters obj3 =new SetParameters();
	public static ArrayList<String> parametersTemp = null;

    /***
     * Setup the pareameters and create the first url.
     * @param browser
     * @throws IOException 
     */
	@BeforeTest
	public void setup() throws IOException {
		System.out.println("**************Start the Set up*****************");
		ArrayList<String> parameters = obj3. setUpParameters( );
		parametersTemp =parameters ;
		String newRequestUrl = REQUESTURL + "?q=" + parameters.get(0) + "&sort=stars&order=desc";
		System.out.println(REQUESTURL);
		log.createLog("RESPONSE: " + newRequestUrl);
	    assertTrue(newRequestUrl!=null);
	    newRequestUrlTemp=newRequestUrl;
	    System.out.println("**************Finish the Set up*****************");
	}
   
	/***
     * Gets all the repositories for the determined URL
     * @throws IOException 
     * @throws InterruptedException
     */
	@Test
	public void getResponse() throws IOException {
		System.out.println("**************Start to get Response*****************");
		GitHubHandlerRequest obj1 =new GitHubHandlerRequest();
		String result = obj1.getHttpResponse(newRequestUrlTemp, USER_AGENT);
	    AssertJUnit.assertTrue(result!=null);
	    resultTemp=result;
	    System.out.println("**************Finish to get Response*****************");
	}
	 /***
     * @throws IOException 
	 * @throws InterruptedException
     */
	@Test(dependsOnMethods = { "getResponse" })
	public void showFirstResultName_Star() throws IOException {
		System.out.println("**************Start to Show the Result Names*****************");
		ArrayList<String> nameAndStarResult = obj2.getKeys(resultTemp, "ShowFirstResultName&Star");
	    AssertJUnit.assertTrue(nameAndStarResult!=null);
		//showLastReleaseTagResult(obj2, result);
		log.createLog("RESPONSE: " + nameAndStarResult.get(0));
		log.createLog("RESPONSE: " + nameAndStarResult.get(1));
		System.out.println("**************Finish to Show the Result Names*****************");
	}
	
	 /***
     * Shows all the release tags for the new repository (new url)
     * @throws IOException 
	 * @throws InterruptedException
     */
	@Test(dependsOnMethods = { "showFirstResultName_Star" })
	public void showLastReleaseTagResult() throws IOException {
		System.out.println("**************Start to Show the last Release Tag Result*****************");
		String lastReleaseTagResult = obj2.showLatestReleaseTag(resultTemp, USER_AGENT);
	    AssertJUnit.assertTrue(lastReleaseTagResult!=null);
		log.createLog("RESPONSE SECOND URL: " + lastReleaseTagResult);	
		System.out.println("The result for the test is : "+ lastReleaseTagResult);
		lastReleaseTagResult_Temp=lastReleaseTagResult;
		System.out.println("**************Finish to Show the last Release Tag Result*****************");
	}
	
	/**
	 * Compares if the version that was getting is the same as the version found in the arg1
	 * @param obj2
	 * @param lastReleaseTagResult
	 * @throws IOException
	 */
	@Test(dependsOnMethods = { "showLastReleaseTagResult" })
	public void verifyLastedReleaset(  ) throws IOException {
		System.out.println("**************Start to Verify the lasted release*****************");
		assertTrue(obj2.verifyLastedRelease(lastReleaseTagResult_Temp, parametersTemp.get(1)));
		System.out.println("**************Finish to Verify the lasted release*****************");
	}

}
