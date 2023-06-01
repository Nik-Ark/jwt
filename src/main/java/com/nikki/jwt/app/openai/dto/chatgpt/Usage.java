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
public class Usage implements Serializable {

    @JsonProperty("prompt_tokens")
    private String promptTokens;

    @JsonProperty("completion_tokens")
    private String completionTokens;

    @JsonProperty("total_tokens")
    private String totalTokens;

    @Override
    public String toString() {
        return "Usage: {" +
                " promptTokens: '" + promptTokens + '\'' +
                ", completionTokens: '" + completionTokens + '\'' +
                ", totalTokens: '" + totalTokens + '\'' +
                " " + '}';
    }
}
