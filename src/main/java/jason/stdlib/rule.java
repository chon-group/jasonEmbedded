package jason.stdlib;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;

/**
 * <p>
 * Internal action: <b><code>.policy</code></b>.
 *
 * <p>
 **/
public class rule extends DefaultInternalAction{

    private boolean lastSendWasSynAsk = false;
    @Override
    public boolean suspendIntention() {
        return lastSendWasSynAsk;
    }

    @Override
    public boolean canBeUsedInContext() {
        return false;
    }

    @Override
    public int getMinArgs() {
        return 3;
    }

    @Override
    public int getMaxArgs() {
        return 5;
    }
    
    @Override
    public Object execute(final TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        ts.getUserAgArch().setFirewall("rule",args[0].toString());
        System.out.println(ts.getUserAgArch().getFirewall("rule"));
        //checkArguments(args);
        //String to = args[0].toString();
        //if (!to.startsWith("\"")) {
        //    to = "\"" + to + "\"";
        //}
        //Term ilf = args[1];
        //Term pcnt = args[2];
        //ts.getUserAgArch().getCommBridge().sendMsgToContextNet(ts.getUserAgArch().getCommBridge().getMyUUID(), to, ilf, pcnt);
        return true;
    }
}
