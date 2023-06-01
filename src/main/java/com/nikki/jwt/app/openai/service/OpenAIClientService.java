package com.nikki.jwt.app.openai.service;

import com.nikki.jwt.app.openai.dto.chatgpt.ChatGPTQuestionRequest;
import com.nikki.jwt.app.openai.dto.chatgpt.ChatGPTRequest;
import com.nikki.jwt.app.openai.dto.chatgpt.ChatGPTResponse;
import com.nikki.jwt.app.openai.dto.chatgpt.Message;
import com.nikki.jwt.app.openai.dto.whisper.TranscriptionRequest;
import com.nikki.jwt.app.openai.dto.whisper.WhisperTranscriptionRequest;
import com.nikki.jwt.app.openai.dto.whisper.WhisperTranscriptionResponse;
import com.nikki.jwt.app.openai.openai_client.OpenAIClient;
import com.nikki.jwt.app.openai.openai_client.OpenAIClientConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class OpenAIClientService {

    private final OpenAIClient openAIClient;
    private final OpenAIClientConfig openAIClientConfig;

    private final static String ROLE_USER = "user";

    public ChatGPTResponse chatGPT(ChatGPTQuestionRequest question){
        Message message = Message.builder()
                .role(ROLE_USER)
                .content(question.getQuestion())
                .build();
        ChatGPTRequest chatGPTRequest = ChatGPTRequest.builder()
                .model(openAIClientConfig.getModel())
                .messages(Collections.singletonList(message))
                .build();
        return openAIClient.chatGPT(chatGPTRequest);
    }

    public WhisperTranscriptionResponse createTranscription(TranscriptionRequest transcriptionRequest){
        WhisperTranscriptionRequest whisperTranscriptionRequest = WhisperTranscriptionRequest.builder()
                .model(openAIClientConfig.getAudioModel())
                .file(transcriptionRequest.getFile())
                .build();
        return openAIClient.createTranscription(whisperTranscriptionRequest);
    }
}
