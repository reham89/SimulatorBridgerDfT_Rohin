package org.cloudbus.agent;

import org.cloudbus.cloudsim.core.MainEventManager;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class AbstractAgent implements Agent {
    private List<AgentMessage> inQueue = new ArrayList<>();
    private String name;
    private long mID=0;
    private double getTime;

    @Override
    public double getCurrentTime() { return getTime; }
    @Override
    public void setCurrentTime(double lastMAPEloop) { this.getTime = lastMAPEloop; }

    public AgentMessage newAgentMessage(){
        AgentMessage message = AgentBroker.getInstance().createEmptyMessage();
        message.setID(mID);
        message.setTIMESTAMP(MainEventManager.clock());
        message.setSOURCE(name);
        mID++;
        return message;
    }

    public void publishMessage(AgentMessage message){
        //Publish message is managed by the Osmotic Broker.
        AgentBroker.getInstance().distributeMessage(message);
    }

    public List<AgentMessage> getReceivedMessages(){
        List<AgentMessage> messages = new ArrayList<>(inQueue);
        inQueue.clear();
        return messages;
    }

    public <v> List<v> getReceivedMessages(Function<AgentMessage, v> f){
        List<v> messages = new ArrayList<>();
        inQueue.forEach(msg -> messages.add(f.apply(msg)));
        inQueue.clear();
        return messages;
    }

    public void receiveMessage(AgentMessage message){
        inQueue.add(message);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
