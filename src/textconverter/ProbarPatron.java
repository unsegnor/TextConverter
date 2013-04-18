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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Víctor
 */
public class ProbarPatron {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        String resultados = pruebaPatron("cmp.rat783", "^best[^\\d]*(\\d+)[^\\d]*iteration[^\\d]*(\\d+)[^\\d]*tours[^\\d]*(\\d+)[^\\d]*time[^\\d]*([\\d\\.]+)");
        
        System.out.println(resultados);
        
    }

    public static String pruebaPatron(String s_archivo, String patron) {
        StringBuilder sb = new StringBuilder();

        //Archivo de entrada
        File archivo = new File(s_archivo);

        if (archivo.exists()) {
            FileReader fr = null;
            try {
                fr = new FileReader(archivo);
                BufferedReader bf = new BufferedReader(fr);
                //Imprimir el documento línea a línea
                String linea = bf.readLine();
                //Definimos los patrones que vamos a utilizar
                Pattern pat = Pattern.compile(patron);
                while (linea != null) {

                    Matcher m = pat.matcher(linea);

                    //Mientras encontremos matcheos
                    while (m.find()) {

                        //Obtener el número de grupos matcheados
                        int ngrupos = m.groupCount();

                        //Añadimos el primer grupo
                        sb.append("(0)").append(m.group(0));
                        for (int i = 1; i <= ngrupos; i++) {
                            //Y todos los demás separados por comas
                            sb.append(", (").append(i).append(")").append(m.group(i));
                        }
                        sb.append("\n");
                    }
                    //Leemos la siguiente línea
                    linea = bf.readLine();
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ProbarPatron.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ProbarPatron.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    fr.close();
                } catch (IOException ex) {
                    Logger.getLogger(ProbarPatron.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        } else {
            System.out.println("El documento no existe");
        }

        return sb.toString();
    }
}
