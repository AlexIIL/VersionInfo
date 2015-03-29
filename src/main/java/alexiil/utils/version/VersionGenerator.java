package alexiil.utils.version;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class VersionGenerator {
    public static void main(String[] args) {
        GitRequester.accessToken = System.getenv("GITHUB_ACCESS_TOKEN");
        // if (GitRequester.accessToken == null || GitRequester.accessToken.length() < 9) {
        // System.out.println("Access Token was not valid!");
        // }
        // else {
        // Fetch the lists
        List<GitHubUser> contributors = Collections.unmodifiableList(GitRequester.getContributors("AlexIIL", "CivCraft"));

        List<Commit> commits = new ArrayList<Commit>();
        int pageNo = 1;
        while (pageNo <= 100) {
            // Don't fetch more than 10000 commits, thats just silly
            List<Commit> commitTemp = GitRequester.getCommits("AlexIIL", "CivCraft", pageNo);
            if (commitTemp.size() == 0)
                break;
            commits.addAll(commitTemp);
            pageNo++;
        }
        Collections.sort(commits, new Comparator<Commit>() {
            @Override
            public int compare(Commit c0, Commit c1) {
                return c1.commit.committer.date.compareTo(c0.commit.committer.date);
            }
        });
        commits = Collections.unmodifiableList(commits);

        List<Release> releases = Collections.unmodifiableList(GitRequester.getReleases("AlexIIL", "CivCraft"));

        System.out.println(contributors.size() + " contributors, " + commits.size() + " commits, " + releases.size() + ".");
        // }
    }
}
