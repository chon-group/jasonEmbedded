import jason.architecture.AgArch;
import jason.asSemantics.Message;
import jason.architecture.EMailMiddleware;
import java.util.ArrayList;


public class Communicator extends AgArch {

    private EMailMiddleware emailBridge = null;

    public Communicator(){
        super();
        this.emailBridge = new EMailMiddleware();
    }

    @Override
    public void addMessageToC() {
        if (this.emailBridge.getLogin()!=null && this.emailBridge.getPassword()!=null
                && this.emailBridge.isRPropsEnable() && this.emailBridge.isRHostEnable()){
            ArrayList<Message> list = this.emailBridge.checkEMail();
            for (Message item : list) {
                this.getTS().getC().addMsg(item);
            }
        }
    }
    @Override
    public EMailMiddleware getEmailBridge() {
        return this.emailBridge;
    }

}

