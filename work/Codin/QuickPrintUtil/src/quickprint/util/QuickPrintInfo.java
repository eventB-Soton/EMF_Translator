package quickprint.util;

import java.util.ArrayList;
import java.util.List;

import ac.soton.eventb.emf.components.Connector;

public class QuickPrintInfo {
	private static List<Connector> conns = new ArrayList<>();
	
	public static List<Connector> getConns(){
		return conns;
	}
}
