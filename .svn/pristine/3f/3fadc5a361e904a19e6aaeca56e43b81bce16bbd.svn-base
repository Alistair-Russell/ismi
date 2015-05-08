package de.mpiwg.itgroup.ismi.publicView;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.mpi.openmind.repository.bo.Attribute;
import org.mpi.openmind.repository.bo.Entity;

import de.mpiwg.itgroup.ismi.entry.beans.AbstractBean;
import de.mpiwg.itgroup.ismi.entry.beans.ApplicationBean;

public class PublicCodexList extends AbstractBean{
	private static final long serialVersionUID = 4576052953031233629L;

	private static Logger logger = Logger.getLogger(PublicCodexList.class);
	/*
	private static List<Long> codexIdList = new ArrayList<Long>();
	static{
		codexIdList.add(new Long(27543));
		codexIdList.add(new Long(36745));
		codexIdList.add(new Long(58453));
		codexIdList.add(new Long(87298));
		codexIdList.add(new Long(259646));
		codexIdList.add(new Long(35093));
		codexIdList.add(new Long(22863));
		codexIdList.add(new Long(34870));
		codexIdList.add(new Long(36882));
		codexIdList.add(new Long(101488));
		codexIdList.add(new Long(36696));
		codexIdList.add(new Long(31794));
		codexIdList.add(new Long(37240));
		codexIdList.add(new Long(35014));
		codexIdList.add(new Long(35583));
		codexIdList.add(new Long(37025));
		codexIdList.add(new Long(35960));
		codexIdList.add(new Long(172492));
		codexIdList.add(new Long(98286));
		codexIdList.add(new Long(165721));
		codexIdList.add(new Long(260111));
		codexIdList.add(new Long(90980));
		codexIdList.add(new Long(36316));
		codexIdList.add(new Long(260120));
		codexIdList.add(new Long(36241));
		codexIdList.add(new Long(260129));
		codexIdList.add(new Long(260138));
		codexIdList.add(new Long(38860));
		codexIdList.add(new Long(176694));
		codexIdList.add(new Long(72545));
		codexIdList.add(new Long(36185));
		codexIdList.add(new Long(36575));
		codexIdList.add(new Long(260146));
		codexIdList.add(new Long(31672));
		codexIdList.add(new Long(37739));
		codexIdList.add(new Long(89861));
		codexIdList.add(new Long(176778));
		codexIdList.add(new Long(180743));
		codexIdList.add(new Long(86328));
		codexIdList.add(new Long(260150));
		codexIdList.add(new Long(90658));
		codexIdList.add(new Long(58423));
		codexIdList.add(new Long(181058));
		codexIdList.add(new Long(105948));
		codexIdList.add(new Long(35526));
		codexIdList.add(new Long(74078));
		codexIdList.add(new Long(260158));
		codexIdList.add(new Long(181096));
		codexIdList.add(new Long(31606));
		codexIdList.add(new Long(31568));
		codexIdList.add(new Long(27872));
		codexIdList.add(new Long(36938));
		codexIdList.add(new Long(4836));
		codexIdList.add(new Long(34668));
		codexIdList.add(new Long(76866));
		codexIdList.add(new Long(102230));
		codexIdList.add(new Long(76888));
		codexIdList.add(new Long(74070));
		codexIdList.add(new Long(73757));
		codexIdList.add(new Long(182685));
		codexIdList.add(new Long(260162));
		codexIdList.add(new Long(260170));
		codexIdList.add(new Long(1102));
		codexIdList.add(new Long(172888));
		codexIdList.add(new Long(260174));
		codexIdList.add(new Long(34806));
		codexIdList.add(new Long(28088));
		codexIdList.add(new Long(36713));
		codexIdList.add(new Long(37323));
		codexIdList.add(new Long(34551));
		codexIdList.add(new Long(35943));
		codexIdList.add(new Long(98095));
		codexIdList.add(new Long(260178));
		codexIdList.add(new Long(260182));
		codexIdList.add(new Long(182770));
		codexIdList.add(new Long(260186));
		codexIdList.add(new Long(260190));
		codexIdList.add(new Long(260194));
		codexIdList.add(new Long(36114));
		codexIdList.add(new Long(85003));
		codexIdList.add(new Long(31630));
		codexIdList.add(new Long(157290));
		codexIdList.add(new Long(37153));
		codexIdList.add(new Long(37213));
		codexIdList.add(new Long(172952));
		codexIdList.add(new Long(86871));
		codexIdList.add(new Long(64406));
		codexIdList.add(new Long(102590));
		codexIdList.add(new Long(82615));
		codexIdList.add(new Long(58245));
		codexIdList.add(new Long(179791));
		codexIdList.add(new Long(179550));
		codexIdList.add(new Long(12419));
		codexIdList.add(new Long(95861));
		codexIdList.add(new Long(36429));
		codexIdList.add(new Long(36099));
		codexIdList.add(new Long(74237));
		codexIdList.add(new Long(36065));
		codexIdList.add(new Long(74822));
		codexIdList.add(new Long(87549));
		codexIdList.add(new Long(83765));
		codexIdList.add(new Long(36733));
		codexIdList.add(new Long(19259));
		codexIdList.add(new Long(260198));
		codexIdList.add(new Long(34986));
		codexIdList.add(new Long(88041));
		codexIdList.add(new Long(260202));
		codexIdList.add(new Long(36550));
		codexIdList.add(new Long(260206));
		codexIdList.add(new Long(37228));
		codexIdList.add(new Long(39880));
		codexIdList.add(new Long(36318));
		codexIdList.add(new Long(36597));
		codexIdList.add(new Long(35035));
		codexIdList.add(new Long(58328));
		codexIdList.add(new Long(80831));
		codexIdList.add(new Long(58354));
		codexIdList.add(new Long(74277));
		codexIdList.add(new Long(36529));
		codexIdList.add(new Long(36380));
		codexIdList.add(new Long(69450));
		codexIdList.add(new Long(200246));
		codexIdList.add(new Long(260222));
		codexIdList.add(new Long(81178));
		codexIdList.add(new Long(260226));
		codexIdList.add(new Long(199952));
		codexIdList.add(new Long(262557));
		codexIdList.add(new Long(87212));
		codexIdList.add(new Long(99059));
		codexIdList.add(new Long(64270));
		codexIdList.add(new Long(81811));
		codexIdList.add(new Long(65785));
		codexIdList.add(new Long(36645));
	}*/
	
