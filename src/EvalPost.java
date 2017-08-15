import javax.swing.*;
import java.util.*;

/**
 * @author Carlos Calderon
 * @version 6.0
 * Clase que se encarga de hacer las operaciones del automata.
 */
public class EvalPost {
    /*Atributos*/
    private Stack<String> E = new Stack<String>(); //Pila entrada
    private Stack<String> P = new Stack<String>(); //Pila de operandos
    private Stack<String> o = new Stack<String>(); //Pila de operadores
    private Stack<NFA> operands = new Stack<NFA>();
    private String sim = "";
    private ArrayList<String> sim2 = new ArrayList<String>();
    /**
     * @param expr Expresion regular ingresada por el usuario ya en postfix.
     * Metodo para anadir la expresion ya en postfix a un Stack.
     */
    void anadirExpresion(String expr) {
        String[] post = expr.split(" ");
        //Añadir post (array) a la Pila de entrada (E)
        int tam = 0;
        for (int i = post.length - 1; i >= 0; i--) {
            E.push(post[i]);

        }
        List asList = Arrays.asList(post);
        Set<String> mySet = new HashSet<String>(asList);
        for (String s : mySet) {
            if (!s.equals("*") && !s.equals("+") && !s.equals(".") && !s.equals("?") && !s.equals("^") && !s.equals("|")) {
                sim += s + ",";
                this.sim2.add(s);
            }
        }
        this.sim = "{" + "Los simbolos son: " + sim + "}";
    }

    /**
     * Metodo para evaluar la expresion e ir haciendo las operaciones necesarias.
     */
    void evaluar() {
        //Algoritmo de Evaluación Postfija
        NFA nuevo;
        String operadores = "*+?.|";
        // Mientras halla algo en el Stack ira comparando para separar operadores de simbolos
        while (!E.isEmpty()) {
            if (operadores.contains("" + E.peek())) {
                if (E.peek().equals("*")) {
                    NFA sym = operands.peek();
                    operands.pop();
                    NFA push = operands.push(kleene(sym));
                    E.pop();
                } else if (E.peek().equals("|")) {
                    // o.push(E.peek());
                    NFA op1 = new NFA();
                    NFA op2 = new NFA();
                    Stack<NFA> selections = new Stack<NFA>();
                    op1 = operands.pop();
                    op2 = operands.pop();
                    selections.add(op2);
                    selections.add(op1);
                    operands.push(or_selection(selections, 2));
                    E.pop();
                } else if (E.peek().equals("?")) {
                    nuevo = new NFA();
                    nuevo.setVertex(2);
                    nuevo.setTransicions(0, 1, "^");
                    nuevo.setFinal_state(1);
                    operands.push(nuevo);
                    NFA op1 = new NFA();
                    NFA op2 = new NFA();
                    Stack<NFA> selections = new Stack<NFA>();
                    op1 = operands.pop();
                    op2 = operands.pop();
                    selections.add(op2);
                    selections.add(op1);
                    operands.push(or_selection(selections, 2));
                    E.pop();
                } else if (E.peek().equals("+")) {
                    System.out.println("Closure");
                    String aux = P.peek();
                    NFA op1 = new NFA();
                    NFA op2 = new NFA();
                    operands.push(kleene(operands.peek()));
                    op2 = operands.pop();
                    op1 = operands.pop();
                    NFA push = operands.push(concat(op1, op2));
                    E.pop();

                } else if (E.peek().equals(".")) {
                    // o.push(E.peek());
                    NFA op1 = new NFA();
                    NFA op2 = new NFA();
                    op2 = operands.pop();
                    op1 = operands.pop();
                    operands.push(concat(op1, op2));
                    // System.out.println(push.toString());
                    E.pop();
                }
            } else {
                //System.out.println(E.peek());
                nuevo = new NFA();
                nuevo.setVertex(2);
                nuevo.setTransicions(0, 1, E.peek());
                nuevo.setFinal_state(1);
                operands.push(nuevo);
                P.push(E.pop());
            }
        }
        //Construir automatas
        String tiempos="",tiempos2="";
        NFA nfa =operands.elementAt(0);
        DFA dfa=nfaToDFA( nfa,this.sim2);
        // Simulacion y toma de tiempos
        String reg2 = JOptionPane.showInputDialog("Ingrese una expresion");
        long startTime4 = System.nanoTime();
        System.out.println( simulacionDFA(reg2,dfa));
        long stopTime4 = System.nanoTime();
        tiempos = simulacionDFA(reg2,dfa);
        tiempos += "\r\nTiempo de simulacion a AFN:"+(stopTime4 - startTime4)+" ns";
        System.out.println("Simulacion a AFN:"+(stopTime4 - startTime4));
        long startTime3 = System.nanoTime();
        System.out.println( simulacionNFA(reg2,nfa));
        long stopTime3 = System.nanoTime();
        tiempos2=simulacionNFA(reg2,nfa);
        tiempos2+="\r\nTiempo de simulacion a AFD:"+(stopTime3 - startTime3)+" ns";
        System.out.println("Simulacion a AFD:"+(stopTime3 - startTime3));
        //Generar textos
        nfa.generarTxt(this.sim,tiempos);
        dfa.generarTxt(this.sim,tiempos2);
        //Desplegar grafos
        nfa.desplegarGrafo();
        dfa.desplegarGrafo();



    }


