package sr.unasat.country.rest.calls;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import sr.unasat.country.rest.calls.dto.CountryDto;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TextView mTextView;
    private TextView landTextView;
    private EditText landNaamEditText;
    private String ALL_COUNTRIES;
    private Spinner spinner;
    private String messageText;
    private List<CountryDto> countryDto;
    private TextView land;
    private TextView hoofdstad;
    private TextView regio;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.text);
        landTextView = (TextView) findViewById(R.id.landTextView);
        landNaamEditText = (EditText) findViewById(R.id.landNaamEditText);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
         land = (TextView) findViewById(R.id.landTextView);
         hoofdstad = (TextView) findViewById(R.id.HoofdstadTextView);
         regio = (TextView) findViewById(R.id.RegioTextView);


    }

    public void onSearchCountry(View view) {
        if (! landNaamEditText.getText().toString().isEmpty()){
            ALL_COUNTRIES = "https://restcountries.eu/rest/v2/name/" + landNaamEditText.getText().toString();
        } else {
            ALL_COUNTRIES = "https://restcountries.eu/rest/v2/all";
        }
        getCountryData(ALL_COUNTRIES);
    }

    private void getCountryData(String ALL_COUNTRIES ) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
       // final String ALL_COUNTRIES = "https://restcountries.eu/rest/v2/all";
      //  countryName = landNaamEditText.getText().toString();
      //   final String ONE_COUNTRY = "https://restcountries.eu/rest/v2/name/" + countryName;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ALL_COUNTRIES ,
                new Response.Listener<String>() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void onResponse(String response) {

                       countryDto = mapJsonToCountryObject(response);
                         //Creating adapter for spinner
                        ArrayAdapter<CountryDto> adapter =
                                new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, countryDto);
                        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);

                        if (countryDto.size() == 1){
                            land.setText(countryDto.get(0).getName());
                            hoofdstad.setText(countryDto.get(0).getCapital());
                            regio.setText(countryDto.get(0).getRegion());
                        }

                         messageText = countryDto.toString().trim()
                                .replace("[Country:", "Land gegevens")
                                .replace("Name=", "Naam = ")
                                .replace("Capital=", "Hoofdstad = ")
                                .replace("Region=","Regio = ")
                                .replace("]", "")
                                .replace("--------------------------------","");



                        // dit zou eigenlijk dan in een combobox moeten komen als ik
                        //niet invoer en search dan moeten alle landen in die combobox comen
                        // als ik eentje select moet gegevens dan ervan tevoorschijn komen


                        spinner.setAdapter(adapter);
                        System.out.println(countryDto);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mTextView.setText("something went wrong");
                System.out.println("something went wrong" + error);

            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    private List<CountryDto> mapJsonToCountryObject(String jsonArray) {
        ObjectMapper mapper = new ObjectMapper();
        List<CountryDto> countryList = new ArrayList<>();
        List<Map<String, ?>> countryArray = null;
        CountryDto country = null;

        try {
            countryArray = mapper.readValue(jsonArray, List.class);
            for (Map<String, ?> map : countryArray) {
                country = new CountryDto((String) map.get("name"), (String) map.get("capital"), (String) map.get("region"));
                countryList.add(country);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Er is wat fout gegaan bij het parsen van de json data");
        }
        return countryList;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        CountryDto c = (CountryDto) spinner.getSelectedItem();
        land.setText(c.getName());
        hoofdstad.setText(c.getCapital());
        regio.setText(c.getRegion());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        mTextView.setText("Selecteer a.u.b een item uit de lijst");
    }
}