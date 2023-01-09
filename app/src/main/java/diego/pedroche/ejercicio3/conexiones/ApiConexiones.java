package diego.pedroche.ejercicio3.conexiones;

import diego.pedroche.ejercicio3.modelos.Personaje;
import diego.pedroche.ejercicio3.modelos.Respuesta;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiConexiones {

    // Obtener los datos iniciales
    @GET("/characters")
    Call<Respuesta> getPersonajes();
    // Obtener un personaje
    @GET("/characters/{id}")
    Call<Personaje> getPersonaje(@Path("id") String id);
    // Obtener una página en concreto
    @GET("/characters?")
    Call<Respuesta> getPage(@Query("page") String page);
}
