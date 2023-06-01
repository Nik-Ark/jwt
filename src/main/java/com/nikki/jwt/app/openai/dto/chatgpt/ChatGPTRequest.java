package com.nikki.jwt.app.openai.dto.chatgpt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ChatGPTRequest implements Serializable {

    private String model;
    private List<Message> messages;

    @Override
    public String toString() {
        return "ChatGPTRequest: {" +
                " model: '" + model + '\'' +
                ", messages: " + messages +
                " " + '}';
    }
}
