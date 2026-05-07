package com.example.learningbyloosing;

import java.util.ArrayList;
import java.util.List;

public class ApiRequest {
    public List<Content> contents;

    public ApiRequest(String promptText) {
        contents = new ArrayList<>();
        Content content = new Content();
        content.parts = new ArrayList<>();
        Part part = new Part();
        part.text = promptText;
        content.parts.add(part);
        contents.add(content);
    }

    public static class Content {
        public List<Part> parts;
    }

    public static class Part {
        public String text;
    }
}
