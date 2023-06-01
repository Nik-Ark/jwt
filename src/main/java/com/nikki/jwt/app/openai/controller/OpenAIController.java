package com.nikki.jwt.app.openai.controller;

import com.nikki.jwt.app.openai.dto.chatgpt.ChatGPTQuestionRequest;
import com.nikki.jwt.app.openai.dto.chatgpt.ChatGPTResponse;
import com.nikki.jwt.app.openai.dto.whisper.TranscriptionRequest;
import com.nikki.jwt.app.openai.dto.whisper.WhisperTranscriptionResponse;
import com.nikki.jwt.app.openai.service.OpenAIClientService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/open-ai")
public class OpenAIController {

    private final OpenAIClientService openAIClientService;

    @PostMapping(value = "/chat-gpt", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChatGPTResponse> chatGPT(@RequestBody @NotNull ChatGPTQuestionRequest question) {

        log.info("START endpoint '/open-ai/chat-gpt' (Post), request: {}", question);
        log.info("Security Context Was Set for Principal: {}.",
                SecurityContextHolder.getContext().getAuthentication().getPrincipal()
        );

        ChatGPTResponse response = openAIClientService.chatGPT(question);

        log.info("END endpoint '/open-ai/chat-gpt', response id: {}", response.getId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/transcription", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public WhisperTranscriptionResponse createTranscription(@ModelAttribute @NotNull TranscriptionRequest transcriptionRequest) {
        return openAIClientService.createTranscription(transcriptionRequest);
    }
}
