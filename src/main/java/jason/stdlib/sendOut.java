package jason.stdlib;

import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;

import java.util.logging.Logger;

public class sendOut extends group.chon.agent.hermes.jasonStdLib.sendOut {
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
//        /* in next version */
//        Logger logger = Logger.getLogger("HERMES");
//        logger.severe("The internal action \".sendOut\" changed to \".hermes.sendOut\", please update your .ASL file. Consult: https://github.com/chon-group/Hermes/wiki");
        return super.execute(ts,un,args);
    }
}
