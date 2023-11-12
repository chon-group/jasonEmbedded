package jason.stdlib;

import jason.architecture.EMailMiddleware;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.Message;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;

import java.util.ArrayList;

public class receiveEMail extends DefaultInternalAction {

    public Object execute(final TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        if (args.length == 2) {
            ts.getUserAgArch().getEmailBridge().setLogin(args[0].toString());
            ts.getUserAgArch().getEmailBridge().setPassword(args[1].toString());
        }
        ts.getUserAgArch().addMessageToC();
        return true;

    }
}
