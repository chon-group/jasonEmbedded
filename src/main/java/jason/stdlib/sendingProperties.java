package jason.stdlib;

import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;

public class sendingProperties {
    //.send_auth(True,True,True,"String","String")
    public Object execute(final TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        if (args.length == 5) {
            ts.getUserAgArch().getEmailBridge().setSendAuth(Boolean.parseBoolean(args[0].toString()),Boolean.parseBoolean(args[1].toString()),Boolean.parseBoolean(args[2].toString()),args[3].toString(),args[4].toString());
            return true;
        } else {
            return false;
        }
    }

}
