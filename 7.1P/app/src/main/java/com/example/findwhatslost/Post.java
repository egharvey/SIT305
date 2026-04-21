package com.example.findwhatslost;

public class Post {
    private int id, phone;
    private String name, description, location, image, foundDate, postedDate;

    public Post() {}

    public Post(int id, int phone, String name, String description, String location, String image, String foundDate, String postedDate){
        this.id = id;
        this.phone = phone;
        this.name = name;
        this.description = description;
        this.location = location;
        this.image = image;
        this.foundDate = foundDate;
        this.postedDate = postedDate;
    }

    //Getters
    public int getId() { return id; }
    public int getPhone() { return phone; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getLocation() { return location; }
    public String getImage() { return image; }
    public String getFoundDate() { return foundDate; }
    public String getPostedDate() { return postedDate; }
    //Setters
    public void setId(int id) { this.id = id; }
    public void setPhone(int phone) { this.phone = phone; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setLocation(String location) { this.location = location; }
    public void setImage(String image) { this.image = image; }
    public void setFoundDate(String date) { this.foundDate = date; }
    public void setPostedDate(String date) { this.postedDate = date; }
}
