/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.itson.ajusteDeCurvas.entidades;

/**
 *
 * @author Fernando
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int elementos = 5; //N -> cantidad de puntos agregados
        double[][] tabulacion = {
            {0.4,1},
            {2.5,0.5},
            {4.3,2.0},
            {5,2.55},
            {6,4}
        }; //new double[elementos][2]; nxm
        
        //-------------------------------------
        int tipoGrafica = 4; //2: recta, 3: parabola, 4: cubica, 5:, 6:
        int numeroDeX = tipoGrafica + (tipoGrafica-1) - 1; //numero de x que se ocupan para el tipo de grafica
        //System.out.println(numeroDeX);
        double[][] xn = new double[elementos][numeroDeX]; //arreglo de x^n
        
        //llenar arreglo xn
        int exponente = 1;
        for (int j = 0; j < numeroDeX; j++) {
            for (int i = 0; i < elementos; i++) {
                //tabulacion
                double valor = Math.pow(tabulacion[i][0] , exponente);
                //System.out.println("valor:"+valor);
                //System.out.println("exponente:"+exponente);
                xn[i][j] = valor;
            }
            exponente++;
            
        }
        exponente = 1;
        
        //-------------------------------------
        int numeroDeMulti = tipoGrafica;
        double[][] xnPf = new double[elementos][numeroDeMulti];
        
        //llenar arreglo xnPf
        int columnaX = -1;
        for (int j = 0; j < numeroDeMulti; j++) {
            for (int i = 0; i < elementos; i++) {
                double valor;
                if (j == 0) {
                    valor = tabulacion[i][1];
                } else {
                    //se multiplica la columna segun la x que se ocupa por la segunda columna de la tabulacion
                    valor = xn[i][columnaX] * tabulacion[i][1];
                    
                    //System.out.println("columna:"+columnaX);
                }
                //System.out.println("valor:" + valor);
                xnPf[i][j] = valor;
            }
            columnaX++;
        }
        columnaX = -1;
        
        //hacer sumatorias
        double[] sumX = new double[numeroDeX]; //arreglo para guardar sumatorias de la matriz xn
        double[] sumXF = new double[numeroDeMulti]; //para guardar sumatorias de la matriz xnPf
        
        //sumX
        for (int j = 0; j < numeroDeX; j++) {
            for (int i = 0; i < elementos; i++) {
                sumX[j] = sumX[j] + xn[i][j];
            }
        }
        
        //sumXF
        for (int j = 0; j < numeroDeMulti; j++) {
            for (int i = 0; i < elementos; i++) {
                sumXF[j] = sumXF[j] + xnPf[i][j];
            }
        }
        
        //llenar matriz
        double[][] matriz = new double[tipoGrafica][tipoGrafica+1];
        int posicionX = 0, posicionXF = 0, iteracion = 0;
        for (int i = 0; i < tipoGrafica; i++) {
            for (int j = 0; j <= tipoGrafica ; j++) {
                if (i==0 && j==0) {
                    matriz[i][j] = elementos;
                }
                else if (j == (tipoGrafica)) {
                    matriz[i][j] = sumXF[posicionXF];
                    posicionXF++;
                }
                else {
                    matriz[i][j] = sumX[posicionX];
                    posicionX++;
                }
                //System.out.println(matriz[i][j]);
                
            }
            posicionX = 0 + iteracion;
            iteracion++;
        }
        posicionXF = 0;
        
        
        
        //imprimir xn
//        for (int i = 0; i < elementos; i++) {
//            for (int j = 0; j < numeroDeX; j++) {
//                System.out.println(xn[i][j]);
//            }
//        }
        //imprimir xnPf
//        for (int i = 0; i < elementos; i++) {
//            for (int j = 0; j < numeroDeMulti; j++) {
//                System.out.println(xnPf[i][j]);
//            }
//        }

        //imprimir sumX
//        for (int i = 0; i < sumX.length; i++) {
//            System.out.println(sumX[i]);
//        }
        
        //imprimir sumXF
//        for (int i = 0; i < sumXF.length; i++) {
//            System.out.println(sumXF[i]);
//        }

        for (int i = 0; i < tipoGrafica; i++) {
            for (int j = 0; j <= tipoGrafica; j++) {
                System.out.println(matriz[i][j]);
            }
        }
    }
    
}
