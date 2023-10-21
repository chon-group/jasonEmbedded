package jason.stdlib;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.Message;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;
import group.chon.pythia.inmetGovBR.InmetRSS;
import group.chon.pythia.inmetGovBR.InmetAlert;
import java.text.Normalizer;

public class inmetGovBrCheck extends DefaultInternalAction {
    private InmetAlert inmetAlert = null;
    private InmetRSS inmetRSS = null;

    @Override
    public Object execute(TransitionSystem ts, Unifier un, Term[] args) throws Exception {
       inmetRSS = new InmetRSS(args[0].toString().replace("\"",""));
        String strBelief;
        while (inmetRSS.getHasNewItem()) {
            inmetAlert = inmetRSS.getLastUnperceivedAlert(Integer.parseInt(args[1].toString()));
            if(inmetAlert!=null){
                strBelief = "inmetAlert("+ inmetAlert.getId() +
                        "," + strToTerm(inmetAlert.getEvent())+
                        "," + strToTerm(inmetAlert.getSeverity()) +
                        "," + strToTerm(inmetAlert.getCertainty()) +
                        "," + whenWillEvent(inmetAlert.getTimeStampDateOnSet(), inmetAlert.getTimeStampDateExpires())+
                        "," + strToTerm(inmetAlert.getResponseType()) +
                        ",\"" + inmetAlert.getDescription()+"\""+
                        ",\"" + inmetAlert.getInstruction()+"\"" +
                        ",\"" + inmetAlert.getWeb()+"\""+
                        ")";
                //ts.getAg().getBB().add(Literal.parseLiteral(strBelief));
                Message m = new Message("tell",
                        "inmetGovBR",
                        ts.getUserAgArch().getAgName(),
                        Literal.parseLiteral(strBelief));
                ts.getUserAgArch().sendMsg(m);

            }
        }
        return true;
    }

    private String whenWillEvent(Long timeStampDateOnSet, Long timeStampDateExpires){
        if(inmetRSS.isFuture(timeStampDateOnSet,timeStampDateExpires)){
            return "future";
        } else if (inmetRSS.isRightNow(timeStampDateOnSet,timeStampDateExpires)) {
            return "rightNow";
        }else{
            return "past";
        }

    }

    private String strToTerm(String inputStr){
        String strAux = Normalizer.normalize(inputStr, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
        inputStr = strAux.replaceAll(" ", "");
        strAux = inputStr.substring(0, 1).toLowerCase() + inputStr.substring(1);
        return strAux;
    }
    
}