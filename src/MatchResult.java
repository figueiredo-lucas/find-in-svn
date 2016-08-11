

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author figueiredo
 */
public class MatchResult {

    private final String fileName;
    private final List<Integer> lineMatches = new ArrayList<>();

    public void addLineMatch(final Integer lineMatch) {
        lineMatches.add(lineMatch);
    }

    public MatchResult(final String fileName) {
        this.fileName = fileName;
    }

    public boolean matched() {
        return !lineMatches.isEmpty();
    }

    @Override
    public String toString() {
        if (!lineMatches.isEmpty()) {
            return fileName + " (" + lineMatches.toString().replace("[", "").replace("]", "") + ")";
        }
        return null;
    }

}
