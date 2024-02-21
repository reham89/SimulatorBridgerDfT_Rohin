/*
 * Title:        IoTSim-Osmosis 1.0
 * Description:  IoTSim-Osmosis enables the testing and validation of osmotic computing applications 
 * 			     over heterogeneous edge-cloud SDN-aware environments.
 * 
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2020, Newcastle University (UK) and Saudi Electronic University (Saudi Arabia) 
 * 
 */

package org.cloudbus.osmosis.core;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import org.cloudbus.cloudsim.core.MainEventManager;
import org.cloudbus.cloudsim.sdn.Channel;
import org.cloudbus.cloudsim.sdn.Link;
import org.cloudbus.cloudsim.sdn.NetworkNIC;
import org.cloudbus.cloudsim.sdwan.NetworkMeasurement;
import org.cloudbus.cloudsim.sdwan.TCP;
import org.cloudbus.cloudsim.sdwan.UDP;

/**
 * 
 * @author Khaled Alwasel
 * @contact kalwasel@gmail.com
 * @since IoTSim-Osmosis 1.0
 * 
**/

public class Flow implements Serializable{
	
	private String appName;	
	private int flowId;	
	int source ; // --> mapper, reducer, HDFS, etc. 
	int destination; // --> mapper, reducer, VM, etc.	
	long flowSize;
	double amountToBeProcessed;
	private double transmissionTime;
	public long requestedBW =0;
	private double submitTime = 0;
	private double startTime = -1;
	private double finishTime = -1;
	private String appNameSrc;

	private String actualEdgeDevice;

	private String appNameDest;
	private String flowType;
	private OsmoticAppDescription app;
	private boolean isScheduled = false;
	private int osmesisAppId;
	private int appPriority;
	private int ackEntity; // used to notify the respective destination of packet's completion -- khaled
	private int datacenterId;
	private double flowBandwidth = 0;

	private String labelPlace; // used to identify the desintation of the flow (inside datacenters or outside datacenters)

	private long osmesisEdgeletSize;
	private WorkflowInfo workflowTag;
	private String datacenterName;

	public static int resolutionPlaces = 5;
	public static int timeUnit = 1;	// 1: sec, 1000: msec
	private double previousTime;
	private Channel channel;
	private List<Link> linkList;
	private List<NetworkNIC> nodeOnRouteList; // this is an end to end route (edge, sdwan, and cloud)
	private TCP tcp;
	private UDP udp;
	private int pktGlobalID;
	private List<SDNController> controllerList = new ArrayList<>();
	private int totalPktNum;
	private int totalFrNum;
	private double totalDelay;
	long totalHeaders;

	int tcpPacketSize = 12000; // 1500KB
	int tcpPacketNo; // size/ tcpPacketSize

	public double networkTransferTime;
	private boolean sourceSent;
	private boolean packetOnWAN;
	private boolean destReceived;

	/*
	 * The bw variables are used to determine which one has less bw,
	 * which will be used as the main flow bw
	 */
	private double sourceBw;
	private double destBw;

	//////////////////////////////
	private double edgeToWANBW;
	private double latency;


	public Flow(String vmNameSrc,
				String vmNameDest,
				int source,
				int destination,
				int flowId,
				String flowType, OsmoticAppDescription app) {
		this.appNameSrc = vmNameSrc;
		this.appNameDest = vmNameDest;
		this.source = source;
		this.destination = destination;
		this.flowId = flowId;
		this.flowType = flowType;
		this.app = app;
		this.actualEdgeDevice = "N/A";
	}

	public String getActualEdgeDevice() {
		return actualEdgeDevice;
	}

	public void setActualEdgeDevice(String actualEdgeDevice) {
		this.actualEdgeDevice = actualEdgeDevice;
	}

	public double getTransmissionTime() {
		return transmissionTime;
	}

	public void setTransmissionTime(double transmissionTime) {
		this.transmissionTime = transmissionTime - this.startTime;
	}

	public List<NetworkNIC> getNodeOnRouteList() {
		return nodeOnRouteList;
	}