    /**
     * @param a Automata
     *          Metodo para hacerle kleene al simbolo (automata) que llega.
     *          Inspirado en AlexMathew
     */
    NFA kleene(NFA a) {
        NFA result = new NFA();
        int i;
        Transicion new_trans;
        result.setVertex(a.get_vertex_count() + 2);

        result.setTransicions(0, 1, "^");

        for (i = 0; i < a.getTransicions().size(); i++) {
            new_trans = a.getTransicions().elementAt(i);
            result.setTransicions(new_trans.getVertex_from() + 1, new_trans.getVertex_to() + 1, new_trans.getTrans_symbol());
        }

        result.setTransicions(a.get_vertex_count(), a.get_vertex_count() + 1, "^");
        result.setTransicions(a.get_vertex_count(), 1, "^");
        result.setTransicions(0, a.get_vertex_count() + 1, "^");

        result.setFinal_state(a.get_vertex_count() + 1);
        //System.out.println("finalkleene"+result.getFinal_state());
        return result;
    }

    /**
     * @param a Automata
     * @param b Automata
     *          Metodo para hacerle concatenacion al simbolo (automata) que llega.
     *          Inspirado en AlexMathew
     */
    NFA concat(NFA a, NFA b) {
        NFA result = new NFA();
        result.setVertex(a.get_vertex_count() + b.get_vertex_count()-1);
        //System.out.println("count"+a.get_vertex_count()+"countb"+b.get_vertex_count());
        int i;
        Transicion new_trans;
        for (i = 0; i < a.getTransicions().size(); i++) {
            new_trans = a.getTransicions().elementAt(i);
            result.setTransicions(new_trans.getVertex_from(), new_trans.getVertex_to(), new_trans.getTrans_symbol());
            //System.out.println(new_trans.getVertex_from()+""+new_trans.getVertex_to()+""+new_trans.getTrans_symbol());
        }
        //result.setTransicions(a.getFinal_state(), a.get_vertex_count(), "^");
        new_trans = b.getTransicions().elementAt(0);
        result.setTransicions(a.getFinal_state(), a.get_vertex_count(), new_trans.getTrans_symbol());
        // System.out.println("count"+a.get_vertex_count()+"fina"+a.getFinal_state());
        for (i = 1; i < b.getTransicions().size(); i++) {
            new_trans = b.getTransicions().elementAt(i);
            result.setTransicions(new_trans.getVertex_from() + a.get_vertex_count()-1, new_trans.getVertex_to() + a.get_vertex_count()-1, new_trans.getTrans_symbol());
            //System.out.println((new_trans.getVertex_from() + a.get_vertex_count()-1)+" "+(new_trans.getVertex_to()+ a.get_vertex_count()-1)+""+new_trans.getTrans_symbol());
        }
        result.setFinal_state(a.get_vertex_count() + b.get_vertex_count() - 2);
        return result;
    }

