package de.mpiwg.itgroup.ismi.publicView.pages;

import java.util.List;

import org.mpi.openmind.repository.bo.Attribute;
import org.mpi.openmind.repository.bo.Entity;
import org.mpi.openmind.repository.utils.RomanizationLoC;

public class WitnessDynamicPage extends JSPDynamicPage{
	
	private Long titleId;
	private Long authorId;
	private String author;
	private String title;
	private String firstPage;
	
	@Override
	public void load(Long witnessId){
		super.load(witnessId);
		
		this.titleId = null;
		this.title = null;
		this.author = null;
		this.authorId = null;
		
		
		if(witnessId != null){
			
			this.loadFirstPage();
			
			//loading the digitalization object
			List<Entity> list = getWrapper().getTargetsForSourceRelation(witnessId, "is_part_of", "CODEX", 1);
			if (list.size() > 0) {
				Entity codex = list.get(0);
				List<Entity> list0 = getWrapper().getSourcesForTargetRelation(codex.getId(), "is_digitalization_of", "DIGITALIZATION", 1);
				this.digi = (list0.size() == 0) ? null : list0.get(0);
			}
			
			
			//loading witness
			List<Entity> list0 = getWrapper().getTargetsForSourceRelation(witnessId, "is_exemplar_of", "TEXT", 1);
			if (list0.size() > 0) {
				
				this.titleId = list0.get(0).getId();
				this.title = RomanizationLoC.convert(list0.get(0).getOwnValue());
				System.out.println("&&&&&& " + this.title);
				
				list0 = getWrapper().getTargetsForSourceRelation(this.titleId, "was_created_by", "PERSON", 1);
				if(list0.size() > 0){
					this.authorId = list0.get(0).getId();
					this.author = RomanizationLoC.convert(list0.get(0).getOwnValue());
				}
			}	
		}
	}	
	
	/**
	 * The start_page saves the first page of the witness in the codex.
	 * start_page can contains number from the 1, however the diva viewer considers that the first page is 0.
	 * For this reason, if a witness contains this attribute, then we must subtract 1 page.
	 */
	public void loadFirstPage(){
		Attribute firstPageAtt = getWrapper().getAttributeByName(getCurrentEntId(), "start_page");
		Integer tmp = 0;
		
		if(firstPageAtt != null){
			try {
				tmp = Integer.parseInt(firstPageAtt.getOwnValue());
				tmp = (tmp > 0) ? tmp-1 : tmp;
			} catch (Exception e) {}
		}
		
		this.firstPage = tmp.toString();
	}
	
	public void init(){
		try {
			
			this.load(Long.parseLong(getRequest().getParameter("eid")));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Long getTitleId() {
		return titleId;
	}

	public void setTitleId(Long titleId) {
		this.titleId = titleId;
	}

	public Long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getFirstPage(){
		return this.firstPage;
	}
}
