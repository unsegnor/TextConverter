/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package textconverter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Víctor
 */
public class TextConverter {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        // TODO code application logic here

        //Conjunto de directorios
        ArrayList<String> directorios = new ArrayList<String>();

        directorios.add("SH");
        directorios.add("SCH");
        directorios.add("SHMM");
        directorios.add("SHMP");
        directorios.add("SH-BL");
        directorios.add("SCH-BL");
        directorios.add("SHMM-BL");
        directorios.add("SHMP-BL");

        //Almacenamos los resultados por cada directorio
        ArrayList<ArrayList<AlgInfo>> todos = new ArrayList<ArrayList<AlgInfo>>();

        //Para cada directorio
        for (String directorio : directorios) {

            //Definimos el conjunto de nombres de archivos
            ArrayList<String> archivos = new ArrayList<String>();

            archivos.add(directorio + "/best.eil76");
            archivos.add(directorio + "/best.kroA100");
            archivos.add(directorio + "/best.d198");
            archivos.add(directorio + "/best.lin318");
            archivos.add(directorio + "/best.att532");        
            archivos.add(directorio + "/best.rat783");

            //Definimos la estructura en la que recopilaremos los datos
            ArrayList<AlgInfo> alginfos = new ArrayList<AlgInfo>();

            //Para cada archivo hacer

            for (String s_archivo : archivos) {

                File archivo = new File(s_archivo);

                //Si existe el archivo lo tratamos
                if (archivo.exists()) {

                    //Para cada archivo creamos una estructura que almacena los datos
                    AlgInfo info = new AlgInfo();
                    info.nombre = archivo.getName().substring(5);

                    //Leer archivo de entrada
                    FileReader fr = new FileReader(archivo);
                    BufferedReader bf = new BufferedReader(fr);

                    //Imprimir el documento línea a línea
                    String linea = bf.readLine();

                    //Definimos los patrones que vamos a utilizar
                    Pattern resultados = Pattern.compile("^Best:[^\\d]*(\\d+)");
                    Pattern media = Pattern.compile("^Average-Best:[^\\d]*(\\d+)");
                    Pattern desviaciontipica = Pattern.compile("^Stddev-Best:[^\\d]*(\\d+)");
                    Pattern optimo = Pattern.compile("^optimum[^\\d]*(\\d+)");

                    double maxresult = 0;

                    while (linea != null) {

                        Matcher m_resultados = resultados.matcher(linea);



                        while (m_resultados.find()) {
                            //Para cada matching que haga hacer...

                            //Rellenar la información del algoritmo
                            String m1 = m_resultados.group(1);
                            double d = Double.parseDouble(m1);
                            if (d > maxresult) {
                                maxresult = d;
                            }
                            info.resultados.add(new Resultado(d));
                        }

                        //Definir el máximo
                        info.mejor = maxresult;


                        Matcher m_media = media.matcher(linea);

                        while (m_media.find()) {
                            String s = m_media.group(1);
                            double med = Double.parseDouble(s);
                            info.media = med;
                        }

                        Matcher m_desv = desviaciontipica.matcher(linea);

                        while (m_desv.find()) {
                            String s = m_desv.group(1);
                            double desv = Double.parseDouble(s);
                            info.desv = desv;
                        }

                        Matcher m_optimo = optimo.matcher(linea);

                        while (m_optimo.find()) {
                            String s = m_optimo.group(1);
                            double optim = Double.parseDouble(s);
                            info.optimo = optim;
                        }

                        //System.out.println(linea);
                        linea = bf.readLine();
                    }

                    //Añadir AlgInfo a la lista
                    alginfos.add(info);

                } else {
                    System.out.println("El documento no existe");
                }
            }

            //Imprimir alginfos

            for (AlgInfo info : alginfos) {
                //    System.out.println(info);
            }
            int N = alginfos.size();
            int nejecuciones = 5;

            //Imprimir tabla de ejecuciones en formato CSV
            StringBuilder sb = new StringBuilder();

            //Añadimos la primera columna vacía
            //sb.append(",");
            //Indicamos el algoritmo
            sb.append(directorio);

            //Ahora una columna por cada problema
            for (int i = 0; i < N; i++) {
                sb.append(",").append(alginfos.get(i).nombre);
            }

            //Siguiente línea
            sb.append("\n");

            //Para cada ejecución 
            for (int e = 0; e < nejecuciones; e++) {
                sb.append("Ejecución ").append(e);
                for (int i = 0; i < N; i++) {
                    sb.append(",").append(alginfos.get(i).resultados.get(e).valor);
                }
                sb.append("\n");
            }

            //Y lo mismo para la media
            sb.append("Media ");
            for (int i = 0; i < N; i++) {
                sb.append(",").append(alginfos.get(i).media);
            }
            sb.append("\n");

            //Y para la desviación típica
            sb.append("Desv ");
            for (int i = 0; i < N; i++) {
                sb.append(",").append(alginfos.get(i).desv);
            }
            sb.append("\n");

            System.out.println(sb.toString());

            //Añadimos la información al total
            todos.add(alginfos);
        }

        //Aquí ya tenemos los datos para hacer las tablas comparativas de todos los algoritmos

        int N = todos.get(0).size();
        StringBuilder sb = new StringBuilder();

        //Primera fila
        //Añadimos para cada problema 3 columnas
        for (int i = 0; i < N; i++) {
            for (int c = 0; c < 3; c++) {
                sb.append(",").append(todos.get(0).get(i).nombre);
            }
        }

        sb.append("\n");

        for (int i = 0; i < N; i++) {
            for (int c = 0; c < 3; c++) {
                sb.append(",").append(todos.get(0).get(i).optimo);
            }
        }

        sb.append("\n");

        sb.append("Modelo");

        for (int i = 0; i < N; i++) {
            sb.append(",").append("Med");
            sb.append(",").append("Mej");
            sb.append(",").append("Desv");
        }

        sb.append("\n");
        
        //Para cada algoritmo
        for (int k = 0; k < directorios.size(); k++) {
            sb.append(directorios.get(k));

            //Para cada problema
            for (int i = 0; i < N; i++) {
                sb.append(",").append(todos.get(k).get(i).media);
                sb.append(",").append(todos.get(k).get(i).mejor);
                sb.append(",").append(todos.get(k).get(i).desv);
            }
            
            sb.append("\n");
        }


        System.out.println(sb);
    }
}
