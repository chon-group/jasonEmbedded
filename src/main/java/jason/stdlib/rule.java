package jason.stdlib;

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

        return true;
    }
}
