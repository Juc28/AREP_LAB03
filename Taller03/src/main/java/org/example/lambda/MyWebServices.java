package org.example.lambda;

import java.io.IOException;
import java.net.URISyntaxException;

import org.example.Taller02.HttpServer;

public class MyWebServices {
    /**
     * Punto de entrada del programa.
     * Este es el punto de entrada del programa, donde se crean y registran los servicios web y se inicia el servidor HTTP.
     * En este ejemplo, se crean y registran dos servicios web en las rutas "/arep" y "/harry". El servidor HTTP se inicia y comienza a escuchar solicitudes en el puerto 35000.
     * @param args los argumentos de la línea de comandos.
     * @throws IOException si ocurre un error al iniciar el servidor HTTP.
     * @throws URISyntaxException si la URL de la solicitud no es válida.
     */

    public static void main(String[] args) throws IOException, URISyntaxException {
        HttpServer.get("/arep", (param) -> {
            String resp = "HTTP/1.1 200 OK\r\n"
                    + "Content-Type:text/html\r\n"
                    + "\r\n"
                    + "<h1> Welcome to AREP! </h1>";
            if (param != "") {
                resp += "<p> El parametro de su consulta fue: " + param + "</p>";
            }

            return resp;
        });


        HttpServer.get("/harry", (param) -> {
            String resp = "HTTP/1.1 200 OK\r\n"
                    + "Content-Type:text/html\r\n"
                    + "\r\n"
                    + "<h1> Harry es el mejor </h1>";

            if (param != "") {
                resp += "<p> El parametro de su consulta fue:  " + param + "</p>";
            }

            return resp;
        });


        HttpServer.getInstance().runstart();


    }
}
