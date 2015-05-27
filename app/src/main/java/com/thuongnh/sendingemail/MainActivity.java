package com.thuongnh.sendingemail;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    EditText etMessage;
    Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etMessage = (EditText) findViewById(R.id.et_message);
        btnSend = (Button) findViewById(R.id.btn_send);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etMessage.getText().length() > 0) {
                    MailTask mailTask = new MailTask();
                    mailTask.execute();
                }
            }
        });
    }

    class MailTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            Mail mail = new Mail();
            mail.send(etMessage.getText().toString());
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            Toast.makeText(MainActivity.this, "Email sent!", Toast.LENGTH_LONG).show();
        }
    }

}
