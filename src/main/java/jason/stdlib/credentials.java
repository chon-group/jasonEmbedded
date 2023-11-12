package jason.stdlib;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;

public class credentials extends DefaultInternalAction {
    public Object execute(final TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        if (args.length == 2) {
            ts.getUserAgArch().getEmailBridge().setLogin(args[0].toString().replaceAll("\"",""));
            ts.getUserAgArch().getEmailBridge().setPassword(args[1].toString().replaceAll("\"",""));
            ts.getUserAgArch().getEmailBridge().setMailerName(ts.getUserAgArch().getAgName());
            return true;
        } else {
            return false;
        }
    }
}
