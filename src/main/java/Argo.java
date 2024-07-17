import java.util.logging.Logger;

public class Argo extends group.chon.agent.argo.Argo {
    @Override
    public void init() throws Exception {
        Logger logger = Logger.getLogger("ARGO");
        logger.severe("The agentArchClass \"Argo\" changed to \"jason.Argo\", please update your .MAS2J file. Consult: https://github.com/chon-group/Argo/wiki");
        super.init();
    }
}
