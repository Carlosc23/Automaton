import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

/**
 * @author Carlos Calderon
 *  @version 5.0
 * Clase que simula un automata
 */
public class NFA extends Component {
    /*Atributos*/
    private Vector<Integer> vertex = new Vector<Integer>();
    private Vector<Transicion> transicions = new Vector<Transicion>();
    private int final_state;
    Graph graph= new SingleGraph("Automata");

    /**
     * Metodo constructor
     */
    public NFA() {
    }

    public Vector<Integer> getVertex() {
        return vertex;
    }

    public void setVertex(int no_vertex) {
        for (int i = 0; i < no_vertex; i++) {
            vertex.add(i);
        }
    }

    public Vector<Transicion> getTransicions() {
        return transicions;
    }

    public void setTransicions(int vertex_from, int vertex_to,String trans_symbol) {
        Transicion new_trans = new Transicion(vertex_from,vertex_to,trans_symbol);
        transicions.add(new_trans);
    }

    public int getFinal_state() {
        return final_state;
    }

    public void setFinal_state(int final_state) {
        this.final_state = final_state;
    }
    public int get_vertex_count(){
        return vertex.size();
    }

    /**
     * ToString
     */
    @Override
    public String toString() {
        Transicion new_trans;
        for (int i = 0; i < transicions.size(); i++) {
            new_trans = transicions.elementAt(i);
            System.out.println("q"+new_trans.getVertex_from()+" --> q"+new_trans.getVertex_to()+" : Simbolo - "+new_trans.getTrans_symbol());
        }
        System.out.print("Los estados son: {");
        for (int j: vertex){
            System.out.print("q"+j+",");
        }
        System.out.print("}\n");
        return  "El estado de aceptacion es q" + getFinal_state();
    }
    /**
     * Genera el txt
     * Inspirado en: http://codejavu.blogspot.com/2013/11/ejemplo-jfilechooser.html
     */
    public void generarTxt(String a,String tiempo){
        JOptionPane.showMessageDialog(null,
                "Seleccione donde quiere guardar el txt generado.",
                "Información",JOptionPane.INFORMATION_MESSAGE);
        Transicion new_trans;
        String s=""+a+"\r\n";
        s+="El estado incial es q0 \r\n";
        s+="Las transiciones del automata son: \r\n";
        for (int i = 0; i < transicions.size(); i++) {
            new_trans = transicions.elementAt(i);
            s+="q"+new_trans.getVertex_from()+" --> q"+new_trans.getVertex_to()+" : Simbolo - "+new_trans.getTrans_symbol()+"\r\n";
        }
        s+="Los estados son: {";
        for (int j: vertex){
            s+="q"+j+",";
        }
        s+="}\r\n";
        s+="El estado de aceptacion es q" + getFinal_state()+"\r\n";
        s+=tiempo;
        try
        {
            String nombre="";
            JFileChooser file=new JFileChooser();
            file.showSaveDialog(this);
            File guarda =file.getSelectedFile();

            if(guarda !=null)
            {
   /*guardamos el archivo y le damos el formato directamente,
    * si queremos que se guarde en formato doc lo definimos como .doc*/
                FileWriter  save=new FileWriter(guarda+".txt");
                save.write(s);
                save.close();
                JOptionPane.showMessageDialog(null,
                        "El archivo se a guardado Exitosamente",
                        "Información",JOptionPane.INFORMATION_MESSAGE);
            }
        }
        catch(IOException ex)
        {
            JOptionPane.showMessageDialog(null,
                    "Su archivo no se ha guardado",
                    "Advertencia",JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Metodo para desplegar un grafo y llenarlo.
     */
    void desplegarGrafo(){
        graph.addAttribute("ui.stylesheet", "node {fill-color: red; size-mode: dyn-size;} edge {fill-color:grey;}");
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        Transicion new_trans;
        for (int j: vertex){
           Node a = graph.addNode(String.valueOf(j));
            a.addAttribute("ui.label", "q"+j);
        }
        for (int i = 0; i < transicions.size(); i++) {
            new_trans = transicions.elementAt(i);
            Edge e = graph.addEdge("q"+new_trans.getVertex_from()+" --> q"+new_trans.getVertex_to(), String.valueOf(new_trans.getVertex_from()),
                    String.valueOf(new_trans.getVertex_to()),true);
            e.setAttribute("ui.label",new_trans.getTrans_symbol());
        }
        graph.display();
    }

}

