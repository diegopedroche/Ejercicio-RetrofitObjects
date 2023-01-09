package diego.pedroche.ejercicio3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import javax.net.ssl.HttpsURLConnection;

import diego.pedroche.ejercicio3.conexiones.ApiConexiones;
import diego.pedroche.ejercicio3.conexiones.RetrofitObject;
import diego.pedroche.ejercicio3.modelos.Personaje;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerPersonajeActivity extends AppCompatActivity {

    private ImageView imgPersonaje;
    private TextView lbNombre;
    private TextView lbFilms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_personaje);

        lbNombre = findViewById(R.id.lbNombrePersonajeVer);
        lbFilms = findViewById(R.id.lbFilmsPersonajesVer);
        imgPersonaje = findViewById(R.id.imgPersonajeVer);

        if (getIntent().getExtras() != null && getIntent().getExtras().getString("ID") != null){
            ApiConexiones api = RetrofitObject.getConexion().create(ApiConexiones.class);
            Call<Personaje> doGetPersonaje = api.getPersonaje(getIntent().getExtras().getString("ID"));

            doGetPersonaje.enqueue(new Callback<Personaje>() {
                @Override
                public void onResponse(Call<Personaje> call, Response<Personaje> response) {
                    if (response.code() == HttpsURLConnection.HTTP_OK){
                        lbNombre.setText(response.body().getName());
                        lbFilms.setText("");
                        for (String film:response.body().getFilms()) {
                            lbFilms.setText(lbFilms.getText()+"\n"+film);
                        }
                        Picasso.get().load(response.body().getImageUrl()).placeholder(R.drawable.ic_launcher_foreground).error(R.drawable.ic_launcher_background).into(imgPersonaje);
                    }
                }

                @Override
                public void onFailure(Call<Personaje> call, Throwable t) {

                }
            });
        }
    }
}