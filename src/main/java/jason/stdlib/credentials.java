package jason.stdlib;

import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;

public class credentials {
    public Object execute(final TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        if (args.length == 2) {
            ts.getUserAgArch().getEmailBridge().setLogin(args[0].toString());
            ts.getUserAgArch().getEmailBridge().setPassword(args[1].toString());
            return true;
        } else {
            return false;
        }
    }
}
