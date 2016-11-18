/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.itson.ajusteDeCurvas.entidades;

import mx.itson.ajusteDeCurvas.JfreeChart.FormularioChart;
import mx.itson.ajusteDeCurvas.JfreeChart.GraficasLineal;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

/**
 *
 * @author Fernando
 */
public class InterfazAjusteDeCurvas extends javax.swing.JFrame {

    /**
     * Creates new form InterfazAjusteDeCurva
     */
    public InterfazAjusteDeCurvas() {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setSize(755, 500);
    }
    
    char[] letras = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'};
    String[] equis = {"","x","x^2","x^3","x^4","x^5","x^6","x^7","x^8","x^9","x^10"};
    //variables para la tabulacion
    int elementos;
    double[][] tabulacion;
    int tipoGrafica;
    
    //arreglos para las multiplicaciones
    double[][] xn, xnPf;
    int numeroDeX, numeroDeMulti;
    
    //arreglos para las sumatorias
    double[] sumX, sumXF;
    
    //matriz del resultado
    double[][] matriz;
    
    //matriz de la grafica
    double[][] grafica;
    
    String funcion = "";
    
    public void llenarTablaTabulacion(int e) {
        DefaultTableModel tabla = new DefaultTableModel();
        
        //Columnas
        tabla.addColumn("x");
        tabla.addColumn("y");
        tb_tabulacion.setModel(tabla);
        
        //Filas
        String datosTabla[] = new String[e];
        
        for (int i = 0; i < e; i++) {
            tabla.addRow(datosTabla);
        }
        
    }
    
    public void llenarTabulacion(int e) {
        TableModel tabla = tb_tabulacion.getModel();
        tabulacion = new double[e][2];
        
        for (int i = 0; i < e; i++) { // ciclo para añadir datos a la matriz tabulación
            for (int j = 0; j < 2; j++) {
                double numero = Double.parseDouble(tabla.getValueAt(i, j).toString());
                
                tabulacion[i][j] = numero;
                //System.out.println("a: " + tabulacion[i][j]);
                
            }
        }
        
        //guardar tipo de grafica
        tipoGrafica = cb_tipoGrafica.getSelectedIndex()+2;
        //System.out.println(tipoGrafica);
        
        //generar
        generarMultiplicaciones();
        generarSumatorias();
        generarMatriz();
        llenarTablaMatriz(tipoGrafica);
        
        aplicarMetodo(tipoGrafica);
        llenarTablaResultados(tipoGrafica);
        generarFuncion(tipoGrafica);
        
        
    }
    
    public void llenarTablaMatriz(int e) {
        //llenar campos de la tabla
        DefaultTableModel tabla = new DefaultTableModel();
        
        //Columnas
        for (int i = 0; i < e; i++) {
            tabla.addColumn(letras[i]);
        }
        tabla.addColumn("R");
        tb_matriz.setModel(tabla);
        
        //Filas
        String datosTabla[] = new String[e];
        
        for (int i = 0; i < e; i++) {
            tabla.addRow(datosTabla);
        }
        
        //llenar datos de la tabla
        for (int i = 0; i < e; i++) {
            for (int j = 0; j <= e; j++) {
                tabla.setValueAt(matriz[i][j], i, j);
            }
        }
    }
    
    public void llenarTablaResultados(int e) {
        //llenar campos de la tabla
        DefaultTableModel tabla = new DefaultTableModel();
        
        //Columnas
        tabla.addColumn("Valores");
        tb_resultados.setModel(tabla);
        
        //Filas
        String datosTabla[] = new String[e];
        
        for (int i = 0; i < e; i++) {
            tabla.addRow(datosTabla);
        }
        
        //llenar datos de la tabla
        for (int j = e; j <= e; j++) {
            for (int i = 0; i < e; i++) {
                tabla.setValueAt(matriz[i][j], i, 0);
            }
        }
        
    }
    
    public void generarMultiplicaciones() {
        //xn
        numeroDeX = tipoGrafica + (tipoGrafica-1) - 1; //numero de x que se ocupan para el tipo de grafica
        //System.out.println(numeroDeX);
        xn = new double[elementos][numeroDeX]; //arreglo de x^n
        
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
        
        //xnPf
        numeroDeMulti = tipoGrafica;
        xnPf = new double[elementos][numeroDeMulti];
        
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
    }
    
    public void generarSumatorias() {
        sumX = new double[numeroDeX]; //arreglo para guardar sumatorias de la matriz xn
        sumXF = new double[numeroDeMulti]; //para guardar sumatorias de la matriz xnPf
        
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
    }
    
    public void generarMatriz() {
        matriz = new double[tipoGrafica][tipoGrafica+1];
        
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
    }
    
