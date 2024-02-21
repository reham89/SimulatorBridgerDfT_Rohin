/*
 * Title:        BigDataSDNSim 1.0
 * Description:  BigDataSDNSim enables the simulating of MapReduce, big data management systems (YARN), 
 * 				 and software-defined networking (SDN) within cloud environments.
 * 
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2020, Newcastle University (UK) and Saudi Electronic University (Saudi Arabia) 
 * 
 */

package org.cloudbus.osmosis.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import org.cloudbus.cloudsim.sdn.Link;
import org.cloudbus.cloudsim.sdn.NetworkNIC;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Table;

/**
 * 
 * @author Khaled Alwasel
 * @contact kalwasel@gmail.com
 * @since BigDataSDNSim 1.0
 */

public class Topology implements Serializable {

	Hashtable<Integer,NetworkNIC> nodesTable;	
	Table<Integer, Integer, Link> links; 	
	Multimap<NetworkNIC,Link> nodeLinks;	// Node -> all Links
	Table<NetworkNIC, NetworkNIC, List<Link>> nTnlinks; // store the links from one vertex/node to vertex/node   
	List<Link> nodeLinkLists; 

    private  List<CloudDatacenter> datacentres = null;
    private  SDNController wanController  = null;

	
	public Topology() {
		nodesTable = new Hashtable<>();
		nodeLinks = HashMultimap.create();
		links = HashBasedTable.create();
		this.nTnlinks = HashBasedTable.create();
	}
	
	public Link getLink(int from, int to) {
		return links.get(from, to);
	}
	public NetworkNIC getNode(int id) {
		return nodesTable.get(id);
	}
	
	public void addNode(NetworkNIC node){
		nodesTable.put(node.getAddress(), node);
	}

	public void removeNode(NetworkNIC node) {
		nodesTable.remove(node.getAddress());
		Collection<Link> linksToRemove = nodeLinks.removeAll(node);
		var nodeAddr = node.getAddress();
		for (Link edge : linksToRemove) {
			nodeLinkLists.remove(edge);
			NetworkNIC dst = edge.dst();
			var ls = nTnlinks.get(node, dst);
			ls.remove(edge);
			if (ls.isEmpty()) nTnlinks.remove(node, dst);
			var ls2 = nodeLinks.get(dst);
			ls2.remove(edge);
			if (ls.isEmpty()) nodeLinks.removeAll(dst);
			links.remove(nodeAddr, dst.getAddress());
		}
	}

	public void addLink(int from, int to, double bw) {
		NetworkNIC fromNode = nodesTable.get(from); 
		NetworkNIC toNode = nodesTable.get(to); 
			
		if(!nodesTable.containsKey(from)||!nodesTable.containsKey(to)){
			throw new IllegalArgumentException("Unknown node on link:"+nodesTable.get(from).getAddress()+"->"+nodesTable.get(to).getAddress());
		}

		Link l = new Link(fromNode, toNode, bw);
		
		// Two way links (From -> to, To -> from)		
		links.put(from, to, l); 
		links.put(to, from, l);
		
		nodeLinks.put(fromNode, l);
		nodeLinks.put(toNode, l);

		if(nTnlinks.get(fromNode, toNode)== null){
			nodeLinkLists = new ArrayList<Link>(); 			
			nTnlinks.put(fromNode, toNode, nodeLinkLists);
		} 
		if(nTnlinks.get(toNode, fromNode) == null){
			nodeLinkLists = new ArrayList<Link>();
			nTnlinks.put(toNode, fromNode, nodeLinkLists);
		}
		List<Link> temLink_1 = nTnlinks.get(fromNode, toNode);
		if(!temLink_1.contains(l)){
			temLink_1.add(l);
			nTnlinks.put(fromNode, toNode, temLink_1);
		}
		
		List<Link> temLink_2 = nTnlinks.get(toNode, fromNode);
		if(!temLink_2.contains(l)){
			temLink_2.add(l);
			nTnlinks.put(toNode, fromNode, temLink_1);	
		}								
	}
	
	public Collection<Link> getAdjacentLinks(NetworkNIC node) {
		return nodeLinks.get(node);
	}
	
	public Collection<NetworkNIC> getAllNodes() {
		return nodesTable.values();
	}
	
	public Collection<Link> getAllLinks() {
		return nodeLinks.values();
	}

	public List<Link> getNodeToNodeLinks(NetworkNIC srcNode, NetworkNIC destNode) {
		return nTnlinks.get(srcNode, destNode);
	}
    public void setTopology(List<CloudDatacenter> datacentres, SDNController wanController) {    	
        this.datacentres = datacentres;
        this.wanController = wanController;
    }

    public List<CloudDatacenter> getDatacentres() {
        return datacentres;
    }
    public SDNController getWanController() { 
    	return wanController;  
    }

    public void removeLink(int srcAddress, int dstAddress) {
		NetworkNIC fromNode = nodesTable.get(srcAddress);
		NetworkNIC toNode = nodesTable.get(dstAddress);
		var ls = nTnlinks.remove(fromNode, toNode);
		nodeLinkLists.removeAll(ls);
		nodeLinks.get(fromNode).removeAll(ls);
		nodeLinks.get(toNode).removeAll(ls);
		links.remove(srcAddress, dstAddress);
	}
}
