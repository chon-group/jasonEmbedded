package jason.stdlib;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;

public class receivingHost extends DefaultInternalAction {
    //.receive_config("host","protocol","port")
    public Object execute(final TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        if (args.length == 3) {
            ts.getUserAgArch().getEmailBridge().setReceiverProps(
                    args[0].toString().replaceAll("\"",""),
                    args[1].toString().replaceAll("\"",""),
                    args[2].toString().replaceAll("\"",""));
            return true;
        } else {
            return false;
        }
    }

}
