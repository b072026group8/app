package com.cscb07.taamapp;

import java.util.Objects;

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
    // no idea how to store images right now, assume grabbed from supabase with url
    private String image;

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
     * default ctor, to be used with FirebaseDatabase (<c>DataSnapshot.get()</c>).
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

    @Override
    public int hashCode() {
        return Objects.hashCode(lotNumber);
    }
}
