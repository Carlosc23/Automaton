/**
 * @author Carlos Calderon
 *  @version 5.0
 * Clase que simula una transicion/nodo de un NFA
 */
public class TransicionDFA {
    /* Atributos*/
    private String vertex_from;
    private String vertex_to;
    String trans_symbol;
    /*
    * Metodo constructor
     */
    public TransicionDFA(String vertex_from, String vertex_to, String trans_symbol) {
        this.vertex_from = vertex_from;
        this.vertex_to = vertex_to;
        this.trans_symbol = trans_symbol;
    }

    public void setVertex_from(String vertex_from) {
        this.vertex_from = vertex_from;
    }

    public void setVertex_to(String vertex_to) {
        this.vertex_to = vertex_to;
    }

    public void setTrans_symbol(String  trans_symbol) {
        this.trans_symbol = trans_symbol;
    }

    public String getVertex_from() {

        return vertex_from;
    }

    public String getVertex_to() {
        return vertex_to;
    }

    public String  getTrans_symbol() {
        return trans_symbol;
    }
}
