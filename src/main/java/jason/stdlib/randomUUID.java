package jason.stdlib;

import jason.asSemantics.*;
import jason.asSyntax.*;
import java.util.UUID;


public class randomUUID extends DefaultInternalAction {

    @Override
    public Object execute(final TransitionSystem ts, final Unifier un, final Term[] args) throws Exception {
        if (args.length == 1) {
            UUID uuid = UUID.randomUUID();
            return un.unifies(args[0],ASSyntax.parseTerm("\""+uuid.toString()+"\"") );
        }else{
            return null;
        }
    }
}
