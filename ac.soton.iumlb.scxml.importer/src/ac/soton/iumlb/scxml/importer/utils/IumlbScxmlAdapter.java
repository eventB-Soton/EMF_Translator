/**
 * 
 */
package ac.soton.iumlb.scxml.importer.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <p>
 * 
 * </p>
 * 
 * @author cfs
 * @version
 * @see
 * @since
 */
public class IumlbScxmlAdapter {
	
	  /**
	   * The adapted SCXML element.
	   */
	  protected EObject target = null;

	
	 /**
	   * Creates an instance.
	   */
	  public IumlbScxmlAdapter(EObject target)
	  {
	    this.target = target;
	  }

	 /**
	  * returns the value of the attribute with the given name contained in
	  * the anyAttribute feature map...
	  * ... or null if no such attribute is found.
	  * @param attributeName
	  * @return attribute value object
	  */
	  
	  public Object getAnyAttributeValue(String attributeName){
			EStructuralFeature anyAttributeFeature  = target.eClass().getEStructuralFeature("anyAttribute");
			FeatureMap fm = (FeatureMap) target.eGet(anyAttributeFeature);
			for (int i=0; i< fm.size(); i++){
				EStructuralFeature sf = fm.getEStructuralFeature(i);
				if (attributeName.equals(sf.getName())){
					return fm.getValue(i);
				}
			}
			return null;	  
	  }
	  
	 /**
	  * returns a list of adapters wrapping EObjects that are the value of the features with the given name contained in
	  * the any feature map...
	  * ... or an empty list if none are found.
	  * @param attributeName
	  * @return attribute value object
	  */
	  
	  public List<IumlbScxmlAdapter> getAnyChildren(String featureName){
			List<IumlbScxmlAdapter> ret = new ArrayList<IumlbScxmlAdapter>();
			EStructuralFeature anyFeature  = target.eClass().getEStructuralFeature("any");
			if (anyFeature==null) return ret;
			FeatureMap fm = (FeatureMap) target.eGet(anyFeature);
			for (int i=0; i< fm.size(); i++){
				EStructuralFeature sf = fm.getEStructuralFeature(i);
				if (featureName.equals(sf.getName()) && fm.getValue(i) instanceof EObject){
					ret.add(new IumlbScxmlAdapter((EObject)fm.getValue(i)));
				}
			}
			return ret;	  
	  }
	  
	  
	/**
	 * Returns the starting refinement level for this SCXML element
	 * This is given in a 'refinement' iumlb:attribute attached to the element,
	 * or, if none, the refinement level of its parent,
	 * or, if none, 0
	 * 
	 * @param scxmlElement
	 * @return
	 */
	public int getRefinementLevel(){
		Object level = getAnyAttributeValue("refinement");
		if (level instanceof String) {
			return Integer.parseInt((String)level);
		}else{
			if (target.eContainer()==null){
				return 0;
			}else{
				return new IumlbScxmlAdapter(target.eContainer()).getRefinementLevel();
			}
		}
	}

	
	public List<IumlbScxmlAdapter> getinvariants() {		
		return getAnyChildren("invariant");
	}

	/**
	 * @return
	 */
	public List<IumlbScxmlAdapter> getGuards() {
		return getAnyChildren("guard");	
	}
		
}
