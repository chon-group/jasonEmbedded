// Copyright (C) 2003  Rafael H. Bordini, Jomi F. Hubner, et al.
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
// To contact the authors:
// http://www.inf.ufrgs.br/~bordini
// http://www.das.ufsc.br/~jomi
//
//----------------------------------------------------------------------------

package jason.architecture;

import br.pro.turing.javino.Javino;
import jason.JasonException;
import jason.asSemantics.ActionExec;
import jason.asSemantics.Message;
import jason.asSemantics.TransitionSystem;
import jason.asSyntax.Literal;
import jason.infra.centralised.CentralisedAgArch;
import jason.mas2j.ClassParameters;
import jason.profiling.QueryProfiling;
import jason.runtime.RuntimeServicesInfraTier;
import jason.runtime.Settings;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Base agent architecture class that defines the overall agent architecture;
 * the AS interpreter is the reasoner (a kind of mind) within this architecture
 * (a kind of body).
 *
 * <p>
 * The agent reasoning cycle (implemented in TransitionSystem class) calls these
 * methods to get perception, action, and communication.
 *
 * <p>
 * This class implements a Chain of Responsibilities design pattern. Each member
 * of the chain is a subclass of AgArch. The last arch in the chain is the
 * infrastructure tier (Centralized, JADE, Saci, ...). The getUserAgArch method
 * returns the first arch in the chain.
 * <p>
 * Users can customize the architecture by overriding some methods of this
 * class.
 */
public class AgArch implements AgArchInfraTier, Comparable<AgArch> {

    //[Pantoja]
    private String port = "";
    //***

    private TransitionSystem ts = null;

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    /**
     * Successor in the Chain of Responsibility
     */
    private AgArch successor = null;

    private AgArch firstArch = null;

    /** the current cycle number, in case of sync execution mode */
    private int cycleNumber = 0;

    public Javino getArgo() {
        return null;
    }

    public AgArch() {
        firstArch = this;
    }

    /**
     * @deprecated for arch initialisation you should override the init()
     * method.
     */
    public void initAg(String agClass, ClassParameters bbPars, String asSrc, Settings stts) throws JasonException {
        // Agent.create(this, agClass, bbPars, asSrc, stts);
    }

    public void init() throws Exception {
    }

    /**
     * A call-back method called by the infrastructure tier when the agent is
     * about to be killed.
     */
    public void stop() {
        if (successor != null) {
            successor.stop();
        }
    }

    // Management of the chain of responsibility

    /** Returns the first architecture in the chain of responsibility pattern */
    public AgArch getFirstAgArch() {
        return firstArch;
    }

    public AgArch getNextAgArch() {
        return successor;
    }

    public List<String> getAgArchClassesChain() {
        List<String> all = new ArrayList<String>();
        AgArch a = getFirstAgArch();
        while (a != null) {
            all.add(0, a.getClass().getName());
            a = a.getNextAgArch();
        }
        return all;
    }

    public void insertAgArch(AgArch arch) {
        if (arch != firstArch) // to avoid loops
        {
            arch.successor = firstArch;
        }
        if (ts != null) {
            arch.ts = this.ts;
            ts.setAgArch(arch);
        }
        setFirstAgArch(arch);
    }

    private void setFirstAgArch(AgArch arch) {
        firstArch = arch;
        if (successor != null) {
            successor.setFirstAgArch(arch);
        }
    }

    public void createCustomArchs(List<String> archs) throws Exception {
        if (archs == null) {
            return;
        }
        for (String agArchClass : archs) {
            // user custom arch
            if (!agArchClass.equals(AgArch.class.getName()) && !agArchClass.equals(CentralisedAgArch.class.getName())) {
                try {
                    AgArch a = (AgArch) Class.forName(agArchClass).newInstance();
                    a.setTS(ts); // so a.init() can use TS
                    a.initAg(null, null, null, null); // for compatibility
                    // reasons
                    insertAgArch(a);
                    a.init();
                } catch (Exception e) {
                    System.out.println("Error creating custom agent.asl aarchitecture." + e);
                    e.printStackTrace();
                    ts.getLogger().log(Level.SEVERE, "Error creating custom agent.asl aarchitecture.", e);
                }
            }
        }
    }