    //Método de gauss-jordan
    public void aplicarMetodo(int e) {
        int p = 0;
        for (int i = 0; i < e; i++) {
            pivote(matriz, p, e);
            hacerCeros(matriz, p, e);
            
            p++;
        }
    }
    
    public void pivote(double matriz[][], int p, int var) {
        double divicion = matriz[p][p]; //guardar sobre cuanto se va a dividir
        
        for (int y = 0; y < (var + 1); y++) { //recorrer la fila para ir dividiendo
            double resultado = matriz[p][y] / divicion;
            matriz[p][y] = resultado;
        }
    }
    
    public void hacerCeros(double matriz[][], int p, int var) {
        for (int x = 0; x < var; x++) { //recorrer filas
            
            if (x != p) { //si el pivote no concuerda con la fila en la que la esta
                double constante = matriz[x][p]; //guardar constante del valor de la fila que se va a restar
                
                for (int z = 0; z < (var + 1); z++) { //recorrer columnas
                    double resultado = ((-1 * constante) * matriz[p][z]) + matriz[x][z]; //la matriz se le va a restar tantas veces el pivote por su valor actual
                    matriz[x][z] = resultado;
                }
            }
        }
    }
    
    public void generarFuncion(int e){
        TableModel tabla = tb_resultados.getModel();
        
        for (int i = 0; i < e; i++) {
            double resultado = redondearDecimales(Double.parseDouble(tabla.getValueAt(i, 0).toString()), 4);
            
            if (resultado > 0) { //evaluar si es positivo
                if (i != 0) {
                    funcion += "+";
                }
            }
            
            funcion += String.valueOf(resultado) + equis[i];
            
        }
        
        txt_resultado.setText(funcion);
        funcion = "";
    }
    
    public double redondearDecimales(double valor, int decimales) {
        double resultado = valor;
        BigDecimal big = new BigDecimal(resultado);
        big = big.setScale(decimales, RoundingMode.HALF_UP);
        resultado = Double.parseDouble(big.toString());
        
        return resultado;
    }
    
    //Función con exp4j
    public double f(String funcion, double x) { //funcion
        Expression e = new ExpressionBuilder(funcion) //clase de la libreria exp4j que lee funciones
                .variables("x") //se establece la variable x
                .build() //se construye
                .setVariable("x", x); //establecer lo que vale x
        
        double resultado = e.evaluate(); //se evalua la función
        return resultado;
    }
    
    public void generarPuntosGrafica(String funcion) {
        grafica = new double[201][2]; //guardar tabulacion de la grafica desde -10 hasta 10
        double numerador = -10;
        for (int j = 0; j < 2; j++) { //recorrer la matriz grafica
            for (int i = 0; i < 201; i++) {
                if (j == 0) {
                    grafica[i][j] = redondearDecimales(numerador, 2); //redondear el numerador
                    numerador+=0.1;
                }
                else {
                    grafica[i][j] = f(funcion, redondearDecimales(numerador, 4)); //redondear el numerador y aplicar la funcion f(x)
                    numerador+=0.1;
                }
                //System.out.println("grafica: "+grafica[i][j]);
            }
            numerador = -10;
        }
        
    }
    
    public void llamarFormularioChart(double[][] grafica, int l1, double[][] tabulacion, int l2) {
        FormularioChart ventana = new FormularioChart();
        ventana.setVisible(true);

        GraficasLineal g = new GraficasLineal();
        g.Graficar(grafica, l1, tabulacion, l2);

    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txt_resultado = new javax.swing.JTextField();
        txt_elementos = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tb_tabulacion = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        cb_tipoGrafica = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        tb_matriz = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tb_resultados = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        btn_aceptar = new javax.swing.JButton();
        btn_calcular = new javax.swing.JButton();
        btn_graficar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Ajuste de curvas");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(217, 233, 238));
        jPanel1.setLayout(null);

        jLabel1.setBackground(new java.awt.Color(9, 165, 165));
        jLabel1.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Calculando una curva");
        jLabel1.setOpaque(true);
        jPanel1.add(jLabel1);
        jLabel1.setBounds(0, -1, 750, 50);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Elementos de la tabulación:");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(170, 100, 160, 20);

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Función resultante:");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(30, 420, 110, 20);

        txt_resultado.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jPanel1.add(txt_resultado);
        txt_resultado.setBounds(150, 420, 450, 20);

        txt_elementos.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jPanel1.add(txt_elementos);
        txt_elementos.setBounds(340, 100, 110, 21);

        tb_tabulacion.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tb_tabulacion);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(20, 180, 120, 150);

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Tipo de gráfica:");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(220, 70, 110, 20);

