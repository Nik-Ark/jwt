package com.nikki.jwt.app.openai.dto.chatgpt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
public class ChatGPTQuestionRequest implements Serializable {

    private String question;

    @Override
    public String toString() {
        return "ChatGPTQuestionRequest: {" +
                " question: '" + question + '\'' +
                " " + '}';
    }
}
