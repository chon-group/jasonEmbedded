package jason.stdlib;

import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.Message;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;
import group.chon.pythia.inmetGovBR.*;

import java.util.ArrayList;

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
                        "," + convertString(inmetAlert.getEvent())+
                        "," + convertString(inmetAlert.getSeverity()) +
                        "," + convertString(inmetAlert.getCertainty()) +
                        ","+temporalidade(inmetAlert.getTimeStampDateOnSet(), inmetAlert.getTimeStampDateExpires())+
                        "," + convertString(inmetAlert.getResponseType()) +
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

    private String temporalidade(Long timeStampDateOnSet, Long timeStampDateExpires){
        if(inmetRSS.isFuture(timeStampDateOnSet,timeStampDateExpires)){
            return "future";
        } else if (inmetRSS.isRightNow(timeStampDateOnSet,timeStampDateExpires)) {
            return "rightNow";
        }else{
            return "past";
        }

    }

    private String convertString(String inputStr){
        return inputStr.substring(0, 1).toLowerCase() + removeEspaco(inputStr.substring(1));
    }

    private String removeEspaco(String inputStr){
        return inputStr.replaceAll(" ", "");
    }
}