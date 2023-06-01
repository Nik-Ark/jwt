package com.nikki.jwt.app.openai.dto.chatgpt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Message implements Serializable {

    private String role;
    private String content;

    @Override
    public String toString() {
        return "Message: {" +
                " role: '" + role + '\'' +
                ", content: '" + content + '\'' +
                " " + '}';
    }
}
