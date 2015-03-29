package alexiil.utils.version;

public class Release {
    public final String name;
    public final Commit commit;

    public Release(String name, Commit commit) {
        this.name = name;
        this.commit = commit;
    }
}
