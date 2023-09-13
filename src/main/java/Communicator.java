import jason.AslTransferenceModel;
import jason.architecture.AgArch;
import jason.architecture.CommMiddleware;
import jason.architecture.TransportAgentMessageType;
import jason.infra.centralised.CentralisedAgArch;
import jason.infra.centralised.RunCentralisedMAS;
import jason.mas2j.ClassParameters;
import jason.runtime.RuntimeServicesInfraTier;
import jason.util.BioInspiredProtocolLogUtils;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Communicator extends AgArch {

    private CommMiddleware commBridge = null;
    private static final String AGENT_FILE_EXTENSION = ".asl";

    private String policyList;
    private String ruleList;
    @Override
    public void setFirewall(String tipo,String values) {
        if (tipo.equals("policy")) {
            this.policyList = values;
        } else if (tipo.equals("rule")) {
            this.ruleList = values;
        } else {
            System.out.println("deu erro");
        }
    }
    @Override
    public String getFirewall(String tipo) {
        if (tipo.equals("policy")) {
            return this.policyList;
        } else if (tipo.equals("rule")) {
            return this.ruleList;
        } else {
            return "deu erro";
        }
    }
    @Override
    public void connectCN(String gatewayIP, int gatewayPort, String myUUID) {
        this.commBridge = new CommMiddleware(gatewayIP, gatewayPort, myUUID);
        this.commBridge.setAgName(this.getAgName());
    }

    @Override
    public void disconnectCN() {
        if (this.commBridge != null) {
            this.commBridge.disconnect();
        }
    }

    @Override
    public CommMiddleware getCommBridge() {
        return this.commBridge;
    }

    @Override
    public void addMessageToC() {
        this.getTS().getC().addMsg(this.commBridge.checkMailCN());
        //this.commBridge.cleanMailBox();
    }

    @Override
    public void instantiateAgents() {
        if (this.commBridge.getProtocol().equals(TransportAgentMessageType.PREDATION.getName())) {
            this.executePredatorProtocol();
        } else if (this.commBridge.getProtocol().equals(TransportAgentMessageType.MUTUALISM.getName())) {
            this.executeMutualismProtocol();
        } else if (this.commBridge.getProtocol().equals(TransportAgentMessageType.INQUILINISM.getName())) {
            this.executeInquilinismProtocol();
        }
    }

    private int startAgent(String name, String path, String agArchClasse, int qtdAgentsInstantiated) {
        try {
            String agClass = null;
            List<String> agArchClasses = new ArrayList<String>();
            if(agArchClasse != null && !agArchClasse.isEmpty()) {
                agArchClasses.add(agArchClasse);
            }
            ClassParameters bbPars = null;

            RuntimeServicesInfraTier rs = this.getTS().getUserAgArch().getRuntimeServices();
            name = rs.createAgent(name, path, agClass, agArchClasses, bbPars, this.getTS().getSettings());
            rs.startAgent(name);
            qtdAgentsInstantiated++;
            return qtdAgentsInstantiated;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return qtdAgentsInstantiated;
    }

    private void executePredatorProtocol () {
        int qtdAgentsInstantiated = 0;
        for (AslTransferenceModel aslTransferenceModel : this.commBridge.getAgentsReceived()) {
            String name = aslTransferenceModel.getName();
            String path = getPath(name);
            String agArchClass = aslTransferenceModel.getAgentArchClass();

            qtdAgentsInstantiated = this.startAgent(name, path, agArchClass, qtdAgentsInstantiated);
        }

        if (qtdAgentsInstantiated == this.commBridge.getAgentsReceived().size()) {
            // Todos os agentes instanciados, enviando mensagem para deletar da origem
            this.commBridge.sendMsgToDeleteAllAgents();
            this.killAllAgents();
            // Apagando Variaveis do transporte
            this.commBridge.cleanAtributesOfTransference();
            BioInspiredProtocolLogUtils.LOGGER.info("The " + TransportAgentMessageType.PREDATION.getName()
                    + " protocol has finished instantiating all agents at " + LocalDateTime.now().format(
                            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss.SSS")));
        }
    }

    private void executeMutualismProtocol() {
        int qtdAgentsInstantiated = 0;
        for (AslTransferenceModel aslTransferenceModel : this.commBridge.getAgentsReceived()) {
            String name = aslTransferenceModel.getName();
            String path = getPath(name);
            String agArchClass = aslTransferenceModel.getAgentArchClass();

            qtdAgentsInstantiated = this.startAgent(name, path, agArchClass, qtdAgentsInstantiated);
        }
        if (qtdAgentsInstantiated == this.commBridge.getAgentsReceived().size()) {
            // Todos os agentes instanciados, enviando mensagem para deletar da origem
            this.commBridge.sendMsgToDeleteAllAgents();
            // Apagando Variáveis do transporte
            this.commBridge.cleanAtributesOfTransference();
            BioInspiredProtocolLogUtils.LOGGER.info("The " + TransportAgentMessageType.MUTUALISM.getName()
                    + " protocol has finished instantiating all agents at " + LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss.SSS")));
        }
    }

    private void executeInquilinismProtocol () {
        int qtdAgentsInstantiated = 0;
        for (AslTransferenceModel aslTransferenceModel : this.commBridge.getAgentsReceived()) {
            String name = aslTransferenceModel.getName();
            String path = getPath(name);
            String agArchClass = aslTransferenceModel.getAgentArchClass();

            qtdAgentsInstantiated = this.startAgent(name, path, agArchClass, qtdAgentsInstantiated);
        }
        if (qtdAgentsInstantiated == this.commBridge.getAgentsReceived().size()) {
            // Todos os agentes instanciados, enviando mensagem para deletar da origem
            this.commBridge.sendMsgToDeleteAllAgents();
            // Apagando Variáveis do transporte
            this.commBridge.cleanAtributesOfTransference();
            BioInspiredProtocolLogUtils.LOGGER.info("The " + TransportAgentMessageType.INQUILINISM.getName()
                    + " protocol has finished instantiating all agents at " + LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss.SSS")));
        }
    }

    private String getPath(String agentName) {
        String path = "";
        for (CentralisedAgArch centralisedAgArch : RunCentralisedMAS.getRunner().getAgs().values()) {
            path = centralisedAgArch.getTS().getAg().getASLSrc();
            path = path.substring(0, path.length() - (centralisedAgArch.getAgName() + AGENT_FILE_EXTENSION).length());
            break;
        }
        path += agentName + AGENT_FILE_EXTENSION;
        return path;
    }

    @Override
    public void killAllAgents() {
        Map<String, CentralisedAgArch> agentsOfTheSMA = RunCentralisedMAS.getRunner().getAgs();
        if (this.commBridge.getAgentsReceived() != null && !this.commBridge.getAgentsReceived().isEmpty()
                && this.commBridge.getAgentsReceived().size() > 0) {
            for (CentralisedAgArch centralisedAgArch : agentsOfTheSMA.values()) {
                if (!this.commBridge.getNameAgents().contains(centralisedAgArch.getAgName())) {
                    this.getTS().getUserAgArch().getRuntimeServices().killAgent(centralisedAgArch.getAgName(),
                            this.getTS().getUserAgArch().getAgName());
                    File file = new File(centralisedAgArch.getTS().getAg().getASLSrc());
                    this.commBridge.deleteFileAsl(file);
                }
            }
        } else {
            for (CentralisedAgArch centralisedAgArch : agentsOfTheSMA.values()) {
                if (this.commBridge.getNameAgents().contains(centralisedAgArch.getAgName())) {
                    this.getTS().getUserAgArch().getRuntimeServices().killAgent(centralisedAgArch.getAgName(),
                            this.getTS().getUserAgArch().getAgName());
                }
            }
            this.commBridge.cleanAtributesOfTransference();
        }
    }
}
