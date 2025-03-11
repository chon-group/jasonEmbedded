package jason.stdlib;

import group.chon.agent.hermes.jasonStdLib.configureContextNetConnection;
import group.chon.agent.hermes.jasonStdLib.disconnect;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;

import java.util.logging.Logger;

public class disconnectCN extends group.chon.agent.hermes.jasonStdLib.disconnect{
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {

        //Logger logger = Logger.getLogger(ts.getAgArch().getAgName());
        //logger.info("Disconnecting from contextNet IoT Network...");

        Term[] onlyOneArg = new Term[1];
        onlyOneArg[0] = Literal.parseLiteral("contextNet");
        return super.execute(ts,un,onlyOneArg);
    }

    private Term[] createNewArray(Term[] args) {
        Term[] newArray = new Term[args.length + 1];
        newArray[0] = Literal.parseLiteral("contextNet");
        System.arraycopy(args, 0, newArray, 1, args.length);
        return newArray;
    }
}
