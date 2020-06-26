package dev.setephano.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.github.library.bubbleview.BubbleTextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class MainActivity extends AppCompatActivity {
    private static int SIGN_IN_REQUEST_CODE = 1;
    RelativeLayout activity_main;
    private FirebaseListAdapter<ChatMessage> adapter;
    EmojIconActions emojIconActions;
    ImageView emojiButton;
    EmojiconEditText emojiconEditText;
    ImageView submitButton;

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_sign_out) {
            AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                public void onComplete(@NonNull Task<Void> task) {
                    Snackbar.make((View) MainActivity.this.activity_main, (CharSequence) "You have been signed out.", -1).show();
                    MainActivity.this.finish();
                }
            });
        }
        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != SIGN_IN_REQUEST_CODE) {
            return;
        }
        if (resultCode == -1) {
            Snackbar.make((View) this.activity_main, (CharSequence) "Successfully signed in.Welcome!", -1).show();
            displayChatMessage();
            return;
        }
        Snackbar.make((View) this.activity_main, (CharSequence) "We couldn't sign you in.Please try again later", -1).show();
        finish();
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_main);
        this.activity_main = (RelativeLayout) findViewById(R.id.activity_main);
        this.emojiButton = (ImageView) findViewById(R.id.emoji_button);
        this.submitButton = (ImageView) findViewById(R.id.submit_button);
        this.emojiconEditText = (EmojiconEditText) findViewById(R.id.emojicon_edit_text);
        this.emojIconActions = new EmojIconActions(getApplicationContext(), this.activity_main, this.emojiButton, this.emojiconEditText);
        this.emojIconActions.ShowEmojicon();
        this.submitButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().push().setValue(new ChatMessage(MainActivity.this.emojiconEditText.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getEmail()));
                MainActivity.this.emojiconEditText.setText("");
                MainActivity.this.emojiconEditText.requestFocus();
            }
        });
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), SIGN_IN_REQUEST_CODE);
            return;
        }
        Snackbar.make((View) this.activity_main, (CharSequence) "Welcome " + FirebaseAuth.getInstance().getCurrentUser().getEmail(), -1).show();
        displayChatMessage();
    }

    private void displayChatMessage() {
        ListView listOfMessage = (ListView) findViewById(R.id.list_of_message);
        this.adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class, R.layout.list_item, FirebaseDatabase.getInstance().getReference()) {
            /* access modifiers changed from: protected */
            public void populateView(View v, ChatMessage model, int position) {
                TextView messageUser = (TextView) v.findViewById(R.id.message_user);
                TextView messageTime = (TextView) v.findViewById(R.id.message_time);
                ((BubbleTextView) v.findViewById(R.id.message_text)).setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));
            }
        };
        listOfMessage.setAdapter(this.adapter);
    }
}
