package jason.stdlib;

import com.google.gson.JsonObject;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;

/**
 * <p>
 * Internal action: <b><code>.rule</code></b>.
 *
 * <p>
 **/
public class rule extends DefaultInternalAction{

    private String tipo;
    private String abrangencia;
    private String origem;
    private String destino;
    private String protocolo;

    private String determinacao;

    private JsonObject rule;

    public rule(){

    }
    public rule(String tipo, String abrangencia, String origem, String destino, String protocolo, String determinacao){
        this.tipo = tipo;
        this.abrangencia = abrangencia;
        this.origem = origem;
        this.destino = destino;
        this.protocolo = protocolo;
        this.determinacao = determinacao;
    }

    @Override
    public Object execute(final TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        int error = 0;
        if(args.length != 5){
            System.out.println("Numero incorreto de argumentos para criar uma nova rule");
            error++;
        }else{
            if(!args[0].toString().equals("input") && !args[0].toString().equals("output")){
                System.out.println("O valor inserido na rule para tipo está incorreto. input/output");
                error++;
            }else {
                if (!args[1].toString().equals("all") && !args[1].toString().equals("communication") && !args[1].toString().equals("migration")) {
                    System.out.println("O valor inserido na rule para abrangencia está incorreto. all/communication/migration");
                    error++;
                } else {
                    if ( args[2].toString().isEmpty() ){
                        System.out.println("O valor inserido na rule para endereco está incorreto.");
                        error++;
                    } else {
                            if (!validarProtocolo(args[3].toString(), args[1].toString())) {
                                System.out.println("O valor inserido na rule para força/protocolo esta incorreto. all/kqml/bioinsp");
                                error++;
                            } else {
                                if (!args[4].toString().equals("accept") && !args[4].toString().equals("drop")) {
                                    System.out.println("O valor inserido na rule para determinação está incorreto. accept/drop");
                                    error++;
                                } else {
                                    JsonObject rule = new JsonObject();
                                    rule.addProperty("tipo", args[0].toString());
                                    rule.addProperty("abrangencia", args[1].toString());
                                    rule.addProperty("endereco", args[2].toString());
                                    rule.addProperty("protocolo", args[3].toString());
                                    rule.addProperty("determinacao", args[4].toString());
                                    ts.getUserAgArch().setFirewallRule(rule);
                                }
                            }
                        }
                    }
                }
            }
        if(error > 0)
            return false;
        return true;
    }

    private boolean validarProtocolo(String protocolo, String abrangencia){
        if (abrangencia.equals("all")) {
            switch (protocolo) {
                case "tell":
                case "untell":
                case "ask one":
                case "ask all":
                case "achieve":
                case "unachieve":
                case "mutualism":
                case "inquilinism":
                case "predatism":
                case "all":
                    return true;
            }
        }else if (abrangencia.equals("communication")) {
            switch (protocolo) {
                case "tell":
                case "untell":
                case "ask one":
                case "ask all":
                case "achieve":
                case "unachieve":
                    return true;
            }
        } else if (abrangencia.equals("migration")) {
            switch (protocolo) {
                case "mutualism":
                case "inquilinism":
                case "predatism":
                case "all":
                    return true;
            }
        }
        return false;
    }
}
