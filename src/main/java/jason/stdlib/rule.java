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
        if(args.length != 5){
            System.out.println("Numero incorreto de argumentos para criar uma nova rule");
        }else{
            if(!args[0].toString().equals("input") && !args[0].toString().equals("output")){
                System.out.println("O valor inserido na rule para tipo está incorreto. input/output");
            }else {
                if (!args[1].toString().equals("all") && !args[1].toString().equals("communication") && !args[1].toString().equals("migration")) {
                    System.out.println("O valor inserido na rule para abrangencia está incorreto. all/communication/migration");
                } else {
                    if ( args[2].toString().isEmpty() ){
                        System.out.println("O valor inserido na rule para endereco está incorreto.");
                    } else {
                            if (!args[3].toString().equals("all") && !args[3].toString().equals("kqml") && !args[3].toString().equals("bioinsp")) {
                                System.out.println("O valor inserido na rule para força/protocolo esta incorreto. all/kqml/bioinsp");
                            } else {
                                if (!args[4].toString().equals("accept") && !args[4].toString().equals("drop")) {
                                    System.out.println("O valor inserido na rule para determinação está incorreto. accept/drop");
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
        return true;
    }
}
