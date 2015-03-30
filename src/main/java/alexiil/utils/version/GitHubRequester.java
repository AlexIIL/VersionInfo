package alexiil.utils.version;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

public class GitHubRequester {
    public static String accessToken = null;

    public static String getContributors(String user, String repo) {
        return getResponse("repos/" + user + "/" + repo + "/contributors");
    }

    public static String getCommits(String user, String repo, int pageNo) {
        return getResponse("repos/" + user + "/" + repo + "/commits?page=" + pageNo + "&per_page=100");
    }

    public static String getReleases(String user, String repo) {
        return getResponse("repos/" + user + "/" + repo + "/tags");
    }

    /** Get a GitHub API response, without using an access token */
    public static String getResponse(String site) {
        if (site.contains("?") && accessToken != null)
            return getResponse(site + "&access_token=" + accessToken, null);
        return getResponse(site, accessToken);
    }

    public static void uploadGist(String site, String gistData) {
        getResponse("", accessToken);
    }

    /** This appends the site to "https://api.github.com" so you don't need to (also, so you cannot use this method for
     * non-GitHub sites)<br>
     * The accessToken parameter is for if you have an access token, and you don't have any parameters in the site (so,
     * if your site is <code>"repo/AlexIIL/CivCraft/issues"</code> then you can use an access token, but if your site is
     * <code>"repo/AlexIIL/CivCraft/issues?label:enhancement"</code> you cannot. If any error occurs, then the returned
     * string is <code>null</code>, and an error is printed out to console */
    public static String getResponse(String site, String accessToken) {
        try {
            if (accessToken != null)
                site = site + "?access_token=" + accessToken;
            URLConnection url = new URL("https://api.github.com/" + site).openConnection();
            InputStream response = url.getInputStream();
            System.out.println(url.getHeaderField("X-RateLimit-Remaining") + " requests left from GitHub in this hour");
            BufferedReader br = new BufferedReader(new InputStreamReader(response, Charset.forName("UTF-8")));
            return br.readLine();
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
