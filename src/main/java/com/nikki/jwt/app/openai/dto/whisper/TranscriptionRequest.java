package com.nikki.jwt.app.openai.dto.whisper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TranscriptionRequest implements Serializable {

    private MultipartFile file;

    @Override
    public String toString() {
        return "TranscriptionRequest: {" +
                " file: " + file +
                " " + '}';
    }
}
