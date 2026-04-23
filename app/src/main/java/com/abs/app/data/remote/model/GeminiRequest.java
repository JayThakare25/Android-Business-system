package com.abs.app.data.remote.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.Collections;

public class GeminiRequest {
    @SerializedName("contents")
    public List<Content> contents;

    public GeminiRequest(String prompt) {
        this.contents = Collections.singletonList(new Content(prompt));
    }

    public static class Content {
        @SerializedName("parts")
        public List<Part> parts;
        public Content(String text) {
            this.parts = Collections.singletonList(new Part(text));
        }
    }

    public static class Part {
        @SerializedName("text")
        public String text;
        public Part(String text) {
            this.text = text;
        }
    }
}
