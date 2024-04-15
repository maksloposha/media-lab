package com.example.mediaandroid;

// AddOnlineAudioActivity.java
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddOnlineAudioActivity extends AppCompatActivity {

    EditText editTextTitle;
    EditText editTextUrl;
    Button buttonAdd;
    ImageButton buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_online_audio);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextUrl = findViewById(R.id.editTextUrl);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonBack = findViewById(R.id.buttonBack);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTextTitle.getText().toString().trim();
                String url = editTextUrl.getText().toString().trim();
                addOnlineAudio(title, url);
            }
        });
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Finish this activity to return to MainActivity
            }
        });
    }

    private void addOnlineAudio(String title, String url) {
        // Validate inputs
        if (title.isEmpty() || url.isEmpty()) {
            Toast.makeText(this, "Title or URL cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the URL is valid (You can add more robust URL validation)
        if (!android.util.Patterns.WEB_URL.matcher(url).matches()) {
            Toast.makeText(this, "Invalid URL", Toast.LENGTH_SHORT).show();
            return;
        }

        // Return the added online audio back to the MainActivity
        Intent resultIntent = new Intent();
        resultIntent.putExtra("title", title);
        resultIntent.putExtra("url", url);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}

