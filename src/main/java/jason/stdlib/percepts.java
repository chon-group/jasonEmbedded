package jason.stdlib;

import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;

import java.util.logging.Logger;

public class percepts extends group.chon.agent.argo.jasonStdLib.percepts{
    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        //Logger logger = Logger.getLogger("ARGO");
        //logger.severe("The internal action \".percepts\" changed to \".argo.percepts\", please update your .ASL file. Consult: https://github.com/chon-group/Argo/wiki");
        return super.execute(ts,un,args);
    }
}