	public void setNodeOnRouteList(List<NetworkNIC> nodeOnRouteList) {
		if(this.nodeOnRouteList == null){
			this.nodeOnRouteList = new ArrayList<>();
		}
		this.nodeOnRouteList.addAll(nodeOnRouteList);
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public List<Link> getLinkList() {
		return linkList;
	}

	public void setLinkList(List<Link> linkList) {
		if(this.linkList == null){
			this.linkList = new ArrayList<>();
		}
		this.linkList.addAll(linkList);
	}

	public int getDatacenterId() {
		return datacenterId;
	}

	public void setDatacenterId(int setDatacenterId) {
		this.datacenterId = setDatacenterId;
	}

	public String getFlowType() {
		return flowType;
	}

	public int getAckEntity() {
		return ackEntity;
	}

	public void setAckEntity(int ackEntity) {
		this.ackEntity = ackEntity;
	}

	public int getAppPriority() {
		return appPriority;
	}

	public void setAppPriority(int appPriority) {
		this.appPriority = appPriority;
	}

	public boolean isScheduled() {
		return isScheduled;
	}

	public void setIsScheduled(boolean isScheduled) {
		this.isScheduled = isScheduled;
	}

	public void setOsmesisAppId(int appId){
		this.osmesisAppId = appId;
	}
	public int getOsmesisAppId(){
		return this.osmesisAppId;
	}

	public void setAppName(String appName){
		this.appName = appName;
	}
	public String getAppName(){
		return this.appName;
	}

	public int getOrigin() {
		return source;
	}

	public int getDestination() {
		return destination;
	}

	public void setDestination(int destination) {
		this.destination = destination;
	}

	public long getSize() {
		return flowSize;
	}

	public void setFlowSize(long size) {
		this.flowSize = size;
	}

	public int getFlowId() {
		return flowId;
	}

	public void setFlowId(int flowId) {
		this.flowId = flowId;
	}

	public void setStartTime(double time) {
		this.startTime = time;
	}

	public void setFinishTime(double time) {
		this.finishTime = time;
	}

	public double getStartTime() {
		return this.startTime;
	}
	public double getFinishTime() {
		return this.finishTime;
	}


	public String getAppNameSrc(){
		return this.appNameSrc;
	}

	public String getAppNameDest(){
		return this.appNameDest;
	}

	public void setAppNameDest(String appNameDest){
		this.appNameDest = appNameDest;
	}

	public double getAmountToBeProcessed() {
		return this.amountToBeProcessed;
	}

	public void addCompletedLength(double completed){
		amountToBeProcessed = amountToBeProcessed - completed;
		if (amountToBeProcessed <= 0){
			amountToBeProcessed = 0;
		}
	}

	public boolean isCompleted(){
		return amountToBeProcessed == 0;
	}

	public double getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(double submitTime) {
		this.submitTime = submitTime;
	}

	public int getPktGlobalID() {
		return pktGlobalID;
	}

	long pktSizeNoHeaders; // taken from transmission class
	public long getPktSizeNoHeaders() {
		return pktSizeNoHeaders;
	}


	public boolean isSourceSent() {
		return sourceSent;
	}

	public void setSourceSent(boolean sourceSent) {
		this.sourceSent = sourceSent;
	}

	public boolean isPacketOnWAN() {
		return packetOnWAN;
	}

	public void setPacketOnWAN(boolean packetOnWAN) {
		this.packetOnWAN = packetOnWAN;
	}

	public boolean isDestReceived() {
		return destReceived;
	}

	public void setDestReceived(boolean reachDest) {
		this.destReceived = reachDest;
	}

	public int noOfPackets() {
		this.tcpPacketNo = (int) (this.flowSize / this.tcpPacketSize);
		return tcpPacketNo;
	}

	public void setTransportProtocol(NetworkMeasurement protcol) {
		if (protcol instanceof TCP) {
			this.tcp = (TCP) protcol;
		} else {
			this.udp = (UDP) protcol;
		}
	}

	public NetworkMeasurement getTransportProtocol() {
		if (this.tcp == null) {
			return this.udp;
		}
		return this.tcp;
	}

	public void addSDNController(SDNController sdnController) {
		controllerList.add(sdnController);
	}

	public List<SDNController> getControllerList() {
		return this.controllerList;
	}

	public void computePktNum(){
		this.totalPktNum = (int) Math.ceil((getPktSizeNoHeaders() / getTransportProtocol().getAveragePktSize()));
	}

	public int getTotalPktNum() {
		return totalPktNum;
	}

	public void computeFrNum(){
		this.totalFrNum = (int) (getPktSizeNoHeaders() / getTransportProtocol().getAveragefrSize());

	}

	public int getTotalFrNum() {
		return totalFrNum;
	}

	public void computeTotalDelay() {
		totalDelay = totalFrNum *  getTransportProtocol().getDelays();
	}

	public double getTotalDelay() {
		return totalDelay;
	}

	public void addHeaderSizes(){
		totalHeaders = (long) (totalFrNum * getTransportProtocol().getTotalHeaders()); // bit
		totalHeaders = totalHeaders * 8; // bit
		amountToBeProcessed = getAmountToBeProcessed() + totalHeaders; //add TCP/UDP header data
	}

	public double getHeaderBytes(){
		double inByte = (double)  totalHeaders / (8000000);
		return inByte;
	}

	public void updateBandwidth() {
		double smallestBw = Double.MAX_VALUE;
		if(this.getSourceBw() < smallestBw){
			smallestBw = this.getSourceBw();
		}

		if(this.getDestBw() < smallestBw){
			smallestBw = this.getDestBw();
		}

		this.flowBandwidth = smallestBw;
	}

	public OsmoticAppDescription getApp() {
		return app;
	}

	public void setApp(OsmoticAppDescription app) {
		this.app = app;
	}

	public double FinishingTime() {
		double latency = this.latency <= 0 ? (double) 1/this.flowBandwidth : this.latency;
		return round(latency * amountToBeProcessed);
	}

	public void setPreviousTime(double previousTime) {
		this.previousTime = previousTime;
	}

	public void addPacketSize(long pktSize){
		this.flowSize = pktSize; // for printing
		this.amountToBeProcessed = pktSize;
	}

	public boolean updateTransmission(){
		double currentTime = MainEventManager.clock();
		double timeSpent = round(currentTime - this.previousTime);

		if(timeSpent <= 0 || this.previousTime == 0){
			return false;	// Nothing changed
		}

		previousTime = currentTime;

		double completed = timeSpent * this.flowBandwidth;

		amountToBeProcessed = amountToBeProcessed - completed;
		if (amountToBeProcessed <= 0){
			amountToBeProcessed = 0;
		}

		if (amountToBeProcessed == 0){
			transmissionTime  = currentTime - this.startTime; // time spent
			return true;
		}
		return false;
	}

	public static double round(double value) {
		if(value == 0) return value;
		int places = resolutionPlaces;
	    if (places < 0) throw new IllegalArgumentException();

		if(timeUnit >= 1000) value = Math.floor(value * timeUnit);
		
	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.CEILING);
	    return bd.doubleValue();
	}

