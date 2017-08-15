/**
 * @author Carlos Calderon
 *  @version 3.0
 * Clase que simula una transicion/nodo
 */
public class Transicion {
    /* Atributos*/
    private int vertex_from;
    private int vertex_to;
    String trans_symbol;
    /*
    * Metodo constructor
     */
    public Transicion(int vertex_from, int vertex_to, String trans_symbol) {
        this.vertex_from = vertex_from;
        this.vertex_to = vertex_to;
        this.trans_symbol = trans_symbol;
    }

    public void setVertex_from(int vertex_from) {
        this.vertex_from = vertex_from;
    }

    public void setVertex_to(int vertex_to) {
        this.vertex_to = vertex_to;
    }

    public void setTrans_symbol(String  trans_symbol) {
        this.trans_symbol = trans_symbol;
    }

    public int getVertex_from() {

        return vertex_from;
    }

    public int getVertex_to() {
        return vertex_to;
    }

    public String  getTrans_symbol() {
        return trans_symbol;
    }
}