    /**
     * @param selections       Arreglo de automatas
     * @param no_of_selections Numero de elementos
     *                         Inspirado en AlexMathew
     *                         Metodo para hacerle concatenacion al simbolo (automata) que llega.
     */
    NFA or_selection(Vector<NFA> selections, int no_of_selections) {
        NFA result = new NFA();
        int vertex_count = 2;
        int i, j;
        NFA med;
        Transicion new_trans;
        for (i = 0; i < no_of_selections; i++) {
            vertex_count += selections.elementAt(i).get_vertex_count();
        }
        result.setVertex(vertex_count);
        int adder_track = 1;
        for (i = 0; i < no_of_selections; i++) {
            result.setTransicions(0, adder_track, "^");
            med = selections.elementAt(i);
            for (j = 0; j < med.getTransicions().size(); j++) {
                new_trans = med.getTransicions().elementAt(j);
                result.setTransicions(new_trans.getVertex_from() + adder_track, new_trans.getVertex_to() + adder_track, new_trans.getTrans_symbol());
            }
            adder_track += med.get_vertex_count();

            result.setTransicions(adder_track - 1, vertex_count - 1, "^");
        }
        result.setFinal_state(vertex_count - 1);
        return result;
    }
    /**
     *  Metodo para simular un eclosure
     * @param S       Arreglo de estados
     * @param nfa     Automata finido no determinista
     * @return        La lista de estados alcanzados con eclosure
     */
    ArrayList<String> eClosure(ArrayList<String> S, NFA nfa) {
        Stack<String> pila = new Stack<String>();
        ArrayList<String> ListaClosures = new ArrayList<String>();
        String aux = "";
        for (String i : S) {
            pila.add(i);
            ListaClosures.add(i);
            //System.out.println(i);
        }
        while (!pila.empty()) {
            aux = pila.pop();
            // System.out.println("aux"+aux);
            for (Transicion edge : nfa.getTransicions()) {
                String aux2 = "" + edge.getVertex_from();
                if (aux2.equals(aux) && edge.getTrans_symbol().equals("^")) {
                    //System.out.println("de ->"+aux2);
                    if (!ListaClosures.contains(edge.getVertex_to())) {
                        //  System.out.println("a ->"+edge.getVertex_to());
                        pila.push("" + edge.getVertex_to());
                        ListaClosures.add("" + edge.getVertex_to());
                    }
                }
            }
        }
       /* String listString = "";

        for (String s : ListaClosures)
        {
            listString += s + "\t";
        }
       //State state = new State(listString,false);
       // return state;
        return listString;*/
        return ListaClosures;
    }
    /**
     *  Metodo para simular un move de un NFA.
     * @param S       Arreglo de estados
     * @param a       Simbolo de lenguaje
     * @param nfa     Automata finido no determinista
     * @return        La lista de estados alcanzados con move
     */
    ArrayList<String> move(ArrayList<String> S, String a, NFA nfa) {
        ArrayList<String> lista = new ArrayList<String>();
        //for (String s : S) {
            for (Transicion edge : nfa.getTransicions()) {
                if (S.contains(""+edge.getVertex_from()) && edge.getTrans_symbol().equals(a)) {
                    lista.add("" + edge.getVertex_to());
                }
            }
        //}
        return lista;
    }
    /**
     *  Metodo para simular un move de un DFA.
     * @param S       Arreglo de estados
     * @param a       Simbolo de lenguaje
     * @param dfa     Automata finido no determinista
     * @return        La lista de estados alcanzados con move
     */
    ArrayList<String> move(ArrayList<String> S, String a, DFA dfa) {
        ArrayList<String> lista = new ArrayList<String>();
        //System.out.println("Entre"+S.toString());
        if(S.get(0).length() == 1){
            for (TransicionDFA edge : dfa.getTransicions()) {
                for (char ch : edge.getVertex_from().toCharArray()) {
                    if (S.contains("" + ch) && !lista.contains(edge.getVertex_to())) {
                        lista.add("" + edge.getVertex_to());
                    }
                }
            }
        }
        else{
            for (TransicionDFA edge : dfa.getTransicions()) {
                if (edge.getTrans_symbol().equals(a)) {
                    if (S.contains(""+edge.getVertex_from())&& !lista.contains(edge.getVertex_to())) {
                        lista.add("" + edge.getVertex_to());
                    }
                }
            }
        }

        return lista;
    }

