package de.mpiwg.itgroup.dm2e;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.mpi.openmind.cache.WrapperService;
import org.mpi.openmind.repository.bo.Entity;
import org.mpi.openmind.repository.services.ServiceRegistry;
import org.openrdf.model.Model;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.LinkedHashModel;
import org.openrdf.model.impl.ValueFactoryImpl;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFWriter;
import org.openrdf.rio.Rio;

import de.mpiwg.itgroup.dm2e.utils.EDM;
import de.mpiwg.itgroup.ismi.utils.templates.CodexTemplate;
import de.mpiwg.itgroup.ismi.utils.templates.WitnessTemplate;


public class DM2EConverter {

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
	}
	
	
	
	public static SimpleDateFormat timeFormat = new SimpleDateFormat("yyy-mm-dd'T'hh:mm:ss");
	//private static String RIGHTS = "http://www.mpiwg-berlin.mpg.de/en/institute/licences.html";
	public  static String RIGHTS = "http://www.europeana.eu/portal/rights/rr-f.html";
	
	public static String formatRdfXml = "rdf-xml";
	public static String formatRdfJson = "rdf-json";
	public static String formatNTripes = "ntriples";
	
	public static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy.MM.dd_HH.mm");
	
	public static void convertCodexList(String file, String output, List<Long> list, WrapperService wrapper) throws Exception{
		
		Model g = new LinkedHashModel();
		ValueFactory f = ValueFactoryImpl.getInstance();
		
		int counter = 0;
		for(Long codexId : list){
			
			Entity codex = wrapper.getEntityById(codexId);
			if(codex != null && StringUtils.equals(codex.getObjectClass(), "CODEX")){
				CodexTemplate codexTempl = new CodexTemplate(codex, wrapper);
				if(StringUtils.isNotEmpty(codexTempl.mpiwgId)){
					URI providedCHO = DM2ECodexConverter.getProvidedCHO(codexTempl, wrapper, g, f);
					URI aggregation = DM2ECodexConverter.getAggregation(codexTempl, wrapper, g, f);
					g.add(aggregation, EDM.aggregatedCHO, providedCHO);
					counter++;
				}
			}
		}
		
		
		System.out.println("Transformed " + counter + " of " + list.size());
		
		write(file, output, g);
	}
	
	public void convertCodexList(String file, String output, WrapperService wrapper, Long... list) throws Exception{
	      convertCodexList(file, output, Arrays.asList(list), wrapper);  
	}
	
	public static void convertCodex(String file, String output, Long codexId, WrapperService wrapper) throws Exception{
		 
		Model g = new LinkedHashModel();
		ValueFactory f = ValueFactoryImpl.getInstance();
	
		Entity codex = wrapper.getEntityById(codexId);
		if(codex != null && StringUtils.equals(codex.getObjectClass(), "CODEX")){
			CodexTemplate codexTempl = new CodexTemplate(codex, wrapper);
			if(StringUtils.isNotEmpty(codexTempl.mpiwgId)){
				URI providedCHO = DM2ECodexConverter.getProvidedCHO(codexTempl, wrapper, g, f);
				URI aggregation = DM2ECodexConverter.getAggregation(codexTempl, wrapper, g, f);
				g.add(aggregation, EDM.aggregatedCHO, providedCHO);
				write(file, output, g);
			}
		}
		
		
	}
	
	
	
	public void convertWitnessList(String file, String output, WrapperService wrapper, Long... list) throws Exception{
		
		Model g = new LinkedHashModel();
		ValueFactory f = ValueFactoryImpl.getInstance();
		
		for(Long id : list){
			Entity witness = wrapper.getEntityById(id);
			
			if(witness != null && witness.getObjectClass().equals("WITNESS")){
				WitnessTemplate witnessTempl = new WitnessTemplate(witness, wrapper, true, true);
				if(witnessTempl.hasTitle()){
					URI providedCHO = DM2EWitnessConverter.getProvidedCHO(witnessTempl, wrapper, g, f);
					URI aggregation = DM2EWitnessConverter.getAggregation(witnessTempl, wrapper, g, f);
					g.add(aggregation, EDM.aggregatedCHO, providedCHO);	
				}
			}else{
				
			}
		}
		write(file, output, g);
	} 
	
	public void execute0(String file, String output, WrapperService wrapper) throws Exception{
		
		Model g = new LinkedHashModel();
		ValueFactory f = ValueFactoryImpl.getInstance();
		
		for(Entity witness : wrapper.getEntitiesByDef("WITNESS")){
			WitnessTemplate witnessTempl = new WitnessTemplate(witness, wrapper, true, true);
			if(witnessTempl.hasTitle()){
				URI providedCHO = DM2EWitnessConverter.getProvidedCHO(witnessTempl, wrapper, g, f);
				URI aggregation = DM2EWitnessConverter.getAggregation(witnessTempl, wrapper, g, f);
				g.add(aggregation, EDM.aggregatedCHO, providedCHO);	
			}
		}
		write(file, output, g);
	} 
	
	private static void write(String file, String output, Model g) throws RDFHandlerException, IOException{
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		FileOutputStream fos = null;
		RDFWriter writer = null;
		
		if(formatRdfXml.equals(output)){
			 writer = Rio.createWriter(RDFFormat.RDFXML, out);			
		}else if(formatRdfJson.equals(output)){
			 writer = Rio.createWriter(RDFFormat.RDFJSON, out);
		}else if(formatNTripes.equals(output)){
			writer = Rio.createWriter(RDFFormat.NTRIPLES , out);
		}
		
		writer.startRDF();
		for (Statement st : g) {
			writer.handleStatement(st);
		}
		writer.endRDF();
		
	    try {
	    	fos = new FileOutputStream (new File(file));
	    	out.writeTo(fos);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			fos.close();	
		}
	}
	
	/**
	 * Execute with maven:
	 * mvn exec:java -Dexec.mainClass="de.mpiwg.itgroup.dm2e.DM2EConverter" -Dexec.args="/Users/jurzua/ismi_dm2e ntriples"
	 * @param args
	 */
	public static void main(String[] args){
		
		System.out.println("***************************");
		System.out.println("### DM2EConverter ###");
		
		try {
			String output = (args.length > 1) ? args[1] : formatNTripes;
			
			
			String folder = DATE_FORMAT.format(new Date());
			folder = args[0] + "/" + folder;
			File file = new File(folder);
			file.mkdirs();
			
			WrapperService wrapper = (new ServiceRegistry()).getWrapper();
			
			for(Long codexId : codexIdList){
				convertCodex(folder + "/" + codexId + ".nt", output, codexId, wrapper);
				System.out.print("*");
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		/*
		Long[] codicesList = {(long) 35093};
		
		try {
			
			
			converter.convertCodexList(args[0], output, codexIdList);
			
			//converter.convertWitnessList(args[0], output, list);
			//converter.execute(args[0], output);
		} catch (Exception e) {
			e.printStackTrace();
			e.printStackTrace();
		}
		*/
		System.out.println("### Finished ###");
		System.out.println("***************************");
		System.exit(0);
	}
	
}
