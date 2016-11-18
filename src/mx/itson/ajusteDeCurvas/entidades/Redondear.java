/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.itson.ajusteDeCurvas.entidades;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author Fernando
 */
public class Redondear {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        double valor = 1254.62529202020;
        
        double r = redondearDecimales(valor, 4);
        System.out.println(r);
    }
    
    public static double redondearDecimales(double valor, int decimales) {
        double resultado = valor;
        BigDecimal big = new BigDecimal(resultado);
        big = big.setScale(decimales, RoundingMode.HALF_UP);
        resultado = Double.parseDouble(big.toString());
        
        return resultado;
    }
    
}
