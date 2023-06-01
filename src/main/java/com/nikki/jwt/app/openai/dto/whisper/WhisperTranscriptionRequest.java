package com.nikki.jwt.app.openai.dto.whisper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class WhisperTranscriptionRequest implements Serializable {

    private String model;
    private MultipartFile file;
}
