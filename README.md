# LABORATORIO 03 DE AREP - MICROFRAMEWORKS WEB
Sparkweb es un microframework web que le permite crear fácilmente aplicaciones web  utilizando funciones Lambda.
En este taller, exploraremos la arquitectura de Sparkweb y crearemos un servidor web que admita funciones similares a  Spark.
En este ejercicio, implementará un servidor web que puede registrar servicios GET y POST utilizando funciones Lambda.
También implementa una funcionalidad que le permite configurar un directorio para archivos estáticos.
Es importante tener en cuenta que este servidor está construido utilizando  API  de Java básicas, sin utilizar ningún marco como Spark o Spring.
El objetivo es comprender la arquitectura de Sparkweb y aprender a crear un servidor web simple que pueda manejar diferentes tipos de solicitudes y respuestas, servir archivos estáticos y configurar directorios para estos archivos.

# Instalación 
## Herramientas 
- [MAVEN](https://maven.apache.org) : Para el manejo de las dependecias. 
- [GIT](https://git-scm.com) : Para el manejo de las versiones.
- [JAVA](https://www.java.com/es/) : Lenguaje de programación manejado. 
+ Para poder correr el laboratorio se clona el repositorio en una máquina local con el siguiente comando
  
    ```
  git clone https://github.com/Juc28/AREP_LAB03.git
    ```

+ luego en el ide de preferencia en mi caso Intellij abrilo de la siguiente forma:
![image](https://github.com/Juc28/AREP_LAB03/assets/118181224/a959001f-0bbc-4f06-904b-609021cc8f8c)
+ Para ejecutar el laboratrio es la clase [MyWebServices](https://github.com/Juc28/AREP_LAB03/blob/master/Taller03/src/main/java/org/example/lambda/MyWebServices.java) y desde el ide de prefencia correr la clase mencionada, Luego, ejecute la clase y abra su navegador de confianza(En mi caso use [Google](https://www.google.com/?hl=es)). En la barra de direcciones colocar:
   ```
   http://localhost:35000/action/arep
    ```
   
# Arquitectura
El paquete org.example.lambda tiene dos clases y una interfaz.
 *  MyWebServices: es la clase principal que contiene el método principal que inicia el servidor web.
   Este método crea dos servicios web y los registra en las rutas /arep y /harry.
*  WebService: es una interfaz que define el método Handle que todos los servicios web deben implementar.
  Este método toma un parámetro de tipo Cadena y devuelve una Cadena que contiene la respuesta HTTP.

La clase HttpServer se encuentra en el paquete org.example.Taller02 y es responsable de iniciar el servidor web y manejar las solicitudes HTTP.
 Esta clase tiene un método Runstart que crea un ServerSocket en el puerto especificado y escucha las solicitudes HTTP.
 Cuando llega una solicitud, el servidor extrae la ruta y los parámetros de la solicitud y los envía al servicio web apropiado (si corresponde).
 Si no hay ningún servicio web registrado en la ruta, el servidor devuelve una respuesta  de error HTTP 404.
 
 Además, la clase HttpServer tiene un método Get estático que le permite registrar un servicio web en una ruta específica.
 Este método requiere dos parámetros. Una ruta y un servicio web que se registra con esa ruta.Si la ruta o el servicio web es nulo o está vacío, el método no hace nada.
 
 Esta clase también tiene un método handleStaticRequest para manejar solicitudes de archivos estáticos como HTML, CSS, JavaScript e imágenes.
 Este método requiere dos parámetros:  ruta del archivo  y  socket del cliente.
 Este método crea una respuesta HTTP con el tipo de contenido apropiado y envía el archivo al cliente a través del socket.
 Si el archivo no existe o es un directorio, el método devuelve una respuesta HTTP de error 404.
 Finalmente, la clase HttpServer tiene un método setStaticFilesDirectory que establece la ruta del directorio de archivos estáticos utilizada para servir archivos estáticos   a los clientes.Si el parámetro del directorio es nulo o está vacío, la función no hace nada.


# Pruebas
## Pruebas Windows 
## - 3 rutas para el get y la lectura de parámetros del query:  
http://localhost:35000/action/arep

![image](https://github.com/Juc28/AREP_LAB03/assets/118181224/f90dcf33-14f7-4628-8487-7c1198b62b7d)

http://localhost:35000/action/arep?param=Es%20:0 

![image](https://github.com/Juc28/AREP_LAB03/assets/118181224/fab5fd0f-1735-4324-bb96-bce4daf32e10)
http://localhost:35000/action/harry
<img  alt="image" src="https://github.com/Juc28/AREP_LAB03/assets/118181224/31e01f58-aac5-46c6-bc20-765704309920">
http://localhost:35000/action/harry?param=Es%20precioso
<img  alt="image" src="https://github.com/Juc28/AREP_LAB03/assets/118181224/43e74668-a909-494b-a823-6854db5674cc">

## - los archivos estáticos traidos del directorio : 
 - Al cargar un imagen:

 http://localhost:35000/archivos/Tokyo.jpg
 
<img  alt="image" src="https://github.com/Juc28/AREP_LAB03/assets/118181224/bb072991-431b-41ba-9552-dae311eae2f2">
 - Al cargar un archivo html:

   http://localhost:35000/archivos/hello.html
  
<img  alt="image" src="https://github.com/Juc28/AREP_LAB03/assets/118181224/e9a3b16e-bd7f-4421-aaef-b02c9c616c8e">
 - Al cargar un archivo css:

http://localhost:35000/archivos/harry.css


<img  alt="image" src="https://github.com/Juc28/AREP_LAB03/assets/118181224/7d90c944-0227-4ade-ba5e-ecb50a747af8">
 - Al cargar un archivo js:

http://localhost:35000/archivos/harry.js

<img  alt="image" src="https://github.com/Juc28/AREP_LAB03/assets/118181224/9a1d52b9-34a0-4370-beb5-fce510a1ba37">

## Pruebas Linux
Se utlizo para probar una maquina virtual de Ubuntu.

- El get y la lectura de parámetros del query:
  
http://localhost:35000/action/harry

![image](https://github.com/Juc28/AREP_LAB03/assets/118181224/7327c4dd-47fa-4976-a206-f0aa73f357d9)
http://localhost:35000/action/harry?param=Es%20precioso

![image](https://github.com/Juc28/AREP_LAB03/assets/118181224/71e6421a-6139-48b8-b423-d249fb9f2722)

 - Al cargar una imagen:


http://localhost:35000/archivos/Styles.jpg

![image](https://github.com/Juc28/AREP_LAB03/assets/118181224/d44e7871-8e4f-42e7-acd2-6351a56f5330)

 - Al cargar un archivo html:

http://localhost:35000/archivos/hello.html

![image](https://github.com/Juc28/AREP_LAB03/assets/118181224/136f8730-49dc-46cd-a403-f1d410424fb3)

 - Al cargar un archivo css:

http://localhost:35000/archivos/harry.css

![image](https://github.com/Juc28/AREP_LAB03/assets/118181224/57f765c1-9cae-4176-85bc-bb8b959a423a)

 - Al cargar un archivo js:
   
http://localhost:35000/archivos/harry.js

![image](https://github.com/Juc28/AREP_LAB03/assets/118181224/cf7c0114-6405-49ff-bf86-6c5ee9922b8b)


# Lincencia
Licenciado por GNU General Public License v3.0 [LICENSE](https://github.com/Juc28/AREP_LAB01/blob/master/LICENSE)

# Autor 
Erika Juliana Castro Romero [Juc28](https://github.com/Juc28)
