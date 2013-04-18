/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package textconverter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
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

        ArrayList<ArrayList<CMPInfo>> cmptodos = new ArrayList<ArrayList<CMPInfo>>();

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
                    System.out.println("El documento "+archivo.getAbsolutePath()+" no existe");
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

            //System.out.println(sb.toString());
            guardarArchivo("tabla." + directorio + ".txt", sb.toString());

            //Añadimos la información al total
            todos.add(alginfos);


            //Ya que estamos en el directorio vamos a tratar otros archivos también

            //Definimos el conjunto de nombres de archivos
            ArrayList<String> archivos_cmp = new ArrayList<String>();

            archivos_cmp.add(directorio + "/cmp.eil76");
            archivos_cmp.add(directorio + "/cmp.kroA100");
            archivos_cmp.add(directorio + "/cmp.d198");
            archivos_cmp.add(directorio + "/cmp.lin318");
            archivos_cmp.add(directorio + "/cmp.att532");
            archivos_cmp.add(directorio + "/cmp.rat783");

            //Definimos la estructura en la que recopilaremos los datos
            ArrayList<CMPInfo> cmpinfos = new ArrayList<CMPInfo>();

            //Para cada archivo hacer

            for (String s_archivo_cmp : archivos_cmp) {

                File archivo_cmp = new File(s_archivo_cmp);

                //Si existe el archivo lo tratamos
                if (archivo_cmp.exists()) {

                    //Para cada archivo creamos una estructura que almacena los datos
                    CMPInfo info = new CMPInfo();
                    info.nombre = archivo_cmp.getName().substring(4);

                    //Leer archivo de entrada
                    FileReader fr = new FileReader(archivo_cmp);
                    BufferedReader bf = new BufferedReader(fr);

                    //Imprimir el documento línea a línea
                    String linea = bf.readLine();

                    //Definimos los patrones que vamos a utilizar
                    Pattern p_intento = Pattern.compile("^begin try (\\d+)");
                    Pattern p_semilla = Pattern.compile("^seed (\\d+)");
                    Pattern p_marca = Pattern.compile("^best[^\\d]*(\\d+)[^\\d]*iteration[^\\d]*(\\d+)[^\\d]*tours[^\\d]*(\\d+)[^\\d]*time[^\\d]*([\\d\\.]+)");

                    Intento intento = null;

                    while (linea != null) {


                        Matcher m_semilla = p_semilla.matcher(linea);

                        //No usamos el patrón intento porque la semilla ya nos indica que comienza uno nuevo
                        if (m_semilla.find()) {

                            double semilla = Double.parseDouble(m_semilla.group(1));

                            //Guardamos el anterior intento si es que había
                            if (intento != null) {
                                info.intentos.add(intento);
                            }
                            //Generamos un nuevo intento para seguir rellenándolo
                            intento = new Intento();
                            intento.semilla = semilla;
                        }


                        Matcher m_marca = p_marca.matcher(linea);

                        while (m_marca.find()) {
                            //Para cada marca
                            Marca marca = new Marca();
                            marca.mejor = Double.parseDouble(m_marca.group(1));
                            marca.iteracion = Double.parseDouble(m_marca.group(2));
                            marca.viajes = Double.parseDouble(m_marca.group(3));
                            marca.tiempo = Double.parseDouble(m_marca.group(4));

                            //Añadir la marca al intento
                            intento.marcas.add(marca);
                        }

                        //System.out.println(linea);
                        linea = bf.readLine();
                    }

                    //si queda algún intento lo guardamos
                    info.intentos.add(intento);

                    //Añadir AlgInfo a la lista
                    cmpinfos.add(info);

                } else {
                    System.out.println("El documento no existe");
                }
            }

            cmptodos.add(cmpinfos);

        }

        //Aquí ya tenemos toda la información necesaria para hacer las gráficas

        //Calcular los resultados medios para cada algoritmo y problema

        ArrayList<ArrayList<Intento>> resultados_medios = new ArrayList<ArrayList<Intento>>();

        int nalg = cmptodos.size();

        //Para cada algoritmo
        for (int alg = 0; alg < nalg; alg++) {

            ArrayList<Intento> intentos_problemas = new ArrayList<Intento>();

            //Número de problemas
            int np = cmptodos.get(alg).size();
            //Para cada problema
            for (int p = 0; p < np; p++) {
                //Resumir los intentos
                ArrayList<Intento> intentos = cmptodos.get(alg).get(p).intentos;
                int nintentos = intentos.size();
                //Muestrear de iteración en iteración y para cada una calcular el valor medio de todos los intentos en esa iteración

                //Muestrear por instantes de tiempo
                //Vamos saltando de cada instante al siguiente más próximo manteniendo los valores actuales del mejor para cada intento

                //Guardamos el valor inicial de cada intento, el de la primera ejecución
                double mv[] = new double[nintentos]; //Almacena el mejor valor actual de cada intento
                int iv[] = new int[nintentos]; //Almacena los índices de los intentos que pueden ser distintos

                for (int i = 0; i < nintentos; i++) {
                    mv[i] = intentos.get(i).marcas.get(0).mejor;
                    iv[i] = 1;
                }

                Intento intentomedio = new Intento();

                double momento_actual = 0;

                //Todo esto lo hacemos hasta que todos los índices hayan llegado a su límite
                boolean finalizado = false;


                while (!finalizado) {
                    //Añadimos el primer valor que es la media de los primeros mejores
                    //Calculamos la media de los mejores valores del momento

                    double media = 0;
                    for (int i = 0; i < nintentos; i++) {
                        media += mv[i];
                    }
                    media = media / (double) nintentos;

                    //Y ahora calculamos y añadimos el valor al intentomedio
                    Marca marcamedia = new Marca();
                    marcamedia.tiempo = momento_actual;
                    marcamedia.mejor = media;

                    intentomedio.marcas.add(marcamedia);



                    //Determinamos el siguiente menor valor de tiempo
                    momento_actual = Double.MAX_VALUE;
                    for (int i = 0; i < nintentos; i++) {
                        if (iv[i] < intentos.get(i).marcas.size()) {
                            //Consultamos el tiempo del siguiente muestreo para cada intento y cogemos el menor, digamos que vamos avanzando en el orden temporal
                            double momento = intentos.get(i).marcas.get(iv[i]).tiempo;

                            //Nos quedamos con el siguiente en orden cronológico
                            if (momento < momento_actual) {
                                momento_actual = momento;
                            }
                        }
                    }
                    //Aquí tenemos el siguiente momento en orden
                    //System.out.println("Momento actual: " + momento_actual);
                    //Ahora todos los intentos actualizan su valor al que tenían en ese momento
                    for (int i = 0; i < nintentos; i++) {
                        //Solo para los que no hayan terminado ya
                        if (iv[i] < intentos.get(i).marcas.size()) {
                            double momento = intentos.get(i).marcas.get(iv[i]).tiempo;
                            //Si la siguiente muestra está después del momento actual entonces se actualiza
                            if (momento_actual >= momento) {
                                mv[i] = intentos.get(i).marcas.get(iv[i]).mejor;
                                //Y avanzamos al siguiente si se puede

                                if (iv[i] < intentos.get(i).marcas.size()) {

                                    iv[i]++;
                                }
                            }
                        }
                    }

                    //Calcular si hemos llegado al límite en todos los índices
                    boolean fin = true;
                    for (int i = 0; i < nintentos; i++) {
                        if (iv[i] < intentos.get(i).marcas.size()) {
                            fin = false;
                        }
                    }

                    finalizado = fin;

                }

                //Añadimos el último
                double media = 0;
                for (int i = 0; i < nintentos; i++) {
                    media += mv[i];
                }
                media = media / (double) nintentos;

                //Y ahora calculamos y añadimos el valor al intentomedio
                Marca marcamedia = new Marca();
                marcamedia.tiempo = momento_actual;
                marcamedia.mejor = media;

                intentomedio.marcas.add(marcamedia);

                intentos_problemas.add(intentomedio);

            }

            resultados_medios.add(intentos_problemas);

        }


        //Aquí deberíamos tener todos los datos calculados para la gráfica

        //Para cada algoritmo
        for (int alg = 0; alg < nalg; alg++) {

            int np = resultados_medios.get(alg).size();

            //Para cada problema
            for (int p = 0; p < np; p++) {

                StringBuilder sb = new StringBuilder();

                //Imprimir tiempo, mejor_valor_medio

                //TODO imprimir resultado de todos los algoritmos para el mismo problema

                int N = resultados_medios.get(alg).get(p).marcas.size();

                //Cabecera
                sb.append(directorios.get(alg)).append(" - ").append(todos.get(alg).get(p).nombre).append("\n");
                sb.append("tiempo, mejor\n");

                for (int i = 0; i < N; i++) {
                    sb.append(resultados_medios.get(alg).get(p).marcas.get(i).tiempo);
                    sb.append(",");
                    sb.append(resultados_medios.get(alg).get(p).marcas.get(i).mejor);
                    sb.append("\n");
                }

                //Imprimir tablas por algoritmo-problema
                //System.out.println(sb.toString());

                sb.append("\n");

            }


        }


        //Recorrer los registros de menor a mayor tiempo y rellenar con los mismos valores los de enmedio
        //Para cada algoritmo





