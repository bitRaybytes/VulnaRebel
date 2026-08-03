package challenge;

import challenge.challengesIndex.ChallengesHandler;

/**
 * Represents a single entry on the challenge index page
 * that holds all links to the challenges.
 * <p>
 * A list of {@code ChallengeLink} instances is passed to
 * {@link ChallengesHandler}, which renders each one as
 * an HTML card replacing the {@code {{challengeLink}}} placeholder
 * in {@code challenges.html}.
 * </p>
 *
 * @param route       the route to the challenge
 * @param title       a short title to index the link
 * @param difficulty  the difficulty of the challenge
 */
public record ChallengeLink(String route, String title, String difficulty) {

    public String toHtml(){
        StringBuilder link = new StringBuilder();

        return link.append("<a style=\"cursor:pointer; text-align:center; font-weight:bold; margin:10px 0; font-size:18px; ")
                .append("background-color:lightgrey; padding:10px; border-radius:10px; box-shadow:5px black; width:250px;\"")
                .append(" class=\"challengeLink\" ") // because of hover property on index page
                .append(" href=\"")
                .append(route)
                .append("\">")
                .append(title)
                .append(" (")
                .append(difficulty)
                .append(") </a>").toString();
    }
}
