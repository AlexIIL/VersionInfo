package alexiil.utils.version;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class VersionGenerator {
    public static void main(String[] args) {
        GitRequester.accessToken = System.getenv("GITHUB_ACCESS_TOKEN");
        String user = System.getenv("GITHUB_USER");
        if (user == null)
            user = "AlexIIL";
        String repo = System.getenv("GITHUB_REPO");
        if (repo == null)
            repo = "CivCraft";
        if (GitRequester.accessToken == null || GitRequester.accessToken.length() < 9) {
            System.out.println("Access Token was not valid!");
        }
        else {
            // Fetch the lists
            String contributors = GitRequester.getContributors(user, repo);

            String commits = "[";
            int pageNo = 1;
            while (pageNo <= 100) {
                // Don't fetch more than 10000 commits, thats just silly
                String commitTemp = GitRequester.getCommits(user, repo, pageNo);
                if (commitTemp.length() < 50)
                    break;
                if (commits != null && commits.length() > 50) {
                    commits = commits.substring(0, commits.length() - 1);
                    commits += ",";
                }
                commits += commitTemp.substring(1);
                if (commits.length() > commitTemp.length() + 40)
                    pageNo++;
            }
            String releases = GitRequester.getReleases(user, repo);

            System.out.println(contributors.length() + " contributors, " + commits.length() + " commits, " + releases.length() + " releases.");

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File("output.json")))) {
                writer.write("\"commits\":" + commits.replace("[", "[\n") + ",\n");
                writer.write("\"contributors\":" + contributors.replace("[", "[\n") + ",\n");
                writer.write("\"releases\":" + releases.replace("[", "[\n") + "");
                writer.close();

                System.out.println(new File("output.json").getAbsolutePath());
            }
            catch (IOException e) {
                e.printStackTrace();
                throw new Error(e);
            }
        }
    }
}
