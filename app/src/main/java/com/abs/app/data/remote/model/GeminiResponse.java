package com.abs.app.data.remote.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class GeminiResponse {
    @SerializedName("candidates")
    public List<Candidate> candidates;

    public static class Candidate {
        @SerializedName("content")
        public Content content;
    }

    public static class Content {
        @SerializedName("parts")
        public List<Part> parts;
    }

    public static class Part {
        @SerializedName("text")
        public String text;
    }
    
    public String getResponseText() {
        if (candidates != null && !candidates.isEmpty() && 
            candidates.get(0).content != null && 
            candidates.get(0).content.parts != null && 
            !candidates.get(0).content.parts.isEmpty()) {
            return candidates.get(0).content.parts.get(0).text;
        }
        return "No response generated.";
    }
}
