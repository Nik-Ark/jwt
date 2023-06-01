package com.nikki.jwt.app.openai.openai_client;

import com.nikki.jwt.app.openai.dto.chatgpt.ChatGPTRequest;
import com.nikki.jwt.app.openai.dto.chatgpt.ChatGPTResponse;
import com.nikki.jwt.app.openai.dto.whisper.WhisperTranscriptionRequest;
import com.nikki.jwt.app.openai.dto.whisper.WhisperTranscriptionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "openai-service",
        url = "${openai.config.urls.base-url}",
        configuration = OpenAIClientConfig.class
)
public interface OpenAIClient {

    @PostMapping(value = "${openai.config.urls.chat-url}", headers = {"Content-Type=application/json"})
    ChatGPTResponse chatGPT(@RequestBody ChatGPTRequest chatGPTRequest);

    @PostMapping(value = "${openai.config.urls.create-transcription-url}", headers = {"Content-Type=multipart/form-data"})
    WhisperTranscriptionResponse createTranscription(@ModelAttribute WhisperTranscriptionRequest whisperTranscriptionRequest);
}
