package jason.stdlib;

import jason.JasonException;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;

public class filter extends DefaultInternalAction {

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws JasonException {
        if (args.length > 1 && args.length <= 3) {
            String filter = args[0].toString();
            String type = args[1].toString();
            if (filter.equals("remove")) {
                if (args.length == 2) {
                    if (type.equals("all") || type.equals("except") || type.equals("only")
                            || type.equals("value")) {
                        ts.getAg().changeFilter(filter);
                        return true;
                    } else {
                        System.out.println(
                                "You must use 'all', 'except', 'only' or 'value' as the second parameter to remove filters.");
                        return false;
                    }
                } else {
                    System.out.println("The parameter length must be two while removing a filter.");
                }
            } else {
                if (filter.equals("except") || filter.equals("only") || filter.equals("value")) {
                    if (args[1].isPred() || args[1].isAtom() || args[1].isList()) {
                        if (args.length == 2 && !filter.equals("value")) {
                            // Term perception = args[1];
                            return true;
                        } else {
                            if (!args[1].isList() && args[2].isStructure()) {
                                return true;
                            } else {
                                System.out.println(
                                        "The third parameter must be an arithmetic expression and the second parameter must not be a list.");
                                return false;
                            }
                        }
                    } else {
                        System.out.println("The argument must be a valid predicate beginning with lower case.");
                        return false;
                    }
                } else {
                    System.out.println(
                            "You must use 'except', 'only' and 'value' as the second parameter to add filters.");
                    return false;
                }
            }
        } else {
            System.out.println("The parameters length must be between 2 and 3.");
            return false;
        }
        return false;
    }

}
