package de.mpiwg.itgroup.ismi.entry.dataBeans;

import org.mpi.openmind.cache.WrapperService;
import org.mpi.openmind.repository.services.ServiceRegistry;

public class AbstractCache {

	private ServiceRegistry sr;
	
	public AbstractCache(ServiceRegistry sr){
		this.sr = sr;
		this.reset();
	}
	
	public void reset(){}

	protected WrapperService getWrapper(){
		return sr.getWrapper();
	}
}