    /**
     * A call-back method called by TS when a new reasoning cycle is starting
     */
    public void reasoningCycleStarting() {
        QueryProfiling q = getTS().getAg().getQueryProfiling();
        if (q != null) {
            q.setNbReasoningCycles(getCycleNumber());
        }
        if (successor != null) {
            successor.reasoningCycleStarting();
        }
    }

    /**
     * returns the last arch in the chain, which is supposed to be the infra
     * tier
     */
    public AgArchInfraTier getArchInfraTier() {
        if (this.successor == null) {
            return this;
        } else {
            return successor.getArchInfraTier();
        }
    }

    public TransitionSystem getTS() {
        if (ts != null) {
            return ts;
        }
        if (successor != null) {
            return successor.getTS();
        }
        return null;
    }

    public void setTS(TransitionSystem ts) {
        this.ts = ts;
        if (successor != null) {
            successor.setTS(ts);
        }
    }

    /**
     * Gets the agent's perception as a list of Literals. The returned list will
     * be modified by Jason.
     */
    public List<Literal> perceive() {
        if (successor == null) {
            return null;
        } else {
            return successor.perceive();
        }
    }

    /**
     * Reads the agent's mailbox and adds messages into the agent's circumstance
     */
    public void checkMail() {
        if (successor != null) {
            successor.checkMail();
        }
    }

    /**
     * Executes the action <i>action</i> and, when finished, add it back in
     * <i>feedback</i> actions.
     */
    // TODO: remove feedback parameter and use getTS().addFeedbackAction in a
    // new method of this class to be executed when the action is finished (as
    // actionExecuted of centralised)
    // for jason 2.0 to avoid backward compatibility problems
    public void act(ActionExec action, List<ActionExec> feedback) {
        if (successor != null) {
            successor.act(action, feedback);
        }
    }

    /** Returns true if the agent can enter in sleep mode. */
    public boolean canSleep() {
        return (successor == null) || successor.canSleep();
    }

    /** Puts the agent in sleep. */
    public void sleep() {
        if (successor != null) {
            successor.sleep();
        }
    }

    public void wake() {
        if (successor != null) {
            successor.wake();
        }
    }

    public RuntimeServicesInfraTier getRuntimeServices() {
        if (successor == null) {
            return null;
        } else {
            return successor.getRuntimeServices();
        }
    }

    /** Gets the agent's name */
    public String getAgName() {
        if (successor == null) {
            return "no-named";
        } else {
            return successor.getAgName();
        }
    }

    /** Sends a Jason message */
    public void sendMsg(Message m) throws Exception {
        if (successor != null) {
            successor.sendMsg(m);
        }
    }

    /** Broadcasts a Jason message */
    public void broadcast(Message m) throws Exception {
        if (successor != null) {
            successor.broadcast(m);
        }
    }

    /** Checks whether the agent is running */
    public boolean isRunning() {
        return successor == null || successor.isRunning();
    }

    /** sets the number of the current cycle */
    public void setCycleNumber(int cycle) {
        cycleNumber = cycle;
        if (successor != null) {
            successor.setCycleNumber(cycle);
        }
    }

    public void incCycleNumber() {
        setCycleNumber(cycleNumber + 1);
    }

    /** gets the current cycle number */
    public int getCycleNumber() {
        return cycleNumber;
    }

    @Override
    public String toString() {
        return "arch-" + getAgName();
    }

    @Override
    public int hashCode() {
        return getAgName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj instanceof AgArch) {
            return this.getAgName().equals(((AgArch) obj).getAgName());
        }
        return false;
    }

    public int compareTo(AgArch o) {
        return getAgName().compareTo(o.getAgName());
    }

    public CommMiddleware getCommBridge() {
        //CommMiddleware commBridge = new CommMiddleware();

        return null;
    }

    public void addMessageToC() {
    }

    public void instantiateAgents() {}

    public void killAllAgents () {}

    public void connectCN(String gatewayIP, int gatewayPort, String myUUID) {}

    public void disconnectCN() {}

    public EMailMiddleware getEmailBridge(){
        return null;
    }
}