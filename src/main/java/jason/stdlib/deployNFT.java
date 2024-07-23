package jason.stdlib;

import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;

import java.util.logging.Logger;

public class deployNFT extends group.chon.velluscinum.jasonStdLib.deployNFT{
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        Logger logger = Logger.getLogger("VELLUSCINUM");
        logger.severe("The internal action \".deployNFT\" changed to \".velluscinum.deployNFT\", please update your .ASL file. Consult: https://github.com/chon-group/Velluscinum/wiki");
        return super.execute(ts,un,args);
    }
}
