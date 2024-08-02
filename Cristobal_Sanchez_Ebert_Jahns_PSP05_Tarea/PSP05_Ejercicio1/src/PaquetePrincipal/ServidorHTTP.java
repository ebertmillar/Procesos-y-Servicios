/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package PaquetePrincipal;

import java.io.BufferedReader;
import java.net.Socket;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Ebert
 */
public class ServidorHTTP {

    /**
     * **************************************************************************
     * procedimiento principal que asigna a cada petición entrante un socket
     * cliente, por donde se enviará la respuesta una vez procesada
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, Exception {

        //Asociamos al servidor el puerto 8066
        ServerSocket socServidor = new ServerSocket(8066);
        imprimeDisponible();
        Socket socCliente;

        //ante una petición entrante, procesa la petición por el socket cliente
        //por donde la recibe
        while (true) {
            //a la espera de peticiones
            socCliente = socServidor.accept();
            //atiendo un cliente
            System.out.println("Atendiendo al cliente ");
            procesaPeticion(socCliente);
            //cierra la conexión entrante
            socCliente.close();
            System.out.println("cliente atendido");
        }
    }

    /**
     *****************************************************************************
     * procesa la petición recibida
     *
     * @throws IOException
     */
    private static void procesaPeticion(Socket socketCliente) throws IOException {
        //variables locales
        String peticion;
        String html;

        //Flujo de entrada
        InputStreamReader inSR = new InputStreamReader(
                socketCliente.getInputStream());
        //espacio en memoria para la entrada de peticiones
        BufferedReader bufLeer = new BufferedReader(inSR);

        //objeto de java.io que entre otras características, permite escribir 
        //'línea a línea' en un flujo de salida
        PrintWriter printWriter = new PrintWriter(
                socketCliente.getOutputStream(), true);

        //Código para obtener la fecha y hora actuales
        Date fechaActual = new Date();
        SimpleDateFormat formatoFecha = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z");
        String fechaHTTP = formatoFecha.format(fechaActual);

        //mensaje petición cliente
        peticion = bufLeer.readLine();

        //para compactar la petición y facilitar así su análisis, suprimimos todos 
        //los espacios en blanco que contenga
        peticion = peticion.replaceAll(" ", "");

        //si realmente se trata de una petición 'GET' (que es la única que vamos a
        //implementar en nuestro Servidor)
        if (peticion.startsWith("GET")) {
            //extrae la subcadena entre 'GET' y 'HTTP/1.1'
            peticion = peticion.substring(3, peticion.lastIndexOf("HTTP"));

            //si corresponde a la página de inicio
            if (peticion.length() == 0 || peticion.equals("/")) {
                //sirve la página
                html = Paginas.html_index;
                printWriter.println(Mensajes.lineaInicial_OK);
                printWriter.println("Date: " + fechaHTTP);
                printWriter.println(Paginas.primeraCabecera);
                printWriter.println("Content-Length: " + html.length());
                printWriter.println("\n");
                printWriter.println(html);

                // Imprimir la cabecera en la consola
                System.out.println("Cabecera enviada al cliente:");
                System.out.println(Mensajes.lineaInicial_OK);
                System.out.println("Date: " + fechaHTTP);
                System.out.println(Paginas.primeraCabecera);
                System.out.println("Content-Length: " + html.length());
                System.out.println("\n");
                
            } //si corresponde a la página del Quijote
            else if (peticion.equals("/quijote")) {
                //sirve la página
                html = Paginas.html_quijote;
                printWriter.println(Mensajes.lineaInicial_OK);
                printWriter.println("Date: " + fechaHTTP);// Agregar la cabecera Date en la respuesta del servidor
                printWriter.println(Paginas.primeraCabecera);
                printWriter.println("Content-Length: " + html.length());
                printWriter.println("\n");
                printWriter.println(html);

                // Imprimir la cabecera en la consola
                System.out.println("Cabecera enviada al cliente:");
                System.out.println(Mensajes.lineaInicial_OK);
                System.out.println("Date: " + fechaHTTP);
                System.out.println(Paginas.primeraCabecera);
                System.out.println("Content-Length: " + html.length());
                System.out.println("\n");
                
            } //en cualquier otro caso
            else {
                //sirve la página
                html = Paginas.html_noEncontrado;
                printWriter.println(Mensajes.lineaInicial_NotFound);
                printWriter.println("Date: " + fechaHTTP); // Agregar la cabecera Date en la respuesta del servidor
                printWriter.println(Paginas.primeraCabecera);
                printWriter.println("Content-Length: " + html.length());
                printWriter.println("\n");
                printWriter.println(html);

                // Imprimir la cabecera en la consola
                System.out.println("Cabecera enviada al cliente:");
                System.out.println(Mensajes.lineaInicial_OK);
                System.out.println("Date: " + fechaHTTP);
                System.out.println(Paginas.primeraCabecera);
                System.out.println("Content-Length: " + html.length());
                System.out.println("\n");

            }

        }
    }

    /**
     * **************************************************************************
     * muestra un mensaje en la Salida que confirma el arranque, y da algunas
     * indicaciones posteriores
     */
    private static void imprimeDisponible() {

        System.out.println("El Servidor WEB se está ejecutando y permanece a la "
                + "escucha por el puerto 8066.\nEscribe en la barra de direcciones "
                + "de tu explorador preferido:\n\nhttp://localhost:8066\npara "
                + "solicitar la página de bienvenida\n\nhttp://localhost:8066/"
                + "quijote\n para solicitar una página del Quijote,\n\nhttp://"
                + "localhost:8066/q\n para simular un error");
    }

}