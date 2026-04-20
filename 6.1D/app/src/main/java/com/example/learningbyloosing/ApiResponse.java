package com.example.learningbyloosing;

import java.util.ArrayList;
import java.util.List;

public class ApiResponse {
    public List<Candidate> candidates;

    public List<String> getAllText() {
        List<String> allText = new ArrayList<>();
        if (candidates != null && !candidates.isEmpty()) {
            Candidate c = candidates.get(0);
            if (c.content != null && c.content.parts != null && !c.content.parts.isEmpty()) {
                allText.add(c.content.parts.get(0).text);
            }
        } else {
            allText.add("No Response found");
        }

        return allText;
    }

    public static class Candidate {
        public ApiRequest.Content content;
    }
}
