package com.example.dotlun.jsonemployee;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    ArrayList<Employee> emPloyees = new ArrayList<>();
    private ProgressDialog pDialog;
    private ListView lv;
    private static String url = "http://nks.vncloud.webstarterz.com/employees.php";

    ArrayList<HashMap<String, String>> employeeList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = (ListView)findViewById(R.id.listView);
        employeeList = new ArrayList<>();

        new GetEmployees().execute();


    }
    private class GetEmployees extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please Wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();

            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG,"Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONObject glossary = jsonObj.getJSONObject("glossary");
                    JSONObject list = glossary.getJSONObject("list");

                    //Employee
                    JSONObject emp1 = list.getJSONObject("emp1");
                    JSONObject emp2 = list.getJSONObject("emp2");
                    JSONObject emp3 = list.getJSONObject("emp3");
                    //Emp1
                    String id = emp1.getString("id");
                    String name = emp1.getString("name");
                    String gender = emp1.getString("gender");

                    //Emp2
                    String id2 = emp2.getString("id");
                    String name2 = emp2.getString("name");
                    String gender2 = emp2.getString("gender");
                    //Emp3
                    String id3 = emp3.getString("id");
                    String name3 = emp3.getString("name");
                    String gender3 = emp3.getString("gender");



                    emPloyees.add(new Employee(
                            id, name,gender
                    ));
                    emPloyees.add(new Employee(
                            id2, name2,gender2
                    ));
                    emPloyees.add(new Employee(
                            id3, name3,gender3
                    ));

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (pDialog.isShowing())
                pDialog.dismiss();
            String[] from = new String[] {"id","name", "gender"};
            int[] to = new int[] { R.id.id,R.id.name, R.id.gender};

           for (Employee employe : emPloyees) {
               HashMap<String, String> url_maps = new HashMap<String, String>();
                url_maps.put("id",employe.getId());
                url_maps.put("name",employe.getName());
                url_maps.put("gender",employe.getGender());
               employeeList.add(url_maps);
            }
            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, employeeList,
                    R.layout.list_item, from, to );
            lv.setAdapter(adapter);
        }

    }
}