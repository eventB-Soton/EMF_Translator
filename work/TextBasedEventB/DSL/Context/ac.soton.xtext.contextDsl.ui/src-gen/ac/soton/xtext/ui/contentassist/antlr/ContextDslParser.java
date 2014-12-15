/*
* generated by Xtext
*/
package ac.soton.xtext.ui.contentassist.antlr;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

import org.antlr.runtime.RecognitionException;
import org.eclipse.xtext.AbstractElement;
import org.eclipse.xtext.ui.editor.contentassist.antlr.AbstractContentAssistParser;
import org.eclipse.xtext.ui.editor.contentassist.antlr.FollowElement;
import org.eclipse.xtext.ui.editor.contentassist.antlr.internal.AbstractInternalContentAssistParser;

import com.google.inject.Inject;

import ac.soton.xtext.services.ContextDslGrammarAccess;

public class ContextDslParser extends AbstractContentAssistParser {
	
	@Inject
	private ContextDslGrammarAccess grammarAccess;
	
	private Map<AbstractElement, String> nameMappings;
	
	@Override
	protected ac.soton.xtext.ui.contentassist.antlr.internal.InternalContextDslParser createParser() {
		ac.soton.xtext.ui.contentassist.antlr.internal.InternalContextDslParser result = new ac.soton.xtext.ui.contentassist.antlr.internal.InternalContextDslParser(null);
		result.setGrammarAccess(grammarAccess);
		return result;
	}
	
	@Override
	protected String getRuleName(AbstractElement element) {
		if (nameMappings == null) {
			nameMappings = new HashMap<AbstractElement, String>() {
				private static final long serialVersionUID = 1L;
				{
					put(grammarAccess.getEStringAccess().getAlternatives(), "rule__EString__Alternatives");
					put(grammarAccess.getContextAccess().getGroup(), "rule__Context__Group__0");
					put(grammarAccess.getContextAccess().getGroup_3(), "rule__Context__Group_3__0");
					put(grammarAccess.getContextAccess().getGroup_4(), "rule__Context__Group_4__0");
					put(grammarAccess.getContextAccess().getGroup_5(), "rule__Context__Group_5__0");
					put(grammarAccess.getContextAccess().getGroup_6(), "rule__Context__Group_6__0");
					put(grammarAccess.getContextAccess().getGroup_7(), "rule__Context__Group_7__0");
					put(grammarAccess.getCarrierSetAccess().getGroup(), "rule__CarrierSet__Group__0");
					put(grammarAccess.getCarrierSetAccess().getGroup_2(), "rule__CarrierSet__Group_2__0");
					put(grammarAccess.getConstantAccess().getGroup(), "rule__Constant__Group__0");
					put(grammarAccess.getConstantAccess().getGroup_2(), "rule__Constant__Group_2__0");
					put(grammarAccess.getAxiomAccess().getGroup(), "rule__Axiom__Group__0");
					put(grammarAccess.getAxiomAccess().getGroup_4(), "rule__Axiom__Group_4__0");
					put(grammarAccess.getContextAccess().getNameAssignment_2(), "rule__Context__NameAssignment_2");
					put(grammarAccess.getContextAccess().getCommentAssignment_3_1(), "rule__Context__CommentAssignment_3_1");
					put(grammarAccess.getContextAccess().getExtendsAssignment_4_1(), "rule__Context__ExtendsAssignment_4_1");
					put(grammarAccess.getContextAccess().getExtendsAssignment_4_2(), "rule__Context__ExtendsAssignment_4_2");
					put(grammarAccess.getContextAccess().getSetsAssignment_5_1(), "rule__Context__SetsAssignment_5_1");
					put(grammarAccess.getContextAccess().getSetsAssignment_5_2(), "rule__Context__SetsAssignment_5_2");
					put(grammarAccess.getContextAccess().getConstantsAssignment_6_1(), "rule__Context__ConstantsAssignment_6_1");
					put(grammarAccess.getContextAccess().getConstantsAssignment_6_2(), "rule__Context__ConstantsAssignment_6_2");
					put(grammarAccess.getContextAccess().getAxiomsAssignment_7_1(), "rule__Context__AxiomsAssignment_7_1");
					put(grammarAccess.getContextAccess().getAxiomsAssignment_7_2(), "rule__Context__AxiomsAssignment_7_2");
					put(grammarAccess.getCarrierSetAccess().getNameAssignment_1(), "rule__CarrierSet__NameAssignment_1");
					put(grammarAccess.getCarrierSetAccess().getCommentAssignment_2_1(), "rule__CarrierSet__CommentAssignment_2_1");
					put(grammarAccess.getConstantAccess().getNameAssignment_1(), "rule__Constant__NameAssignment_1");
					put(grammarAccess.getConstantAccess().getCommentAssignment_2_1(), "rule__Constant__CommentAssignment_2_1");
					put(grammarAccess.getAxiomAccess().getNameAssignment_1(), "rule__Axiom__NameAssignment_1");
					put(grammarAccess.getAxiomAccess().getPredicateAssignment_2(), "rule__Axiom__PredicateAssignment_2");
					put(grammarAccess.getAxiomAccess().getTheoremAssignment_3(), "rule__Axiom__TheoremAssignment_3");
					put(grammarAccess.getAxiomAccess().getCommentAssignment_4_1(), "rule__Axiom__CommentAssignment_4_1");
				}
			};
		}
		return nameMappings.get(element);
	}
	
	@Override
	protected Collection<FollowElement> getFollowElements(AbstractInternalContentAssistParser parser) {
		try {
			ac.soton.xtext.ui.contentassist.antlr.internal.InternalContextDslParser typedParser = (ac.soton.xtext.ui.contentassist.antlr.internal.InternalContextDslParser) parser;
			typedParser.entryRuleContext();
			return typedParser.getFollowElements();
		} catch(RecognitionException ex) {
			throw new RuntimeException(ex);
		}		
	}
	
	@Override
	protected String[] getInitialHiddenTokens() {
		return new String[] { "RULE_WS", "RULE_ML_COMMENT", "RULE_SL_COMMENT" };
	}
	
	public ContextDslGrammarAccess getGrammarAccess() {
		return this.grammarAccess;
	}
	
	public void setGrammarAccess(ContextDslGrammarAccess grammarAccess) {
		this.grammarAccess = grammarAccess;
	}
}
