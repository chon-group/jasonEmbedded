package jason.stdlib;

import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;

import java.util.logging.Logger;

public class buildWallet extends group.chon.velluscinum.jasonStdLib.buildWallet{
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        Logger logger = Logger.getLogger("VELLUSCINUM");
        logger.severe("The internal action \".buildWallet\" changed to \".velluscinum.buildWallet\", please update your .ASL file. Consult: https://github.com/chon-group/Velluscinum/wiki");
        return super.execute(ts,un,args);
    }
}
