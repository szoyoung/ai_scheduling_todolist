package com.example.dotodo.network;

import java.util.List;

public class GenerateContentResponse {
    private List<Candidate> candidates;

    public static class Candidate {
        private Content content;

        public static class Content {
            private List<Parts> parts;  // 객체에서 배열로 변경

            public static class Parts {
                private String text;

                public String getText() {
                    return text;
                }
            }

            public List<Parts> getParts() {
                return parts;
            }
        }

        public Content getContent() {
            return content;
        }
    }

    public String getGeneratedText() {
        if (candidates != null && !candidates.isEmpty()
                && candidates.get(0).content != null
                && candidates.get(0).content.parts != null
                && !candidates.get(0).content.parts.isEmpty()) {
            return candidates.get(0).content.parts.get(0).text;  // 배열의 첫 번째 요소 사용
        }
        return null;
    }
}