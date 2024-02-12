package org.example.Taller02;


import org.example.lambda.WebService;

import java.net.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class HttpServer {

    public static ServerSocket serverSocket;
    public static Map<String, WebService> service = new HashMap<>();
    private static HttpServer instance = new HttpServer();
    private static String filesDirectory;
    private HttpServer() {
    }

    /**
     * Obtiene la instancia única del servidor HTTP.
     *
     * @return la instancia única del servidor HTTP.
     */
    public static HttpServer getInstance() {
        return instance;
    }

    /**
     * Inicia el servidor HTTP y comienza a escuchar solicitudes en el puerto 35000.
     * @throws IOException si ocurre un error al crear el socket del servidor.
     * @throws URISyntaxException si la URL de la solicitud no es válida.
     */
    public void runstart() throws IOException, URISyntaxException {
        ServerSocket ServerSocket = null;
        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }

        boolean running = true;
        while (running) {
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()));
            String inputLine, outputLine = "";

            boolean firstLine = true;
            String uriStr = "";

            while ((inputLine = in.readLine()) != null) {
                if (firstLine) {
                    uriStr = inputLine.split(" ")[1];
                    firstLine = false;
                }
                System.out.println("Received: " + inputLine);
                if (!in.ready()) {
                    break;
                }
            }

            URI fileuri = new URI(uriStr);
            String path = fileuri.getPath();
            System.out.println("Path: " + path);

            String query = fileuri.getQuery();
            System.out.println("Query: " + query);
            String param = "";

            if (query != null) {
                String[] queryParam = query.split("&");
                for (String queryPart : queryParam) {
                    String[] keyValue = queryPart.split("=");
                    if (keyValue.length == 2) {
                        if (keyValue[0].equals("param")) {
                            param = keyValue[1];
                            break;
                        }
                    }
                }
            }
            out.println(outputLine);

            try {
                if (path.startsWith("/action")) {
                    String webURI = path.replace("/action", "");
                    if (service.containsKey(webURI)) {
                        outputLine = service.get(webURI).handle(param);
                    }
                } else if (path.startsWith("/archivos")) {
                    setStaticFilesDirectory("target/classes");
                    outputLine = handleStaticRequest(path, clientSocket);
                } else {
                    setStaticFilesDirectory("target/classes");
                    outputLine = handleStaticRequest(path, clientSocket);
                }
            } catch (IOException e) {
                outputLine = httpError();
            }
            out.println(outputLine);

            out.close();
            in.close();
            clientSocket.close();
        }
        ServerSocket.close();
    }

    /**
     *  Esta función maneja una solicitud estática de un cliente, que es una solicitud para un archivo estático como una página HTML,
     *  un archivo JavaScript, un archivo CSS o una imagen
     * @param path la ruta del archivo solicitado, incluyendo la extensión del archivo.
     * @param clientSocket el socket del cliente al que se enviará la respuesta HTTP.
     * @return  la respuesta HTTP como una cadena.
     * @throws IOException
     */
    public static String handleStaticRequest(String path, Socket clientSocket) throws IOException {
        String content_type = "";
        if (path.endsWith(".html")) {
            content_type = "text/html";
        } else if (path.endsWith(".js")) {
            content_type = "application/javascript";
        } else if (path.endsWith(".css")) {
            content_type = "text/css";
        } else if (path.endsWith(".jpg")) {
            content_type = "image/jpg";
        }

        String outputLine = "HTTP/1.1 200 OK\r\n"
                + "Content-Type:" + content_type + "\r\n"
                + "\r\n";


        Path file = Paths.get(filesDirectory + path);
        System.out.println(file);

        Charset charset;
        if (content_type.equals("text/html")) {
            charset = Charset.forName("UTF-8");
        } else if (content_type.equals("application/javascript")) {
            charset = Charset.forName("UTF-8");
        } else if (content_type.equals("text/css")) {
            charset = Charset.forName("UTF-8");
        } else {
            charset = Charset.defaultCharset();
        }


        if (Files.isRegularFile(file) && Files.size(file) > 0 ) {
            if (content_type.equals("image/jpg")) {
                byte[] imageData = Files.readAllBytes(file);
                OutputStream output = clientSocket.getOutputStream();
                output.write(outputLine.getBytes());
                output.write(imageData);
                output.flush();
                output.close();
            } else {
                BufferedReader reader = Files.newBufferedReader(file, charset);
                String line = null;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                    outputLine += line + "\r\n";
                }
                OutputStream output = clientSocket.getOutputStream();
                output.write(outputLine.getBytes());
                output.flush();
                output.close();
            }
        } else {
            outputLine = httpError();
        }

        return outputLine;

    }


    /**
     * Establece la ruta del directorio de archivos estáticos.
     * Esta función establece la ruta del directorio de archivos estáticos que se utilizará para servir archivos estáticos a los clientes.
     * Si el parámetro `directory` es nulo o está vacío, la función no realiza ninguna acción.
     * @param directory la ruta del directorio de archivos estáticos.
     */
    public static void setStaticFilesDirectory(String directory) {
        if (directory != null && !directory.isEmpty()) {
            filesDirectory = directory;
        }
    }

    /**
     * Registra un servicio web en la ruta especificada.
     * Esta función registra un servicio web en la ruta especificada, lo que significa que el servidor web enviará solicitudes HTTP a esa ruta al servicio web.
     * Si el parámetro `path` es nulo o está vacío, o si el parámetro `service` es nulo, la función no realiza ninguna acción.
     * @param path la ruta en la que se registrará el servicio web.
     * @param service el servicio web que se registrará en la ruta especificada.
     */

    public static void get(String path, WebService service) {
        if (path != null && !path.isEmpty() && service != null) {
            HttpServer.service.put(path, service);
        }
    }

    /**
     * Devuelve una respuesta HTTP de error.
     *
     * Esta función devuelve una respuesta HTTP de error con un código de estado 404 (No encontrado) y un cuerpo de respuesta adecuado.
     *
     * @return la respuesta HTTP de error como una cadena.
     */
    private static String httpError() {
        return "HTTP/1.1 404 Not Found\r\n"
                + "Content-Type:text/html\r\n"
                + "\r\n"
                + "<!DOCTYPE html>\n"
                + "<html>\n"
                + "    <head>\n"
                + "        <title>Requested File Not found</title>\n"
                + "        <meta charset=\"UTF-8\">\n"
                + "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                + "    </head>\n"
                + "    <body>\n"
                + "        <h1>Error, file not found</h1>\n"
                + "    </body>\n";
    }



}