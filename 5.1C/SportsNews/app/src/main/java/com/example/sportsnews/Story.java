package com.example.sportsnews;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

//Class is currently used for dummy data, commented sections include code to turn this into a Room Entity

//@Entity(tableName = "stories")
public class Story {

    //@PrimaryKey (autoGenerate = true)
    //private int id;

    private String Image, Title, Description;
    private boolean Bookmark;
    private List<Story> RelatedStories;

    public Story (String image, String title, String description, Boolean bookmark, @Nullable List<Story> relatedStories){
        Image = image;
        Title = title;
        Description = description;
        Bookmark = bookmark;
        if(relatedStories == null || relatedStories.isEmpty()){
            RelatedStories = new ArrayList<>();
        } else{
            RelatedStories = relatedStories;
        }
    }

    //Getters
    //public int getId() { return Id; }
    public String getImage() { return Image; }
    public String getTitle() { return Title; }
    public String getDescription() { return Description; }
    public Boolean getBookmark() { return Bookmark; }
    public List<Story> getRelatedStories() { return RelatedStories; }

    //Setters
    //public void setId(int id) { this.id = id; }
    public void setImage(String image) { Image = image; }
    public void setTitle(String title) { Title = title; }
    public void setLocation(String description) { Description = description; }
    public void setBookmark(Boolean bookmark) { Bookmark = bookmark; }
    public void setRelatedStories(List<Story> stories) { RelatedStories = stories; }
}
