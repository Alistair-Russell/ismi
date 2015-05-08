package de.mpiwg.itgroup.echo.jsp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ECHOViewer {
	
	
	private static String SCALER_WS = "http://digilib.mpiwg-berlin.mpg.de/digitallibrary/servlet/Scaler?fn=permanent/library/WWKYPR05/pageimg"; //&dw=1639&dh=376
	
	private static String PAGES_WS = "http://digilib.mpiwg-berlin.mpg.de/digitallibrary/dirInfo-xml.jsp?fn=/permanent/library/UR271U6Y/pageimg";
	
	private String currentId = "BVED1RUM";
	private int pageSize;
	private int currentPage;
	
	private static List<String> scanList;
	
	static{
		String[] scansList0 = {
				"BVED1RUM",
				"M9XBU92T",
				"RNEZE8Z6",
				"2BPAV5AP",
				"7T85HQNS",
				"TRQNNGSR",
				"S1C20QWU",
				"8XWYTZ26",
				"6UZB2ZF0",
				"M0XAYXH0",
				"2EBGM67W",
				"NXDAZZYU",
				"MWG2QDZ9",
				"B47T3HY2",
				"FM05UE82",
				"UGTHY0RG",
				"4UZFR41E",
				"2AG56K0B",
				"1T0Z5TU5",
				"QQVPRVXX",
				"RUK8AFQP",
				"ERZHST5Y",
				"5MB6HBYV",
				"5AN0VGK8",
				"0SWBP6BQ",
				"CC2KDXXX",
				"YB4U050C",
				"WTC2BK80",
				"KNA1AZYB",
				"1M75N53E",
				"WRQEXG1A",
				"409E3DCG",
				"XQH10RER",
				"DX904FN7",
				"GEWZ52P2",
				"U6BC9X7F",
				"VREYNH8W",
				"QW00YZFR",
				"4ZPFQTN0",
				"6631A2R6"
		};
		scanList = Arrays.asList(scansList0);
	}
	

	

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
}