    /*
    * Metodo para convertir de NFA a DFA
    * @param nfa Automata finito no determinista de tipo
    * @param Simb simbolos manejados por el nfa
    * @return DFA
    * */
    DFA nfaToDFA(NFA nfa, ArrayList<String> simb) {
        DFA dfa = new DFA();
        ArrayList<String> temp = new ArrayList<String>();
        temp.add(""+nfa.getVertex().get(0));
        //LinkedHashSet<State> setdfa= new LinkedHashSet<State>();
        ArrayList<ArrayList<String>> setdfa= new ArrayList<ArrayList<String>>();
        ArrayList<Boolean> b = new ArrayList<Boolean>();
        setdfa.add(eClosure(temp,nfa));
        b.add(false);
        int i=0;
        ArrayList<String> S = new ArrayList<String>();
        while(b.get(i)==false){
            // System.out.println("i"+i);
            b.set(i,true);
           // System.out.println("size"+b.size());
            for (String a: simb){
                //System.out.println(a);
                S= eClosure(move(setdfa.get(i),a,nfa),nfa);
                //System.out.println(setdfa.get(i).toString()+"kk1");
                //System.out.println(move(setdfa.get(i),a,nfa).toString()+"kk2");
                //System.out.println("aqui si"+S.toString());
                if (!setdfa.contains(S)){
                    setdfa.add(S);
                    b.add(false);
                }
                boolean final1 = false;
                boolean final2  = false;
                String listString = "";
                String listString2 = "";
                for (String s :setdfa.get(i) ){
                    listString += s;
                    if (s.equals(""+nfa.getFinal_state())){
                        final1 = true;
                    }
                    //System.out.println("setdfa"+listString);
                }

                //listString=listString.substring(0, listString.length() - 1);
                //listString+=")";
                for (String s :S ){
                    listString2 += s;
                    if (s.equals(""+nfa.getFinal_state()) ){
                        final2 = true;
                    }
                    //System.out.println("S"+listString2);
                }
                //listString2=listString2.substring(0, listString2.length() - 1);
                //listString2+=")";
                //System.out.println("listString"+listString);
                //System.out.println("listString2"+listString2);
                dfa.setVertex(listString);
                dfa.setTransicions(listString,listString2,a);
                if (final1 && !dfa.getFinal_state().contains(listString) ){
                    dfa.setFinal_state(listString);
                }
                if (final2 && !dfa.getFinal_state().contains(listString2)){
                    dfa.setFinal_state(listString2);
                }
            }
            i++;
            if(i==b.size()){
                break;
            }
        }
        return dfa;
    }

    /*
    * Metodo para simular la corrida de una expresion en un automata NFA
    * @param a cadena que contiene los simbolos del lenguaje
    * @param nfa que sera el automata que decide si acepta o no la expresion
    * @return cadena que informa si la expresion es aceptada o no
    * */
    String simulacionNFA(String a,NFA nfa) {
        ArrayList<String> S = new ArrayList<String>();
        S.add(""+0);
        S = eClosure(S,nfa);
        for (char ch : a.toCharArray()){
            S=eClosure(move(S,""+ch,nfa),nfa);
        }
        String temp = ""+nfa.getFinal_state();
        String o="No se acepta";
        for ( String s: S){
            if (s.equals(temp)){
                o = "Se acepta";
            }
        }
        return o;
    }
    /*
   * Metodo para simular la corrida de una expresion en un automata DFA
   * @param a cadena que contiene los simbolos del lenguaje
   * @param dfa que sera el automata que decide si acepta o no la expresion
   * @return cadena que informa si la expresion es aceptada o no
   * */
    String simulacionDFA(String a, DFA dfa) {
        ArrayList<String> S = new ArrayList<String>();
        S.add(""+0);
        for (char ch : a.toCharArray()){
            S=move(S,""+ch,dfa);
        }
        String o="No se acepta";
       for ( String s: S){
            for (String s2: dfa.getFinal_state()){
                if (s.equals(s2)){
                    o = "Se acepta";
                    break;
                }
            }
        }
        return o;
    }
}