        cb_tipoGrafica.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        cb_tipoGrafica.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Recta", "Parábola", "Cúbica", "Grado 4", "Grado 5", "Grado 6" }));
        jPanel1.add(cb_tipoGrafica);
        cb_tipoGrafica.setBounds(340, 70, 110, 21);

        tb_matriz.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(tb_matriz);

        jPanel1.add(jScrollPane2);
        jScrollPane2.setBounds(160, 180, 430, 200);

        jLabel5.setBackground(new java.awt.Color(9, 165, 165));
        jLabel5.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Tabular:");
        jLabel5.setOpaque(true);
        jPanel1.add(jLabel5);
        jLabel5.setBounds(20, 150, 120, 30);

        jLabel6.setBackground(new java.awt.Color(9, 165, 165));
        jLabel6.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Resultados:");
        jLabel6.setOpaque(true);
        jPanel1.add(jLabel6);
        jLabel6.setBounds(610, 150, 120, 30);

        tb_resultados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane3.setViewportView(tb_resultados);

        jPanel1.add(jScrollPane3);
        jScrollPane3.setBounds(610, 180, 120, 190);

        jLabel7.setBackground(new java.awt.Color(9, 165, 165));
        jLabel7.setFont(new java.awt.Font("Arial Rounded MT Bold", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Resultado de la matriz:");
        jLabel7.setOpaque(true);
        jPanel1.add(jLabel7);
        jLabel7.setBounds(160, 150, 430, 30);

        btn_aceptar.setBackground(new java.awt.Color(117, 215, 216));
        btn_aceptar.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        btn_aceptar.setText("Aceptar");
        btn_aceptar.setBorder(javax.swing.BorderFactory.createMatteBorder(3, 3, 3, 3, new java.awt.Color(11, 137, 191)));
        btn_aceptar.setContentAreaFilled(false);
        btn_aceptar.setOpaque(true);
        btn_aceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_aceptarActionPerformed(evt);
            }
        });
        jPanel1.add(btn_aceptar);
        btn_aceptar.setBounds(470, 70, 100, 50);

        btn_calcular.setBackground(new java.awt.Color(117, 215, 216));
        btn_calcular.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        btn_calcular.setText("Calcular");
        btn_calcular.setBorder(javax.swing.BorderFactory.createMatteBorder(3, 3, 3, 3, new java.awt.Color(11, 137, 191)));
        btn_calcular.setContentAreaFilled(false);
        btn_calcular.setOpaque(true);
        btn_calcular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_calcularActionPerformed(evt);
            }
        });
        jPanel1.add(btn_calcular);
        btn_calcular.setBounds(30, 350, 100, 50);

        btn_graficar.setBackground(new java.awt.Color(117, 215, 216));
        btn_graficar.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        btn_graficar.setText("Graficar");
        btn_graficar.setBorder(javax.swing.BorderFactory.createMatteBorder(3, 3, 3, 3, new java.awt.Color(11, 137, 191)));
        btn_graficar.setContentAreaFilled(false);
        btn_graficar.setOpaque(true);
        btn_graficar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_graficarActionPerformed(evt);
            }
        });
        jPanel1.add(btn_graficar);
        btn_graficar.setBounds(620, 400, 100, 50);

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 750, 470));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_aceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_aceptarActionPerformed
        if (!txt_elementos.getText().equals("")) {
            elementos = Integer.parseInt(txt_elementos.getText());
            llenarTablaTabulacion(elementos);
        }
        else {
            JOptionPane.showMessageDialog(this, "Falta añadir el número de elementos");
        }
    }//GEN-LAST:event_btn_aceptarActionPerformed

    private void btn_calcularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_calcularActionPerformed
        llenarTabulacion(elementos);
    }//GEN-LAST:event_btn_calcularActionPerformed

    private void btn_graficarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_graficarActionPerformed
        if (!txt_resultado.getText().equals("")) {
            //generar matriz graficar
            generarPuntosGrafica(txt_resultado.getText());
            llamarFormularioChart(grafica, 201, tabulacion, elementos);

        }
        else {
            JOptionPane.showMessageDialog(this, "No hay función para graficar");
        }
    }//GEN-LAST:event_btn_graficarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new InterfazAjusteDeCurvas().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_aceptar;
    private javax.swing.JButton btn_calcular;
    private javax.swing.JButton btn_graficar;
    private javax.swing.JComboBox<String> cb_tipoGrafica;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable tb_matriz;
    private javax.swing.JTable tb_resultados;
    private javax.swing.JTable tb_tabulacion;
    private javax.swing.JTextField txt_elementos;
    private javax.swing.JTextField txt_resultado;
    // End of variables declaration//GEN-END:variables
}
