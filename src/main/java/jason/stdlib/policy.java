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

    private String tipo;
    private String abrangencia;
    private String protocolo;

    private String determinacao;

    private JsonObject politica;
    public policy(){

    }
    public policy(String tipo, String abrangencia, String protocolo, String determinacao){
        this.tipo = tipo;
        this.abrangencia = abrangencia;
        this.protocolo = protocolo;
        this.determinacao = determinacao;
    }
    @Override
    public Object execute(final TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        int error = 0;
        if(args.length != 4){
            System.out.println("Numero incorreto de argumentos para criar uma nova politica");
            error++;
        }else{
            if(!args[0].toString().equals("input") && !args[0].toString().equals("output")){
                System.out.println("O valor inserido na politica para tipo está incorreto. input/output");
                error++;
            }else{
                if(!args[1].toString().equals("all") && !args[1].toString().equals("communication") && !args[1].toString().equals("migration")){
                    System.out.println("O valor inserido na politica para abrangencia está incorreto. all/communication/migration");
                    error++;
                }else{
                    if(!validarProtocolo(args[2].toString(), args[1].toString())){
                        System.out.println("O valor inserido na politica para força/protocolo eśta incorreto. all/kqml/bioinsp");
                        error++;
                    }else{
                        if( !args[3].toString().equals("accept") && !args[3].toString().equals("drop")){
                            System.out.println("O valor inserido na politica para determinação está incorreto. accept/drop");
                            error++;
                        }else{
                            policy p = new policy(args[0].toString(), args[1].toString(), args[2].toString(), args[3].toString());
                            JsonObject politica = new JsonObject();
                            politica.addProperty("tipo", args[0].toString());
                            politica.addProperty("abrangencia", args[1].toString());
                            politica.addProperty("forca", args[2].toString());
                            politica.addProperty("determinacao", args[3].toString());
                            ts.getUserAgArch().setFirewallPolicy(politica);
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
