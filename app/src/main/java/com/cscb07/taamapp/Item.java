package com.cscb07.taamapp;

import java.util.Objects;

/**
 * Represents a museum artifact in the catalog.
 * Stores identifying information, descriptive details, and
 * the image URL associated with the artifact.
 */
public class Item {

    private String lotNumber;
    private String artifactName;
    private String description;
    private String category;
    private String material;
    private String dynastyPeriod;
    private String culturalOrigin;
    private String dimensions;
    private String conditionReport;
    private String currentLocation;
    private String acquisitionMethod;
    private String provenance;
    private String accessionNumber;
    private String notes;
    /** URL of the artifact image stored in Supabase. */
    private String image;

    /**
     * Creates a new artifact with the specified information.
     *
     * @param lotNumber the artifact lot number
     * @param artifactName the artifact name
     * @param description the artifact description
     * @param category the artifact category
     * @param material the material the artifact is made from
     * @param dynastyPeriod the dynasty or historical period
     * @param culturalOrigin the artifact's cultural origin
     * @param dimensions the artifact dimensions
     * @param conditionReport the condition report
     * @param currentLocation the artifact's current location
     * @param acquisitionMethod the acquisition method
     * @param provenance the artifact provenance
     * @param accessionNumber the accession number
     * @param notes additional notes about the artifact
     * @param image the URL of the artifact image
     */
    public Item(String lotNumber, String artifactName, String description, String category, String material, String dynastyPeriod, String culturalOrigin, String dimensions, String conditionReport, String currentLocation, String acquisitionMethod, String provenance, String accessionNumber, String notes, String image) {
        this.lotNumber = lotNumber;
        this.artifactName = artifactName;
        this.description = description;
        this.category = category;
        this.material = material;
        this.dynastyPeriod = dynastyPeriod;
        this.culturalOrigin = culturalOrigin;
        this.dimensions = dimensions;
        this.conditionReport = conditionReport;
        this.currentLocation = currentLocation;
        this.acquisitionMethod = acquisitionMethod;
        this.provenance = provenance;
        this.accessionNumber = accessionNumber;
        this.notes = notes;
        this.image = image;
    }

    /**
     * Creates an empty artifact.
     * Required for Firebase object deserialization.
     */
    public Item() {
        this.lotNumber = "";
        this.artifactName = "";
        this.description = "";
        this.category = "";
        this.material = "";
        this.dynastyPeriod = "";
        this.culturalOrigin = "";
        this.dimensions = "";
        this.conditionReport = "";
        this.currentLocation = "";
        this.acquisitionMethod = "";
        this.provenance = "";
        this.accessionNumber = "";
        this.notes = "";
        this.image = "";
    }

    // Getters and setters
    public String getArtifactName() { return artifactName; }
    public void setArtifactName(String artifactName) { this.artifactName = artifactName; }
    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLotNumber() {
        return lotNumber;
    }

    public void setLotNumber(String lotNumber) {
        this.lotNumber = lotNumber;
    }

    public String getDynastyPeriod() {
        return dynastyPeriod;
    }

    public void setDynastyPeriod(String dynastyPeriod) {
        this.dynastyPeriod = dynastyPeriod;
    }

    public String getCulturalOrigin() {
        return culturalOrigin;
    }

    public void setCulturalOrigin(String culturalOrigin) {
        this.culturalOrigin = culturalOrigin;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public String getConditionReport() {
        return conditionReport;
    }

    public void setConditionReport(String conditionReport) {
        this.conditionReport = conditionReport;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getAcquisitionMethod() {
        return acquisitionMethod;
    }

    public void setAcquisitionMethod(String acquisitionMethod) {
        this.acquisitionMethod = acquisitionMethod;
    }

    public String getProvenance() {
        return provenance;
    }

    public void setProvenance(String provenance) {
        this.provenance = provenance;
    }

    public String getAccessionNumber() {
        return accessionNumber;
    }

    public void setAccessionNumber(String accessionNumber) {
        this.accessionNumber = accessionNumber;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    /**
     * Compares this artifact with another object for equality.
     * Two artifacts are considered equal when all stored fields match.
     *
     * @param o the object to compare
     * @return true if the artifacts are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Item)) return false;
        Item item = (Item) o;
        return     Objects.equals(lotNumber, item.lotNumber)
                && Objects.equals(artifactName, item.artifactName)
                && Objects.equals(description, item.description)
                && Objects.equals(category, item.category)
                && Objects.equals(material, item.material)
                && Objects.equals(dynastyPeriod, item.dynastyPeriod)
                && Objects.equals(culturalOrigin, item.culturalOrigin)
                && Objects.equals(dimensions, item.dimensions)
                && Objects.equals(conditionReport, item.conditionReport)
                && Objects.equals(currentLocation, item.currentLocation)
                && Objects.equals(acquisitionMethod, item.acquisitionMethod)
                && Objects.equals(provenance, item.provenance)
                && Objects.equals(accessionNumber, item.accessionNumber)
                && Objects.equals(notes, item.notes)
                && Objects.equals(image, item.image);
    }

    /**
     * Returns the hash code for this artifact.
     *
     * @return the hash code based on the artifact's lot number
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(lotNumber);
    }
}
