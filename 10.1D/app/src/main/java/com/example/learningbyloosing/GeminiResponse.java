package com.example.learningbyloosing;

import java.util.List;

public class GeminiResponse {

    public List<Candidate> candidates;

    public String getFirstText() {
        if (candidates != null && !candidates.isEmpty()) {
            Candidate c = candidates.get(0);
            if (c.content != null && c.content.parts != null && !c.content.parts.isEmpty()) {
                return c.content.parts.get(0).text;
            }
        }
        return "No response received.";
    }

    public static class Candidate {
        public GeminiRequest.Content content;
    }
}