	private List<Entity> codexList;
	
	/*
	public void load(){
		logger.info("************* (1) Loading Public Codex List...\n\n\n");
		this.codexList = new ArrayList<Entity>();
		
		long start = System.currentTimeMillis();
		
		for(Long id : codexIdList){
			Entity codex = getWrapper().getEntityById(id);
			if (codex != null){
				this.codexList.add(codex);
			}
		}
		long diff = System.currentTimeMillis() - start;
		logger.info("Dif: " + diff + ", elemenst: " + this.codexList.size());
	}*/

	public void load0(){
		this.codexList = new ArrayList<Entity>();
		
		long start = System.currentTimeMillis();
		List<Attribute> attList = getWrapper().getAttributesByDefByAttName("CODEX", "public", "true", -1);
		
		for(Attribute att : attList){
			Entity codex = getWrapper().getEntityById(att.getSourceId());
			if (codex != null){
				this.codexList.add(codex);
			}
		}
		
		long diff = System.currentTimeMillis() - start;
		logger.info("Loading Public Codex List - Time[ms]: " + diff + ", elemenst: " + this.codexList.size());
	}
	
	public List<Entity> getCodexList() {
		if(codexList == null){
			this.load0();
		}
		return codexList;
	}

	public void setCodexList(List<Entity> codexList) {
		this.codexList = codexList;
	}
	
	
}
