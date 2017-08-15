import javax.swing.*;

/**
 * @author Carlos Calderon
 *  @version 6.0
 * Clase que principal que recibe datos y ejecutar el programa.
 */
public class Main {
    /**
     * Metodo main para correr el programa
     * @param args .
     */
    public static void main(String[] args) {
        // Instanciar objetos para conversion a postfix y evaluar expresion.
        RegExConverter r = new RegExConverter();
        EvalPost e = new EvalPost();
        // Dar instrucciones
        JOptionPane.showMessageDialog(null,"Bienvenido al generador de AFNs");
        JOptionPane.showMessageDialog(null,"Puede utilizar cualquier caracter a excepcion de las palabras reservadas  " +
                "(?.*|+)  y usar solo yuxtaposicion para concatenar");
        String reg = JOptionPane.showInputDialog("Ingrese una expresion regular");
        String a = r.infixToPostfix(reg);
       // Ejecutar conversion
        e.anadirExpresion(a);
        e.evaluar();
        //Probar simulacion
        //String reg2 = JOptionPane.showInputDialog("Ingrese una expresion");
        //e.simulacionNFA(reg2);


    }
}
