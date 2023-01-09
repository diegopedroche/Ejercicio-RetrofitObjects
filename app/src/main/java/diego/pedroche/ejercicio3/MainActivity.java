package diego.pedroche.ejercicio3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import diego.pedroche.ejercicio3.Adapters.PersonajesAdapter;
import diego.pedroche.ejercicio3.conexiones.ApiConexiones;
import diego.pedroche.ejercicio3.conexiones.RetrofitObject;
import diego.pedroche.ejercicio3.modelos.Personaje;
import diego.pedroche.ejercicio3.modelos.Respuesta;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PersonajesAdapter adapter;
    private RecyclerView.LayoutManager lm;

    private Respuesta respuesta;
    private List<Personaje> personajes;

    private Retrofit retrofit;
    private ApiConexiones apiConexiones;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.contenedor);
        personajes = new ArrayList<>();
        adapter = new PersonajesAdapter(personajes, R.layout.personaje_view_holder, this);
        lm = new GridLayoutManager(this, 2);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(lm);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (!recyclerView.canScrollVertically(1)) {
                    if (respuesta != null){
                        String page = respuesta.getNextPage().split("=")[1];
                        if (page != null && !page.isEmpty()){
                            cargarSiguientePagina(page);
                        }
                    }
                }
            }
        });

        retrofit = RetrofitObject.getConexion();
        apiConexiones = retrofit.create(ApiConexiones.class);

        cargaInicial();
    }

    private void cargarSiguientePagina(String page) {
        Call<Respuesta> getNextPage = apiConexiones.getPage(page);
        getNextPage.enqueue(new Callback<Respuesta>() {
            @Override
            public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {
                if (response.code() == HttpsURLConnection.HTTP_OK){
                    int tam = personajes.size();
                    respuesta = response.body();
                    personajes.addAll(respuesta.getData());
                    adapter.notifyItemRangeInserted(tam,respuesta.getCount());
                    Toast.makeText(MainActivity.this, "Cargada página: " + page, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Respuesta> call, Throwable t) {

            }
        });
    }

    private void cargaInicial() {
        Call<Respuesta> respuesta = apiConexiones.getPersonajes();

        respuesta.enqueue(new Callback<Respuesta>() {
            @Override
            public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {
                if (response.code() == HttpsURLConnection.HTTP_OK){
                    MainActivity.this.respuesta = response.body();
                    personajes.addAll(MainActivity.this.respuesta.getData());
                    adapter.notifyItemRangeInserted(0, MainActivity.this.respuesta.getCount());
                }
            }

            @Override
            public void onFailure(Call<Respuesta> call, Throwable t) {

            }
        });
    }
}