package sr.unasat.country.rest.calls;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
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

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import sr.unasat.country.rest.calls.dto.CountryDto;

public class MainActivity extends AppCompatActivity  {

    private TextView mTextView;
    private TextView landTextView;
    private EditText landNaamEditText;
    private String ALL_COUNTRIES;
    private Spinner spinner;
    List<String> landen ;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.text);
        landTextView = (TextView) findViewById(R.id.landTextView);
        landNaamEditText = (EditText) findViewById(R.id.landNaamEditText);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

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
                    @Override
                    public void onResponse(String response) {
                        List<CountryDto> countryDto = mapJsonToCountryObject(response);

                        ArrayList<String> landen = new ArrayList<>();
                        for(CountryDto country: countryDto){
                            landen.add(country.getName());
                        }

                         //Creating adapter for spinner
                        ArrayAdapter<String> adapter =
                                new ArrayAdapter<String>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, landen);
                        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);

                        spinner.setAdapter(adapter);

                        String messageText = countryDto.toString().trim()
                                .replace("[Country:", "Land gegevens")
                                .replace("Name=", "Naam = ")
                                .replace("Capital=", "Hoofdstad = ")
                                .replace("Region=","Regio = ")
                                .replace("]", "")
                                .replace("--------------------------------","");

                        // dit zou eigenlijk dan in een combobox moeten komen als ik
                        //niet invoer en search dan moeten alle landen in die combobox comen
                        // als ik eentje select moet gegevens dan ervan tevoorschijn komen


                        landTextView.setText(messageText);
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


}