package com.example.activitylifecycle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static TextView Lifecycle_text, status_text;
    private Button dialog, startB, startC, finishA;
    public static String list = "", status = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        list = intent.getStringExtra("methodlist");
        status = intent.getStringExtra("statuslist");

        //绑定method
        Lifecycle_text = (TextView) findViewById(R.id.methodlist);
        Lifecycle_text.setMovementMethod(ScrollingMovementMethod.getInstance());
        list = list + "Activity A.onCreate()\n";
        if(list.substring(0,4).equals("null")) list = "Activity A.onCreate()\n";
        Lifecycle_text.setText(list);

        //绑定status
        status_text = (TextView) findViewById(R.id.statuslist);
        status_text.setMovementMethod(ScrollingMovementMethod.getInstance());

        status_text.setText(status);

        //绑定button
        dialog = (Button) findViewById(R.id.Dialog);
        dialog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                status += "Activity A:Paused\n";
                status_text.setText(status);
                list += "Activity A.onPause()\n";
                Lifecycle_text.setText(list);
                showNormalDialog();
            }
        });

        startB = (Button) findViewById(R.id.StartB);
        startB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list += "Activity A.onPause()\nActivity A.onStop()\n";
                status += "Activity A:Stopped\n";
                Intent intent = new Intent(MainActivity.this,activity_B.class);
                intent.putExtra("methodlist",list);
                intent.putExtra("statuslist",status);
                startActivity(intent);
                finish();
            }
        });

        startC = (Button) findViewById(R.id.StartC);
        startC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list += "Activity A.onPause()\nActivity A.onStop()\n";
                status += "Activity A:Stopped\n";
                Intent intent = new Intent(MainActivity.this,activity_C.class);
                intent.putExtra("methodlist",list);
                intent.putExtra("statuslist",status);
                startActivity(intent);
                finish();
            }
        });

        finishA = (Button) findViewById(R.id.FinishA);
        finishA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void showNormalDialog(){
        //创建dialog构造器
        AlertDialog.Builder normalDialog = new AlertDialog.Builder(this);
        //设置title
        normalDialog.setTitle("Simple Dialog");
        //设置按钮
        normalDialog.setPositiveButton("Close"
                , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        list += "Activity A.onResumed()\n";
                        Lifecycle_text.setText(list);
                        status += "Activity A:Resumed\n";
                        status_text.setText(status);
                        dialog.dismiss();
                    }
                });
        //创建并显示
        normalDialog.create().show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        list = list + "Activity A.onStart()\n";
        Lifecycle_text.setText(list);
    }

    @Override
    protected void onPause() {
        super.onPause();
        list = list + "Activity A.onPause()\n";
        Lifecycle_text.setText(list);
    }

    @Override
    protected void onResume() {
        super.onResume();
        list = list + "Activity A.onResume()\n";
        Lifecycle_text.setText(list);
        status += "Activity A:Resumed\n";
        if(status.substring(0,4).equals("null")) status = "Activity A:Resumed\n";
        status_text.setText(status);
    }

    @Override
    protected void onStop() {
        super.onStop();
        list = list + "Activity A.onStop()\n";
        Lifecycle_text.setText(list);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        list = list + "Activity A.onDestroy()\n";
        Lifecycle_text.setText(list);list = list + "Activity A.onDestroy()\n";
        status = status + "Activity A:onDestroy\n";
        status_text.setText(status);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        list = list + "Activity A.onRestart()\n";
        Lifecycle_text.setText(list);
    }
}
