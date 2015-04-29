package alexiil.utils.version;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class VersionGenerator {
    public static void main(String[] args) {
        if (args.length != 2) {
            throw new Error("Was not given any arguments!" + " There must be three, in the form <USER> <REPOSITORY>");
        }
        String user = args[0];
        if (user == null) {
            throw new Error("Could not get user from arguments!");
        }
        String repo = args[1];
        if (repo == null) {
            throw new Error("Could not get repo from arguments!");
        }

        // Fetch the lists
        String contributors = GitHubRequester.getContributors(user, repo);

        String commits = "[";
        int pageNo = 1;
        while (pageNo <= 100) {
            // Don't fetch more than 10000 commits, thats just silly
            String commitTemp = GitHubRequester.getCommits(user, repo, pageNo);
            if (commitTemp.length() < 50) {
                break;
            }
            if (commits != null && commits.length() > 50) {
                commits = commits.substring(0, commits.length() - 1);
                commits += ",";
            }
            commits += commitTemp.substring(1);
            pageNo++;
        }
        String releases = GitHubRequester.getReleases(user, repo);

        System.out.println(contributors.length() + " contributors, " + commits.length() + " commits, " + releases.length() + " releases.");

        new File("version").mkdir();

        writeFile("version/commits.json", commits.replace("\n", ""));
        writeFile("version/contributors.json", contributors.replace("\n", ""));
        writeFile("version/releases.json", releases.replace("\n", ""));
    }

    private static void writeFile(String name, String contents) {
        BufferedWriter writer = null;
        try {
            File file = new File(name);
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(contents);
            writer.close();

            System.out.println(file.getAbsolutePath());
        }
        catch (IOException e) {
            e.printStackTrace();
            try {
                writer.close();
            }
            catch (IOException e1) {
                e1.printStackTrace();
            }
            throw new Error(e);
        }
    }
}
