/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package textconverter;

import java.util.ArrayList;

/**
 *
 * @author Víctor
 */
class AlgInfo {
    
    //Nombre del algoritmo
    String nombre;
    //Resultados
    ArrayList<Resultado> resultados = new ArrayList<Resultado>();
    
    //Media
    double media;
    
    //Desviación típica
    double desv;
    
    //Mejor resultado
    double mejor;
    
    //Óptimo
    double optimo;
    
    
    
    public String toString(){
        StringBuilder sb = new StringBuilder();
        
        sb.append("Nombre: ").append(nombre).append(",");
        sb.append("Media: ").append(media).append(",");
        sb.append("Desv: ").append(desv).append(",");
        sb.append("Mejor: ").append(mejor).append(",");
        sb.append("Optimo: ").append(optimo).append(",");
        sb.append(",[");
        
        for(Resultado r : resultados){
            
            sb.append(r.valor).append(",");
            
        }
        
        sb.append("]");
        
        
        
        return sb.toString();
    }
    
}