	public void setLabelPlace(String label) {
		this.labelPlace = label;		
	}

	public String getLabelPlace(){
		return this.labelPlace;
	}

	public long getOsmesisEdgeletSize() {
		return osmesisEdgeletSize;
	}

	public void setOsmesisEdgeletSize(long osmesisEdgeletSize) {
		this.osmesisEdgeletSize = osmesisEdgeletSize;
	}

	public WorkflowInfo getWorkflowTag() {
		return workflowTag;
	}

	public void setWorkflowTag(WorkflowInfo workflowTag) {
		this.workflowTag = workflowTag;
	}

	public String getDatacenterName() {
		return datacenterName;
	}
	
	public void setDatacenterName(String name) {
		this.datacenterName = name;
	}	

	public double getFlowBandwidth() {
		return flowBandwidth;
	}
	
	public double getSourceBw() {
		return sourceBw;
	}

	public void updateSourceBw(double sourceBw) {
		this.sourceBw = sourceBw;
	}
	
	public double getDestBw() {
		return destBw;
	}

	public void updateDestBw(double destBw) {
		this.destBw = destBw;
	}

	///////////////////////////////////////////////////
	public double getEdgeToWANBW() {
		return edgeToWANBW;
	}

	public void updateEdgeToWANBW(double edgeToWANBW) {
		this.edgeToWANBW = edgeToWANBW;
	}

	public double getFlowLatency() {return latency; }

	public void updateFlowLatency(double latency) { this.latency = latency; }
	///////////////////////////////////////////////////
}
