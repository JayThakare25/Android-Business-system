package com.abs.app.presentation.ai;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.abs.app.BuildConfig;
import com.abs.app.data.remote.api.GeminiApiService;
import com.abs.app.data.remote.model.GeminiRequest;
import com.abs.app.data.remote.model.GeminiResponse;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltViewModel
public class AiAssistantViewModel extends ViewModel {

    private final GeminiApiService apiService;
    private final MutableLiveData<String> responseText = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    @Inject
    public AiAssistantViewModel(GeminiApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<String> getResponseText() {
        return responseText;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void queryAssistant(String userPrompt, String systemContext) {
        isLoading.setValue(true);
        String finalPrompt = "SYSTEM CONTEXT [You are a business assistant analyzer for the ABS System]:\n" + systemContext + "\n\nUSER PROMPT:\n" + userPrompt;
        
        GeminiRequest request = new GeminiRequest(finalPrompt);
        String apiKey = BuildConfig.GEMINI_API_KEY;
        
        apiService.generateContent(apiKey, request).enqueue(new Callback<GeminiResponse>() {
            @Override
            public void onResponse(Call<GeminiResponse> call, Response<GeminiResponse> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    responseText.setValue(response.body().getResponseText());
                } else {
                    responseText.setValue("Error analyzing context data. Response Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<GeminiResponse> call, Throwable t) {
                isLoading.setValue(false);
                responseText.setValue("Network Exception: " + t.getMessage());
            }
        });
    }
}
