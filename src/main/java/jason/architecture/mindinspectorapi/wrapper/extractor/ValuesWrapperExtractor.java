package jason.architecture.mindinspectorapi.wrapper.extractor;

import jason.NoValueException;
import jason.architecture.mindinspectorapi.wrapper.model.value.ValueWrapper;
import jason.architecture.mindinspectorapi.wrapper.model.value.VarWrapper;
import jason.asSemantics.Unifier;
import jason.asSyntax.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Extraí os valores de um termo.
 */
public class ValuesWrapperExtractor {

    /**
     * Extrái os valores (ou variáveis) de um termo.
     *
     * @param values      Lista de valores para ser preenchida.
     * @param currentTerm Termo atual.
     */
    public void extract(List<ValueWrapper> values, Term currentTerm, Unifier unifier) {
        if (currentTerm instanceof VarTerm) {
            if (currentTerm instanceof UnnamedVar) {
                return;
            }

            Term currentTermValue = unifier.get((VarTerm) currentTerm);
            if (currentTermValue == null) {
                values.add(new VarWrapper(currentTerm.toString(), null));
                return;
            }

            VarWrapper var;
            if (currentTermValue instanceof ListTermImpl) {
                List<ValueWrapper> listVars = new ArrayList<>();

                Term listNext = currentTermValue;
                while (listNext != null) {
                    this.extract(listVars, ((ListTermImpl) listNext).getTerm(), unifier);
                    listNext = ((ListTermImpl) listNext).getNext();
                }

                var = new VarWrapper(currentTerm.toString(), listVars);
            } else if (currentTermValue instanceof NumberTermImpl) {
                try {
                    var = new VarWrapper(currentTerm.toString(), Long.parseLong(currentTermValue.toString()));
                } catch (NumberFormatException e) {
                    var = new VarWrapper(currentTerm.toString(), Double.parseDouble(currentTermValue.toString()));
                }
            } else {
                var = new VarWrapper(currentTerm.toString(), currentTermValue.toString());
            }

            if (!values.contains(var)) {
                values.add(var);
            }
        } else if (currentTerm instanceof Structure) {
            if (currentTerm instanceof LiteralImpl) {
                final String[] currentTermString = {currentTerm.toString()};
                if (((LiteralImpl) currentTerm).hasAnnot()) {
                    currentTermString[0] = new SourceWrapperExtractor().removeAllSourcesFromStructure(
                            currentTermString[0]);
                }

                List<ValueWrapper> valueWrappers = new ArrayList<>();

                for (Term term : ((LiteralImpl) currentTerm).getTerms()) {
                    this.extract(valueWrappers, term, unifier);
                }
                if (!valueWrappers.isEmpty()) {
                    values.add(new VarWrapper(currentTermString[0], valueWrappers));
                } else {
                    values.add(new ValueWrapper(currentTermString[0]));
                }
                return;
            } else if (currentTerm instanceof ListTermImpl) {
                Term listNext = currentTerm;
                List<ValueWrapper> valueWrappers = new ArrayList<>();
                while (listNext != null) {
                    if (listNext instanceof ListTermImpl) {
                        this.extract(valueWrappers, ((ListTermImpl) listNext).getTerm(), unifier);
                        listNext = ((ListTermImpl) listNext).getNext();
                    } else {
                        this.extract(valueWrappers, listNext, unifier);
                        break;
                    }
                }
                values.add(new VarWrapper(currentTerm.toString(), valueWrappers));
                return;
            } else if (currentTerm instanceof ArithExpr) {
                try {
                    double solve = ((ArithExpr) currentTerm).solve();
                    values.add(new VarWrapper(currentTerm.toString(), solve));
                } catch (NoValueException e) {
                    values.add(new VarWrapper(currentTerm.toString(), null));
                }
            }
            for (Term term : ((Structure) currentTerm).getTerms()) {
                this.extract(values, term, unifier);
            }
        } else if (currentTerm instanceof NumberTermImpl) {
            ValueWrapper value;
            try {
                value = new ValueWrapper(Long.parseLong(currentTerm.toString()));
            } catch (NumberFormatException e) {
                value = new ValueWrapper(Double.parseDouble(currentTerm.toString()));
            }
            values.add(value);
        } else if (currentTerm instanceof StringTermImpl | currentTerm instanceof Atom) {
            values.add(new ValueWrapper(currentTerm.toString()));
        }
    }

}
