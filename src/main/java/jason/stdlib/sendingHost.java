package jason.stdlib;

import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;

public class sendingHost {
    //.send_config("host","protocol","port")
    public Object execute(final TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        if (args.length == 3) {
            ts.getUserAgArch().getEmailBridge().setSendProps(args[0].toString(),args[1].toString(),args[2].toString());
            return true;
        } else {
            return false;
        }
    }

}
