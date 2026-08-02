package com.cscb07.taamapp.itemSorting;

import androidx.annotation.NonNull;

import com.cscb07.taamapp.Item;

import java.util.Locale;

/**
 * Class that judges similarity between 2 {@link Item}s
 */
public class ItemRanker {
    /**
     * Checks if a substring is contained in the other, being case invariant.
     * Assumes keyword is already in lower case, for efficiency.
     * @param source The string to check if it contains the keyword
     * @param keyword The keyword to check if it is a substring.
     * @return True if keyword is in source, False otherwise.
     */
    private static boolean contains(@NonNull String source, String keyword) {
        return source.toLowerCase(Locale.ROOT).contains(keyword);
    }

    /**
     * Checks if the artifact has the keyword in any of its fields. It is case invariant
     * @param artifact The artifact to check
     * @param keyword The keyword to check if contained. <b><i>Assumed to be in lower case</i></b>.
     * @return True if the artifact has the keyword, False otherwise.
     */
    public boolean artifactHasKeyword(@NonNull Item artifact, @NonNull String keyword) {
        return     contains(artifact.getArtifactName(), keyword)
                || contains(artifact.getDescription(), keyword)
                || contains(artifact.getMaterial(), keyword)
                || contains(artifact.getCategory(), keyword)
                || contains(artifact.getDynastyPeriod(), keyword)
                || contains(artifact.getCulturalOrigin(), keyword)
                || contains(artifact.getDimensions(), keyword)
                || contains(artifact.getConditionReport(), keyword)
                || contains(artifact.getCurrentLocation(), keyword)
                || contains(artifact.getAcquisitionMethod(), keyword)
                || contains(artifact.getProvenance(), keyword)
                || contains(artifact.getAccessionNumber(), keyword)
                || contains(artifact.getNotes(), keyword);
    }

    /**
     * Ranks the artifact based on how many times the keyword appears in the artifact's fields.
     * @param artifact The artifact to check
     * @param keyword The keyword to check if contained. <b><i>Assumed to be in lower case</i></b>.
     * @return The ranking of the artifact, as an integer. Note: does not necessarily indicate number of matches.
     */
    public int rankArtifactByKeyword(@NonNull Item artifact, @NonNull String keyword) {
        int ranking = 0;
        ranking += contains(artifact.getArtifactName(), keyword)      ? 1 : 0;
        ranking += contains(artifact.getDescription(), keyword)       ? 1 : 0;
        ranking += contains(artifact.getMaterial(), keyword)          ? 1 : 0;
        ranking += contains(artifact.getCategory(), keyword)          ? 1 : 0;
        ranking += contains(artifact.getDynastyPeriod(), keyword)     ? 1 : 0;
        ranking += contains(artifact.getCulturalOrigin(), keyword)    ? 1 : 0;
        ranking += contains(artifact.getDimensions(), keyword)        ? 1 : 0;
        ranking += contains(artifact.getConditionReport(), keyword)   ? 1 : 0;
        ranking += contains(artifact.getCurrentLocation(), keyword)   ? 1 : 0;
        ranking += contains(artifact.getAcquisitionMethod(), keyword) ? 1 : 0;
        ranking += contains(artifact.getProvenance(), keyword)        ? 1 : 0;
        ranking += contains(artifact.getAccessionNumber(), keyword)   ? 1 : 0;
        ranking += contains(artifact.getNotes(), keyword)             ? 1 : 0;
        return ranking;
    }

    /**
     * Ranks the similarity of the target artifact to the source artifact.
     * <p>
     * This may <i>not</i> be symmetric.
     * @param source The base artifact to compare against
     * @param target The artifact to rank similarity
     * @return Returns the rank of similarity of the 2 artifacts as an int,
     *         where a larger value indicates more similarity.
     */
    public int rankSimilarity(@NonNull Item source, @NonNull Item target) {
        int ranking = 0;
        String[] keywords = source.getArtifactName().toLowerCase(Locale.ROOT).split("\\s");
        for (String keyword : keywords) {
            ranking += rankArtifactByKeyword(target, keyword);
        }

        if (source.getMaterial().equals(target.getMaterial())) {
            ranking += 5;
        }
        if (source.getCategory().equals(target.getCategory())) {
            ranking += 5;
        }
        if (source.getDynastyPeriod().equals(target.getDynastyPeriod())) {
            ranking += 5;
        }

        return ranking;
    }

}
