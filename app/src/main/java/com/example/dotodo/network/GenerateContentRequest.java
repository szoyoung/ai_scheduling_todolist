package com.example.dotodo.network;

import java.util.Collections;
import java.util.List;

public class GenerateContentRequest {
    private List<Content> contents;

    public GenerateContentRequest(String prompt) {
        this.contents = Collections.singletonList(new Content(prompt));
    }

    static class Content {
        private Parts parts;

        Content(String text) {
            this.parts = new Parts(text);
        }

        static class Parts {
            private String text;

            Parts(String text) {
                this.text = text;
            }
        }
    }
}