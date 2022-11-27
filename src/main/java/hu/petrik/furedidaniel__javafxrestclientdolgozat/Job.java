package hu.petrik.furedidaniel__javafxrestclientdolgozat;

import com.google.gson.annotations.Expose;

public class Job {

    private int id;
    @Expose
    private String jobTitle;
    @Expose
    private String companyName;
    @Expose
    private String companyEmail;
    @Expose
    private int hiringPriority;
    @Expose
    private boolean isHiring;

    public Job(int id, String jobTitle, String companyName, String companyEmail, int hiringPriority, boolean isHiring) {
        this.id = id;
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.companyEmail = companyEmail;
        this.hiringPriority = hiringPriority;
        this.isHiring = isHiring;
    }

    // Getters

    public int getId() {
        return id;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public int getHiringPriority() {
        return hiringPriority;
    }

    public boolean getIsHiring() {
        return isHiring;
    }

    // Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    public void setHiringPriority(int hiringPriority) {
        this.hiringPriority = hiringPriority;
    }

    public void setHiring(boolean hiring) {
        isHiring = hiring;
    }
}
