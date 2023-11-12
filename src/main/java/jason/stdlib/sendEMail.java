package jason.stdlib;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;

public class sendEMail extends DefaultInternalAction {
    //.sendEmail("Recipient Email","Subject","Message")

    @Override
    public Object execute(final TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        if (args.length == 3) {
            ts.getUserAgArch().getEmailBridge().sendMsg(
                    args[0].toString().replaceAll("\"",""),
                    args[1].toString(),
                    args[2].toString());
            return true;
        } else return false;

    }


}
