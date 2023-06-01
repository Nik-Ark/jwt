package com.nikki.jwt.app.openai.dto.chatgpt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ChatGPTResponse implements Serializable {
    private String id;
    private String object;
    private String model;
    private LocalDate created;
    private List<Choice> choices;
    private Usage usage;

    @Override
    public String toString() {
        return "ChatGPTResponse: {" +
                " id: '" + id + '\'' +
                ", object: '" + object + '\'' +
                ", model: '" + model + '\'' +
                ", created: " + created +
                ", choices: " + choices +
                ", usage: " + usage +
                " " + '}';
    }
}
