package dev.wms.pwrapi.entity.forum;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Teacher {

    private int id;
    private String category;
    private String academicTitle;
    private String fullName;
    private double average;
    private List<Review> reviews;

    public Teacher(){
        this.reviews = new ArrayList<>();
    }

    public Teacher(int id, String category, String academicTitle, String fullName, double average) {
        this.id = id;
        this.category = category;
        this.academicTitle = academicTitle;
        this.fullName = fullName;
        this.average = average;
        this.reviews = new ArrayList<>();
    }

    public void addReview(Review review){
        reviews.add(review);
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAcademicTitle() {
        return academicTitle;
    }

    public void setAcademicTitle(String academicTitle) {
        this.academicTitle = academicTitle;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }
}
