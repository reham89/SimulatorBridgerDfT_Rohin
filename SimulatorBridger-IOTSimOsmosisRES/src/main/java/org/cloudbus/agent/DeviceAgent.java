package org.cloudbus.agent;

import uk.ncl.giacomobergami.components.iot.IoTDevice;

public class DeviceAgent extends AbstractAgent{
    private IoTDevice ioTDevice;

    public DeviceAgent(IoTDevice ioTDevice) {
        this.ioTDevice = ioTDevice;
    }

    public DeviceAgent() {
        //This is necessary for dynamic agent instance creation.
    }

    public void setIoTDevice(IoTDevice ioTDevice) {
        this.ioTDevice = ioTDevice;
    }

    public IoTDevice getIoTDevice() {
        return ioTDevice;
    }

    @Override
    public void monitor() {
        //Collect information from the entity.
    }

    @Override
    public void analyze() {
        //Analyze the facts and share information with the neighbouting agents.

    }

    @Override
    public void plan() {
        //Plan actions based on the locally collected informations and from the messages received from other agents.

    }

    @Override
    public void execute() {
        //Execute planned actions on the local entity.

    }
}
