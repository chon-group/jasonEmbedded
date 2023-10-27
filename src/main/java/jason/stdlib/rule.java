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
        if (args.length != 5) {
            System.out.println("Numero incorreto de argumentos para criar uma nova rule");
            error++;
        } else {
            if (!args[0].toString().equals("input") && !args[0].toString().equals("output") && !args[0].toString().equals("all")) {
                System.out.println("O valor inserido na rule para tipo está incorreto. all/input/output");
                error++;
            } else {
                if (!args[1].toString().equals("all") && !args[1].toString().equals("communication") && !args[1].toString().equals("migration")) {
                    System.out.println("O valor inserido na rule para abrangencia está incorreto. all/communication/migration");
                    error++;
                } else {
                    if (args[2].toString().isEmpty()) {
                        System.out.println("O valor inserido na rule para endereco está incorreto.");
                        error++;
                    } else {
                        boolean v;
                        v = validarProtocolo(args[3].toString(), args[1].toString());
                        switch (args[1].toString()) {
                            case "all":
                                if (!v) {
                                    System.out.println("O valor inserido na rule para protocolo esta incorreto. all/kqml/bioinsp/tell/untell/askOne/askAll/achieve/unachieve/mutualism/inquilinism/predation");
                                    return false;
                                }
                            case "communication":
                                if (!v) {
                                    System.out.println("O valor inserido na rule para protocolo de comunicação esta incorreto. kqml/tell/untell/askOne/askAll/achieve/unachieve");
                                    return false;
                                }
                            case "migration":
                                if (!v) {
                                    System.out.println("O valor inserido na rule para protocolo de migração esta incorreto. bioinsp/mutualism/inquilinism/predation");
                                    return false;
                                }
                        }
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

        if(error > 0)
            return false;
        return true;
    }

    private boolean validarProtocolo(String protocolo, String abrangencia){
        if (abrangencia.equals("all")) {
            switch (protocolo) {
                case "kqml":
                case "bioinsp":
                case "tell":
                case "untell":
                case "askOne":
                case "askAll":
                case "achieve":
                case "unachieve":
                case "mutualism":
                case "inquilinism":
                case "predation":
                case "all":
                    return true;
            }
        }else if (abrangencia.equals("communication")) {
            switch (protocolo) {
                case "kqml":
                case "tell":
                case "untell":
                case "askOne":
                case "askAll":
                case "achieve":
                case "unachieve":
                case "all":
                    return true;
            }
        } else if (abrangencia.equals("migration")) {
            switch (protocolo) {
                case "bioinsp":
                case "mutualism":
                case "inquilinism":
                case "predation":
                case "all":
                    return true;
            }
        }
        return false;
    }
}
