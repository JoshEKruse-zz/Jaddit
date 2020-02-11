package redditBot;

/**
 * Java Wrapper for the Reddit API
 * 
 * Started by Josh Kruse on 8/11/2014
 *
 * Current Features:
 * 1.  Connects to reddit
 * 2.  Logs on
 * 3.  Returns the hot posts of a subreddit
 * 4.  Creates a self post
 * 5.  Creates a link post
 * 6.  Stickies a post
 * 7.  Votes a post up
 * 8.  Votes a post down
 * 9.  Votes a post null
 * 10. Comments on a post
 * 11. Creates a live thread
 * 12. Edits a live thread's information
 * 13. Updates a live thread
 * 14. Closes a live thread
 *
 * Goals:
 * 1. Implement every command in the reddit API
 */

//import libraries
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.*;

public class JavaRedditLibrary
{

    //Instance variables
    private String USER_AGENT;
    private String usag_default = "javaRedditLibrary/1.0 by mrblahblahblacksheep";
    private String modhash;
    private String cookie;
    private String thing_id;
    private String username;
    private String password;
    private CookieManager sessionCookieManager;
    private String live_thread_id;

    //default constructor
    public JavaRedditLibrary(String str1)
    {
        if(!str1.isEmpty())
        {
            USER_AGENT = usag_default; //If a user agent is not given, default will be chosen
        }
        else
        {
            USER_AGENT = str1;         //If a user agent is given, it will be chosen
        }

        sessionCookieManager = new CookieManager(null, CookiePolicy.ACCEPT_ALL);       //adding cookie
        CookieHandler.setDefault(sessionCookieManager);
    }

