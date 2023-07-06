package jason.stdlib;

import jason.architecture.EMailMiddleware;
import jason.asSemantics.DefaultInternalAction;
import jason.asSemantics.Message;
import jason.asSemantics.TransitionSystem;
import jason.asSemantics.Unifier;
import jason.asSyntax.Term;

import java.util.ArrayList;

public class sendMail extends DefaultInternalAction {
    EMailMiddleware mail = new EMailMiddleware();
    @Override
    public Object execute(final TransitionSystem ts, Unifier un, Term[] args) throws Exception {
        //Parametros envio - Hots, Porta, login, senha, destinatário, assunto, mensagem, options
        if (args.length == 8) {
            mail.setProtocol("smtp");
            mail.setHost(args[0].toString());
            mail.setPort(args[1].toString());
            mail.setLogin(args[2].toString());
            mail.setPassword(args[3].toString());
            option(Integer.parseInt(args[7].toString()));
            mail.sendMsg(args[4].toString(), args[5].toString(), args[6].toString());
            return true;
        } else return false;

    }

    public void option(int op){
        // set properties true/false depending of the binary number
        // eg: 3 = 11 = all true
        mail.setAuth((op & 0b100) != 0);
        mail.setStarttls((op & 0b010) != 0);
    }


}
