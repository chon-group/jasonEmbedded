package jason.stdlib;

import com.google.gson.JsonObject;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;


/**
 * <p>
 * Internal action: <b><code>.policy</code></b>.
 *
 * <p>
 **/
public class policy extends DefaultInternalAction{

    @Override
    public Object execute(final TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        int error = 0;
        if(args.length != 4){
            System.out.println("Numero incorreto de argumentos para criar uma nova politica");
            error++;
        }else{
            if(!args[0].toString().equals("input") && !args[0].toString().equals("output") && !args[0].toString().equals("all")){
                System.out.println("O valor inserido na politica para tipo está incorreto. all/input/output");
                error++;
            }else{
                if(!args[1].toString().equals("all") && !args[1].toString().equals("communication") && !args[1].toString().equals("migration")){
                    System.out.println("O valor inserido na politica para abrangencia está incorreto. all/communication/migration");
                    error++;
                }else{
                    boolean v;
                    v = validarProtocolo(args[2].toString(), args[1].toString());
                    switch (args[1].toString()) {
                        case "all":
                            if (!v) {
                                System.out.println("O valor inserido na policy para protocolo esta incorreto. all/kqml/bioinsp/tell/untell/askOne/askAll/achieve/unachieve/mutualism/inquilinism/pedratism");
                                return false;
                            }
                        case "communication":
                            if (!v) {
                                System.out.println("O valor inserido na policy para protocolo de comunicação esta incorreto. kqml/tell/untell/askOne/askAll/achieve/unachieve");
                                return false;
                            }
                        case "migration":
                            if (!v) {
                                System.out.println("O valor inserido na policy para protocolo de migração esta incorreto. bioinsp/mutualism/inquilinism/pedratism");
                                return false;
                            }
                    }
                    if( !args[3].toString().equals("accept") && !args[3].toString().equals("drop")){
                            System.out.println("O valor inserido na politica para determinação está incorreto. accept/drop");
                            error++;
                    }else{
                            JsonObject politica = new JsonObject();
                            politica.addProperty("tipo", args[0].toString());
                            politica.addProperty("abrangencia", args[1].toString());
                            politica.addProperty("protocolo", args[2].toString());
                            politica.addProperty("determinacao", args[3].toString());
                            ts.getUserAgArch().setFirewallPolicy(politica);
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
                case "all":
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
                case "predatism":
                    return true;
            }
        }else if (abrangencia.equals("communication")) {
            switch (protocolo) {
                case "all":
                case "kqml":
                case "tell":
                case "untell":
                case "askOne":
                case "askAll":
                case "achieve":
                case "unachieve":
                    return true;
            }
        } else if (abrangencia.equals("migration")) {
            switch (protocolo) {
                case "all":
                case "bioinsp":
                case "mutualism":
                case "inquilinism":
                case "predatism":
                    return true;
            }
        }
        return false;
    }
}
