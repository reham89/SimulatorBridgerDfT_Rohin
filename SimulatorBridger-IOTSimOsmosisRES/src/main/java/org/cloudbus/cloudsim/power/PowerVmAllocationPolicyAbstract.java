/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009-2012, The University of Melbourne, Australia
 */

package org.cloudbus.cloudsim.power;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import uk.ncl.giacomobergami.components.allocation_policy.VmAllocationPolicy;
import org.cloudbus.cloudsim.core.MainEventManager;

/**
 * The class of an abstract power-aware VM allocation policy.
 * 
 * If you are using any algorithms, policies or workload included in the power package, please cite
 * the following paper:
 * 
 * Anton Beloglazov, and Rajkumar Buyya, "Optimal Online Deterministic Algorithms and Adaptive
 * Heuristics for Energy and Performance Efficient Dynamic Consolidation of Virtual Machines in
 * Cloud Data Centers", Concurrency and Computation: Practice and Experience (CCPE), Volume 24,
 * Issue 13, Pages: 1397-1420, John Wiley & Sons, Ltd, New York, USA, 2012
 * 
 * @author Anton Beloglazov
 * @since CloudSim Toolkit 3.0
 */
public abstract class PowerVmAllocationPolicyAbstract extends VmAllocationPolicy {

	/** The vm table. */
	private final Map<String, Host> vmTable = new HashMap<String, Host>();

	/**
	 * Instantiates a new power vm allocation policy abstract.
	 * 
	 * @param list the list
	 */
	public PowerVmAllocationPolicyAbstract(List<? extends Host> list) {
		super(list);
	}

	/*
	 * (non-Javadoc)
	 * @see uk.ncl.giacomobergami.components.allocation_policy.VmAllocationPolicy#allocateHostForVm(org.cloudbus.cloudsim.Vm)
	 */
	@Override
	public boolean allocateHostForVm(Vm vm) {
		return allocateHostForVm(vm, findHostForVm(vm));
	}

	/*
	 * (non-Javadoc)
	 * @see uk.ncl.giacomobergami.components.allocation_policy.VmAllocationPolicy#allocateHostForVm(org.cloudbus.cloudsim.Vm,
	 * org.cloudbus.cloudsim.Host)
	 */
	@Override
	public boolean allocateHostForVm(Vm vm, Host host) {
		if (host == null) {
			logger.error("%.2f: No suitable host found for VM #" + vm.getId() + "\n"+ MainEventManager.clock());
			return false;
		}
		if (host.vmCreate(vm)) { // if vm has been succesfully created in the host
			getVmTable().put(vm.getUid(), host);
			logger.error(
					"%.2f: VM #" + vm.getId() + " has been allocated to the host #" + host.getId()+
					MainEventManager.clock());
			return true;
		}
		logger.error(
				"%.2f: Creation of VM #" + vm.getId() + " on the host #" + host.getId() + " failed\n"+
				MainEventManager.clock());
		return false;
	}

	/**
	 * Find host for vm.
	 * 
	 * @param vm the vm
	 * @return the power host
	 */
	public PowerHost findHostForVm(Vm vm) {
		for (PowerHost host : this.<PowerHost> getHostList()) {
			if (host.isSuitableForVm(vm)) {
				return host;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see uk.ncl.giacomobergami.components.allocation_policy.VmAllocationPolicy#deallocateHostForVm(org.cloudbus.cloudsim.Vm)
	 */
	@Override
	public void deallocateHostForVm(Vm vm) {
		Host host = getVmTable().remove(vm.getUid());
		if (host != null) {
			host.vmDestroy(vm);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see uk.ncl.giacomobergami.components.allocation_policy.VmAllocationPolicy#getHost(org.cloudbus.cloudsim.Vm)
	 */
	@Override
	public Host getHost(Vm vm) {
		return getVmTable().get(vm.getUid());
	}

	/*
	 * (non-Javadoc)
	 * @see uk.ncl.giacomobergami.components.allocation_policy.VmAllocationPolicy#getHost(int, int)
	 */
	@Override
	public Host getHost(int vmId, int userId) {
		return getVmTable().get(Vm.getUid(userId, vmId));
	}

	/**
	 * Gets the vm table.
	 * 
	 * @return the vm table
	 */
	public Map<String, Host> getVmTable() {
		return vmTable;
	}

}
