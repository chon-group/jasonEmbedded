package jason.stdlib;

import group.chon.agent.hermes.jasonStdLib.configureContextNetConnection;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;

import java.util.logging.Logger;

public class connectCN extends group.chon.agent.hermes.jasonStdLib.connect {
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        /* In next version.... */
//        Logger logger = Logger.getLogger("HERMES");
//        logger.severe("The internal action \".connectCN\" changed to \".hermes.configureContextNetConnection\", please update your .ASL file. Consult: https://github.com/chon-group/Hermes/wiki");

        Term[] newArgs = createNewArray(args);
        DefaultInternalAction hermesConfigureCN = new configureContextNetConnection();
        hermesConfigureCN.execute(ts,un,newArgs);

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
