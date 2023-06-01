package com.nikki.jwt.app.openai.dto.whisper;

import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class WhisperTranscriptionResponse implements Serializable {
    private String text;
}