    // HTTP GET request, tests reddit connection
    public void connect() throws Exception
    {

        String url = "http://www.reddit.com";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null)
        {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());

    }

    // HTTP GET request, returns a JSON with the hot posts on [subreddit]
    public void subredditHot(String str1) throws Exception
    {
        String subreddit = str1;

        String url = "http://www.reddit.com/r/" + subreddit + "/hot";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null)
        {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());

    }

    // HTTP POST request, logins to reddit
    public void login(String str1, String str2) throws Exception
    {

        username = str1;
        password = str2;

        String url = "http://www.reddit.com/api/login/username";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add request header
        con.setRequestMethod("POST");

        String urlParameters = "api_type=json&user=" + username + "&passwd=" + password + "";

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        //System.out.println("\nSending 'POST' request to URL : " + url);
        //System.out.println("Post parameters : " + urlParameters);
        //System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null)
        {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());

        String JSON = response.toString();
        modhash = JSON.substring(66,116);

        cookie = JSON.substring(130,199);
    }

    // HTTP POST request, submits a selfpost to subreddit of your choice
    public void submitSelf(String str1, String str2, String str3) throws Exception
    {

        String subreddit = str1;
        String post_title = str2;
        String post_text = str3;

        String url = "http://www.reddit.com/api/submit";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add request header
        con.setRequestMethod("POST");

        String urlParameters = "api_type=json&uh=" + modhash + "&kind=self&text=" + post_text + "&sr=" + subreddit + "&title=" + post_title + "&r=" + subreddit;

        con.setRequestProperty("Cookie", "name=value");

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        //System.out.println("\nSending 'POST' request to URL : " + url);
        //System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null)
        {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());

        String JSON = response.toString();
        thing_id = JSON.substring(88,94);
    }

    //HTTP post request, submits a linkpost to subreddit of your choice
    public void submitLink(String str1, String str2, String str3) throws Exception
    {

        String subreddit = str1;
        String post_title = str2;
        String post_url = str3;

        String url = "http://www.reddit.com/api/submit";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add request header
        con.setRequestMethod("POST");

        String urlParameters = "api_type=json&uh=" + modhash + "&kind=link&url=" + post_url + "&sr=" + subreddit + "&title=" + post_title + "&r=" + subreddit;

        con.setRequestProperty("Cookie", "name=value");

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        //System.out.println("\nSending 'POST' request to URL : " + url);
        //System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null)
        {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());
    }

    //HTTP post request, stickys a submitted post
    public void stickyPost() throws Exception
    {
        String thing = "t3_" + thing_id;

        String url = "http://www.reddit.com/api/set_subreddit_sticky";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add request header
        con.setRequestMethod("POST");

        String urlParameters = "api_type=json&id="+ thing + "&state=true&uh=" + modhash;

        con.setRequestProperty("Cookie", "name=value");

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        //System.out.println("\nSending 'POST' request to URL : " + url);
        //System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null)
        {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());
    }

    //HTTP post request, upvotes a submitted post
    public void voteUp(String str1) throws Exception
    {

        String subreddit = str1;
        String thing = "t3_" + thing_id;

        String url = "http://www.reddit.com/api/vote";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add request header
        con.setRequestMethod("POST");

        String urlParameters = "id=" + thing + "&dir=1&r=" + subreddit + "&uh=" + modhash;

        con.setRequestProperty("Cookie", "name=value");

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        //System.out.println("\nSending 'POST' request to URL : " + url);
        //System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null)
        {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());
    }

    //HTTP post request, downvotes a submitted post
    public void voteDown(String str1) throws Exception
    {

        String subreddit = str1;
        String thing = "t3_" + thing_id;

        String url = "http://www.reddit.com/api/vote";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add request header
        con.setRequestMethod("POST");

        String urlParameters = "id=" + thing + "&dir=-1&r=" + subreddit + "&uh=" + modhash;

        con.setRequestProperty("Cookie", "name=value");

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        //System.out.println("\nSending 'POST' request to URL : " + url);
        //System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null)
        {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());
    }

    //HTTP post request, votes null a submitted post
    public void voteNull(String str1) throws Exception
    {

        String thing = "t3_" + thing_id;
        String subreddit = str1;

        String url = "http://www.reddit.com/api/vote";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add request header
        con.setRequestMethod("POST");

        String urlParameters = "id=" + thing + "&dir=0&r=" + subreddit + "&uh=" + modhash;

        con.setRequestProperty("Cookie", "name=value");

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        //System.out.println("\nSending 'POST' request to URL : " + url);
        //System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null)
        {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());
    }

    //HTTP post request, comments on a submitted post
    public void commentLink(String str1, String str2) throws Exception
    {

        String thing = "t3_" + thing_id;
        String comment_message = str2;
        String subreddit = str1;

        String url = "http://www.reddit.com/api/comment";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add request header
        con.setRequestMethod("POST");

        String urlParameters = "thing_id=" + thing + "&text=" + comment_message + "&r=" + subreddit + "&uh=" + modhash;

        con.setRequestProperty("Cookie", "name=value");

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        //System.out.println("\nSending 'POST' request to URL : " + url);
        //System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null)
        {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());
    }

    //HTTP post request, creates a live thread with a custom title, description, and recources
    public void createLiveThread(String str1,String str2, String str3) throws Exception
    {
        String description = str1;
        String recourses = str2;
        String thread_title = str3;

        String url = "http://www.reddit.com/api/live/create";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add request header
        con.setRequestMethod("POST");

        String urlParameters = "api_type=json&description=" + description + "&resources=" + recourses + "&title=" + thread_title + "&uh=" + modhash;

        con.setRequestProperty("Cookie", "name=value");

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        //System.out.println("\nSending 'POST' request to URL : " + url);
        //System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null)
        {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());

        String JSON = response.toString();
        live_thread_id = JSON.substring(40, 52);
    }

    //HTTP post request, updates a live thread with a custom message
    public void updateLiveThread(String str1) throws Exception
    {
        String body = str1;

        String url = "http://www.reddit.com/api/live/" + live_thread_id + "/update";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add request header
        con.setRequestMethod("POST");

        String urlParameters = "api_type=json&body=" + body + "&uh=" + modhash;

        con.setRequestProperty("Cookie", "name=value");

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        //System.out.println("\nSending 'POST' request to URL : " + url);
        //System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null)
        {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());
    }

    //HTTP post request, edits a live thread with a new title, description, and recources
    public void editLiveThread(String str1,String str2, String str3) throws Exception
    {
        String description = str1;
        String recourses = str2;
        String thread_title = str3;

        String url = "http://www.reddit.com/api/live/" + live_thread_id + "/edit";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add request header
        con.setRequestMethod("POST");

        String urlParameters = "api_type=json&description=" + description + "&resources=" + recourses + "&title=" + thread_title + "&uh=" + modhash;

        con.setRequestProperty("Cookie", "name=value");

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        //System.out.println("\nSending 'POST' request to URL : " + url);
        //System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null)
        {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());
    }

    //HTTP post request, closes a live thread so no more future updates can be made
    public void closeLiveThread() throws Exception
    {
        String url = "http://www.reddit.com/api/live/" + live_thread_id + "/close_thread";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add request header
        con.setRequestMethod("POST");

        String urlParameters = "api_type=json&uh=" + modhash;

        con.setRequestProperty("Cookie", "name=value");

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        //System.out.println("\nSending 'POST' request to URL : " + url);
        //System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null)
        {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());
    }
}
