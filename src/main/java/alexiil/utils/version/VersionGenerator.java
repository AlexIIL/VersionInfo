package alexiil.utils.version;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class VersionGenerator {
    public static void main(String[] args) {
        String user = System.getenv("GITHUB_USER");
        if (user == null)
            user = "AlexIIL";
        String repo = System.getenv("GITHUB_REPO");
        if (repo == null)
            repo = "CivCraft";
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

        new File("version").mkdir();

        writeFile("version/commits.json", commits);
        writeFile("version/contributors.json", contributors);
        writeFile("version/releases.json", releases);
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
