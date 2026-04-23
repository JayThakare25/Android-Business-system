package com.abs.app.presentation.ai;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.abs.app.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AiAssistantBottomSheet extends BottomSheetDialogFragment {

    private AiAssistantViewModel viewModel;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_assistant, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        viewModel = new ViewModelProvider(this).get(AiAssistantViewModel.class);
        
        TextView tvResponse = view.findViewById(R.id.tv_ai_response);
        EditText etPrompt = view.findViewById(R.id.et_prompt);
        Button btnSend = view.findViewById(R.id.btn_send_prompt);
        ProgressBar progressBar = view.findViewById(R.id.progress_ai);
        
        viewModel.getResponseText().observe(getViewLifecycleOwner(), response -> {
            tvResponse.setText(response);
        });
        
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            btnSend.setEnabled(!isLoading);
        });
        
        btnSend.setOnClickListener(v -> {
            String prompt = etPrompt.getText().toString();
            if (!prompt.isEmpty()) {
                String mockContext = "The business had $1,050 revenue today. We processed 14 checkouts. Inventory levels are healthy except for SKUs: [Wireless Mouse (Stock: 2), USB-C Hub (Stock: 0)].";
                viewModel.queryAssistant(prompt, mockContext);
                etPrompt.setText("");
            }
        });
    }
}
