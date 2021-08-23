package com.example.yourweather;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.drawable.Icon;
import android.location.Location;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText user_field ;
    private Button main_btn ;
    private TextView result_inf ;
    private CheckBox checkBox;
    private TextView result_inf2;
   // private Button map ;
   // private ImageView result_inf3;
    private TextView result_inf4;
    private TextView result_inf5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Метод чтобы ссылаться на элементы дизайна
        user_field = findViewById(R.id.user_field);
        main_btn = findViewById(R.id.main_btn);
        result_inf = findViewById(R.id.result_inf);
        checkBox = findViewById(R.id.checkBox);
        result_inf2 = findViewById(R.id.result_inf2);
        result_inf4 = findViewById(R.id.result_inf4);
        result_inf5 = findViewById(R.id.result_inf5);
        //map = findViewById(R.id.map);


        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION }, 123); // запрос разрешение на использовние геопозиции
        GPS g = new GPS(getApplicationContext()); //создаём трекер
        Location l = g.getLocation(); // получаем координаты
        if(l != null){
            double lat = l.getLatitude();  // широта
            double lon = l.getLongitude(); // долгота
            // Toast.makeText(getApplicationContext(), "Широта: "+lat+"\nДолгота: "+lon, Toast.LENGTH_LONG).show(); // вывод в тосте
            String key = "e1ba3adb00e1f0839dcb27d0a914d996";
            String url = "https://api.openweathermap.org/data/2.5/weather?&appid=" + key +"&lat=" + lat + "&lon=" + lon +"&units=metric&lang=ru";
            new GetURLData().execute(url);}

/*

//Create Array Adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_singlechoice, languages);
//Find TextView control AutoCompleteTextView acTextView = (AutoCompleteTextView) findViewById(R.id.languages);
//Set the number of characters the user must type before the drop down list is shown
        acTextView.setThreshold(1);
//Set the adapter acTextView.setAdapter(adapter);

*/





        // new View.OnClickListener() выделяет память и создает метод онклик
        main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user_field.getText().toString().trim().equals("")){
                    //Указываем страничку на которой всплывет виджет. Указываем строку с  текстом которы всплывет. длительность. ну и метод сшов чтобы оно показалось
                    Toast.makeText(MainActivity.this, R.string.no_input_user, Toast.LENGTH_LONG).show();}
                else{
                    String city= user_field.getText().toString();
                    String key = "e1ba3adb00e1f0839dcb27d0a914d996";
                    String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + key + "&units=metric&lang=ru";
                    //создаем объект  execute на основе GetURLData() и передаем в него юрл по которому будем обращаться
                    new GetURLData().execute(url);


                }
            }
        });

//Если погоду по местоположению кнопкой
      /*  ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION }, 123); // запрос разрешение на использовние геопозиции
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GPS g = new GPS(getApplicationContext()); //создаём трекер
                Location l = g.getLocation(); // получаем координаты
                if(l != null){
                    double lat = l.getLatitude();  // широта
                    double lon = l.getLongitude(); // долгота
                   // Toast.makeText(getApplicationContext(), "Широта: "+lat+"\nДолгота: "+lon, Toast.LENGTH_LONG).show(); // вывод в тосте
                    String key = "e1ba3adb00e1f0839dcb27d0a914d996";
                    String url = "https://api.openweathermap.org/data/2.5/weather?&appid=" + key +"&lat=" + lat + "&lon=" + lon +"&units=metric&lang=ru";
                    new GetURLData().execute(url);
                }
            }
        });*/


    }
//пока приложение работает. берем данные с юрл
        public class GetURLData extends AsyncTask<String,String,String> {
// срабатывает пока мы отправляемся по юрл
            protected  void onPreExecute(){

                super.onPreExecute();
                result_inf.setText("Ожидайте...");
                result_inf2.setText("");
                result_inf5.setText("");
                result_inf4.setText("");
            }

            @Override
            //метод из AsyncTask//в нем ссылаемся на юрл адрес сам и в нем же получаем инфу с адреса
            //(String... strings) получаем не ограниченое количество параметров которые запишутся массивом . первый элемент массива это юрл
            protected String doInBackground(String... strings) {

                HttpURLConnection connection = null;
                BufferedReader reader = null;

                try {
                    //создали объект на основе которого обращаемся к юрл
                    URL url = new URL(strings[0]);
                    //открываем соединение
                    connection =(HttpURLConnection) url.openConnection();
                    connection.connect();
                    // считываем поток
                    InputStream stream = connection.getInputStream();
                    //считываем в формате строки записываем в ридер
                    reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuffer buffer = new StringBuffer();
                    String line ="";
                    //считываем текст пока он есть
                    while((line=reader.readLine()) !=null)
                        buffer.append(line).append("\n");
                    return  buffer.toString();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //закрываем соединения
                finally {
                    if(connection != null)
                        connection.disconnect();
                     // закрываем ридер тоже
                    if(reader!=null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                return  null;
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @SuppressLint("SetTextI18n")
            @Override
            // метод вызываемый когда мы уже получили данные и показываем пользователю
           protected  void onPostExecute(String result){
                super.onPostExecute(result);

                try {
                    JSONObject jsonObject = new JSONObject(result);

                    result_inf.setText("Temperature: " + jsonObject.getJSONObject("main").getInt("temp") + " ℃");

                 //получение иконки???
                /*  String icon=(jsonObject.getJSONObject("weather").getString( "icon"));
                  String iconUrl = "https://openweathermap.org/img/w/" + icon + ".png";
                  iconUrl;
                  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        result_inf3.setImageIcon(Image);
                    } */



                    if (checkBox.isChecked()){
                 result_inf2.setText("Humidity: " + jsonObject.getJSONObject("main").getInt("humidity") + " %");
                 result_inf4.setText("Pressure: " + jsonObject.getJSONObject("main").getInt("pressure") + " mmHg");
                 result_inf5.setText("Wind: " + jsonObject.getJSONObject("wind").getInt("speed") + " m/s");
                 }else {result_inf2.setText("");
                    result_inf4.setText("");
                    result_inf5.setText("");}

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }




}