//TODO toquetear para imprimir una tabla por cada problema con una columna por cada algoritmo y una fila por cada instante de cambio---------------------------------------

        ArrayList<Intento> intentos_problemas = new ArrayList<Intento>();
//Número de problemas
        int np = resultados_medios.get(0).size();

        //Para cada problema
        for (int p = 0; p < np; p++) {

            //Una tabla por problema
            StringBuilder tp = new StringBuilder();
            
            //Recorrer los algoritmos
            for(int alg=0; alg<nalg; alg++){
                
            }
            
                //Cabecera
                    //tp.append(todos.get(0).get(p).nombre);
                    //tp.append("\n");
                    tp.append("names: tiempo");
                    
                    for (int alg = 0; alg < nalg; alg++) {
                        tp.append(" ").append(directorios.get(alg));
                    }
                    
                    tp.append(" optimo");
                    tp.append("\n");
                    
                    tp.append("title: Convergencia de los algoritmos\n");
                    tp.append("subtitle: para el problema ").append(todos.get(0).get(p).nombre).append("\n");
            

                //Una fila por instante de tiempo
                
                //Una columna por algoritmo

                //Muestrear por instantes de tiempo
                //Vamos saltando de cada instante al siguiente más próximo manteniendo los valores actuales del mejor para cada algoritmo

                //Guardamos el valor inicial de cada intento, el de la primera ejecución
                double mv[] = new double[nalg]; //Almacena el valor actual de cada algoritmo
                int iv[] = new int[nalg]; //Almacena los índices de los intentos_medios de los algoritmos

                //Inicializamos los vectores
                for (int alg = 0; alg < nalg; alg++) {
                    mv[alg] = resultados_medios.get(alg).get(p).marcas.get(0).mejor;
                    iv[alg] = 1;
                }


                double momento_actual = 0;

                //Todo esto lo hacemos hasta que todos los índices hayan llegado a su límite
                boolean finalizado = false;


                while (!finalizado) {
                    //Añadimos el primer valor que es la media de los primeros mejores
                    //Imprimimos los primeros valores en la primera fila

                    tp.append(momento_actual);
                    
                    for (int alg = 0; alg < nalg; alg++) {
                        tp.append(",").append(mv[alg]);
                    }
                    
                    //Añadir el óptimo del problema al final
                    tp.append(",").append(todos.get(0).get(p).optimo);
                    
                    tp.append("\n");

                    //Determinamos el siguiente menor valor de tiempo
                    momento_actual = Double.MAX_VALUE;
                    for (int alg = 0; alg < nalg; alg++) {
                        if (iv[alg] < resultados_medios.get(alg).get(p).marcas.size()) {
                            //Consultamos el tiempo del siguiente muestreo para cada intento y cogemos el menor, digamos que vamos avanzando en el orden temporal
                            double momento = resultados_medios.get(alg).get(p).marcas.get(iv[alg]).tiempo;

                            //Nos quedamos con el siguiente en orden cronológico
                            if (momento < momento_actual) {
                                momento_actual = momento;
                            }
                        }
                    }
                    //Aquí tenemos el siguiente momento en orden
                    //System.out.println("Momento actual: " + momento_actual);
                    //Ahora todos los intentos actualizan su valor al que tenían en ese momento
                    for (int alg = 0; alg < nalg; alg++) {
                        //Solo para los que no hayan terminado ya
                        if (iv[alg] < resultados_medios.get(alg).get(p).marcas.size()) {
                            double momento = resultados_medios.get(alg).get(p).marcas.get(iv[alg]).tiempo;
                            //Si la siguiente muestra está después del momento actual entonces se actualiza
                            if (momento_actual >= momento) {
                                mv[alg] = resultados_medios.get(alg).get(p).marcas.get(iv[alg]).mejor;
                                //Y avanzamos al siguiente

                                    iv[alg]++;
                            }
                        }
                    }

                    //Calcular si hemos llegado al límite en todos los índices
                    boolean fin = true;
                    for (int alg = 0; alg < nalg; alg++) {
                        if (iv[alg] < resultados_medios.get(alg).get(p).marcas.size()) {
                            fin = false;
                        }
                    }

                    finalizado = fin;

                }

                //Añadimos el último
                    tp.append(momento_actual);
                    
                    for (int alg = 0; alg < nalg; alg++) {
                        tp.append(",").append(mv[alg]);
                    }
                    
                    tp.append("\n");

                    
                    //System.out.println(tp.toString());
                    guardarArchivo("grafica." + todos.get(0).get(p).nombre + ".txt",tp.toString());
        }

        
        
//------------------------------------------------------------------------------------------------------------------------------------        

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


        //System.out.println(sb);
        guardarArchivo("tablaTODOS.txt",sb.toString());
    }

    
    /*
     * Guardar archivo con el nombre "nombre" y contenido "contenido"
     */
    private static void guardarArchivo(String nombre, String contenido) {
        BufferedWriter bw = null;
        try {
            File archivo = new File(nombre);
            bw = new BufferedWriter(new FileWriter(archivo));
            
            bw.write(contenido);
            
            System.out.println("Generado "+nombre);
            
        } catch (IOException ex) {
            Logger.getLogger(TextConverter.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                bw.close();
            } catch (IOException ex) {
                Logger.getLogger(TextConverter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
