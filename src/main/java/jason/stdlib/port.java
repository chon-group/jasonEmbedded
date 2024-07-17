package jason.stdlib;

import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;

import java.util.logging.Logger;

public class port extends group.chon.agent.argo.jasonStdLib.port{
    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        Logger logger = Logger.getLogger("ARGO");
        logger.severe("The internal action \".port\" changed to \".argo.port\", please update your .ASL file. Consult: https://github.com/chon-group/Argo/wiki");
        return super.execute(ts,un,args);
    }
}
