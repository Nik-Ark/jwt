package com.nikki.jwt.app.openai.dto.chatgpt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Choice implements Serializable {

    private Integer index;
    private Message message;

    @JsonProperty("finish_reason")
    private String finishReason;

    @Override
    public String toString() {
        return "Choice: {" +
                " index: " + index +
                ", message: " + message +
                ", finishReason: '" + finishReason + '\'' +
                " " + '}';
    }
}
