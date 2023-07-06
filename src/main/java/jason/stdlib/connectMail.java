package jason.stdlib;

import jason.architecture.EMailMiddleware;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.Message;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;

import java.util.ArrayList;

public class connectMail extends DefaultInternalAction {
    EMailMiddleware mail = new EMailMiddleware();
    @Override
    public Object execute(final TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        //Parametros recebimento - host, protocolo, porta, login, senha, ssltrust, option;

        mail.setHost(args[0].toString());
        mail.setProtocol(args[1].toString());
        mail.setPort(args[2].toString());
        mail.setLogin(args[3].toString());
        mail.setPassword(args[4].toString());
        mail.setSsltrust(args[5].toString());
        option(Integer.parseInt(args[6].toString()));

        ArrayList<Message> list = mail.checkMail();
        //for (Message item: list) System.out.println(item.toString());
        return true;
    }

    public void option(int op){
        // set properties true/false depending of the binary number
        // eg: 8 = 111 = all true
        mail.setAuth((op & 0b100) != 0);
        mail.setStarttls((op & 0b010) != 0);
        mail.setSslenable((op & 0b001) != 0);
    }
}


//        if(auth){
//                properties.put("mail"+protocol+"auth", auth);
//                }
//                if (starttls) {
//                properties.put("mail."+protocol+".starttls.enable",starttls);
//                }
//                if (sslenable) {
//                properties.put("mail."+protocol+".ssl.enable",sslenable);
