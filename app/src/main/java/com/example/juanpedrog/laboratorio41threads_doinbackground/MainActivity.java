package com.example.juanpedrog.laboratorio41threads_doinbackground;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView numero;
    Button hilo1,hilo2;
    ProgressBar progress1,progress2;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        numero=findViewById(R.id.numero);
        hilo1=findViewById(R.id.hilo1);
        hilo2=findViewById(R.id.hilo2);
        progress1=findViewById(R.id.progress1);
        progress2=findViewById(R.id.progress2);
        progress1.setMin(0);
        progress2.setMin(0);

        hilo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTarea asyncTarea = new AsyncTarea();
                asyncTarea.execute();
            }
        });
        hilo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        progress2.setProgress(0);
                        int aux=Integer.parseInt(numero.getText().toString());
                        progress2.setMax(aux);
                        float por=100/aux;
                        try {
                            for (int i = 0; i <= aux; i++) {
                                Thread.sleep(1000);
                                progress2.setProgress(i);
                            }
                            //Toast.makeText(getApplicationContext(),"Hilo 2 finalizado",Toast.LENGTH_LONG).show();
                        }catch(InterruptedException e){}
                    }
                }).start();
            }
        });
    }
    private void UnSegundo() {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private class  AsyncTarea extends AsyncTask<Void, Integer,Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            int aux=Integer.parseInt(numero.getText().toString());
            progress1.setMax(aux);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            int aux=Integer.parseInt(numero.getText().toString());
            for (int i=1; i<=aux; i++){
                UnSegundo();
                publishProgress(i);

                if (isCancelled()){
                    break;
                }
            }
            return true;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            //Actualizar la barra de progreso
            progress1.setProgress(values[0].intValue());
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            //super.onPostExecute(aVoid);

            if (aVoid){
                Toast.makeText(getApplicationContext(),"Tarea finaliza AsyncTask",Toast.LENGTH_SHORT).show();
            }
        }


        @Override
        protected void onCancelled() {
            super.onCancelled();

            Toast.makeText(getApplicationContext(),"Tarea NO finaliza AsyncTask",Toast.LENGTH_SHORT).show();

        }


    }


}
