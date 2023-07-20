package jason.stdlib;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;

public class sendingProperties extends DefaultInternalAction {
    //.send_auth(True,True,True,"String","String")
    public Object execute(final TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        if (args.length == 5) {
            ts.getUserAgArch().getEmailBridge().setSendAuth(
                    Boolean.parseBoolean(args[0].toString().replaceAll("\"","")),
                    Boolean.parseBoolean(args[1].toString().replaceAll("\"","")),
                    Boolean.parseBoolean(args[2].toString().replaceAll("\"","")),
                    args[3].toString().replaceAll("\"",""),
                    args[4].toString().replaceAll("\"",""));
            return true;
        } else {
            return false;
        }
    }

}
