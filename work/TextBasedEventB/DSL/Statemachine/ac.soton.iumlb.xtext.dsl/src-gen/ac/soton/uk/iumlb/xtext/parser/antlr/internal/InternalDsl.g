/*
* generated by Xtext
*/
grammar InternalDsl;

options {
	superClass=AbstractInternalAntlrParser;
	
}

@lexer::header {
package ac.soton.uk.iumlb.xtext.parser.antlr.internal;

// Hack: Use our own Lexer superclass by means of import. 
// Currently there is no other way to specify the superclass for the lexer.
import org.eclipse.xtext.parser.antlr.Lexer;
}

@parser::header {
package ac.soton.uk.iumlb.xtext.parser.antlr.internal; 

import org.eclipse.xtext.*;
import org.eclipse.xtext.parser.*;
import org.eclipse.xtext.parser.impl.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.xtext.parser.antlr.AbstractInternalAntlrParser;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.eclipse.xtext.parser.antlr.XtextTokenStream.HiddenTokens;
import org.eclipse.xtext.parser.antlr.AntlrDatatypeRuleToken;
import ac.soton.uk.iumlb.xtext.services.DslGrammarAccess;

}

@parser::members {

 	private DslGrammarAccess grammarAccess;
 	
    public InternalDslParser(TokenStream input, DslGrammarAccess grammarAccess) {
        this(input);
        this.grammarAccess = grammarAccess;
        registerRules(grammarAccess.getGrammar());
    }
    
    @Override
    protected String getFirstRuleName() {
    	return "Statemachine";	
   	}
   	
   	@Override
   	protected DslGrammarAccess getGrammarAccess() {
   		return grammarAccess;
   	}
}

@rulecatch { 
    catch (RecognitionException re) { 
        recover(input,re); 
        appendSkippedTokens();
    } 
}




// Entry rule entryRuleStatemachine
entryRuleStatemachine returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getStatemachineRule()); }
	 iv_ruleStatemachine=ruleStatemachine 
	 { $current=$iv_ruleStatemachine.current; } 
	 EOF 
;

// Rule Statemachine
ruleStatemachine returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(	otherlv_0='Statemachine' 
    {
    	newLeafNode(otherlv_0, grammarAccess.getStatemachineAccess().getStatemachineKeyword_0());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getStatemachineAccess().getNameEStringParserRuleCall_1_0()); 
	    }
		lv_name_1_0=ruleEString		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getStatemachineRule());
	        }
       		set(
       			$current, 
       			"name",
        		lv_name_1_0, 
        		"EString");
	        afterParserOrEnumRuleCall();
	    }

)
)(	otherlv_2='>' 
    {
    	newLeafNode(otherlv_2, grammarAccess.getStatemachineAccess().getGreaterThanSignKeyword_2_0());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getStatemachineAccess().getCommentEStringParserRuleCall_2_1_0()); 
	    }
		lv_comment_3_0=ruleEString		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getStatemachineRule());
	        }
       		set(
       			$current, 
       			"comment",
        		lv_comment_3_0, 
        		"EString");
	        afterParserOrEnumRuleCall();
	    }

)
))?(	otherlv_4='translation' 
    {
    	newLeafNode(otherlv_4, grammarAccess.getStatemachineAccess().getTranslationKeyword_3_0());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getStatemachineAccess().getTranslationTranslationKindEnumRuleCall_3_1_0()); 
	    }
		lv_translation_5_0=ruleTranslationKind		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getStatemachineRule());
	        }
       		set(
       			$current, 
       			"translation",
        		lv_translation_5_0, 
        		"TranslationKind");
	        afterParserOrEnumRuleCall();
	    }

)
))?(	otherlv_6='elaborates' 
    {
    	newLeafNode(otherlv_6, grammarAccess.getStatemachineAccess().getElaboratesKeyword_4_0());
    }
(
(
		{
			if ($current==null) {
	            $current = createModelElement(grammarAccess.getStatemachineRule());
	        }
        }
		{ 
	        newCompositeNode(grammarAccess.getStatemachineAccess().getElaboratesEventBNamedCrossReference_4_1_0()); 
	    }
		ruleEString		{ 
	        afterParserOrEnumRuleCall();
	    }

)
))?(	otherlv_8='refines' 
    {
    	newLeafNode(otherlv_8, grammarAccess.getStatemachineAccess().getRefinesKeyword_5_0());
    }
(
(
		{
			if ($current==null) {
	            $current = createModelElement(grammarAccess.getStatemachineRule());
	        }
        }
		{ 
	        newCompositeNode(grammarAccess.getStatemachineAccess().getRefinesStatemachineCrossReference_5_1_0()); 
	    }
		ruleEString		{ 
	        afterParserOrEnumRuleCall();
	    }

)
))?(	otherlv_10='nodes' 
    {
    	newLeafNode(otherlv_10, grammarAccess.getStatemachineAccess().getNodesKeyword_6_0());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getStatemachineAccess().getNodesAbstractNodeParserRuleCall_6_1_0()); 
	    }
		lv_nodes_11_0=ruleAbstractNode		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getStatemachineRule());
	        }
       		add(
       			$current, 
       			"nodes",
        		lv_nodes_11_0, 
        		"AbstractNode");
	        afterParserOrEnumRuleCall();
	    }

)
)(	otherlv_12=',' 
    {
    	newLeafNode(otherlv_12, grammarAccess.getStatemachineAccess().getCommaKeyword_6_2_0());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getStatemachineAccess().getNodesAbstractNodeParserRuleCall_6_2_1_0()); 
	    }
		lv_nodes_13_0=ruleAbstractNode		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getStatemachineRule());
	        }
       		add(
       			$current, 
       			"nodes",
        		lv_nodes_13_0, 
        		"AbstractNode");
	        afterParserOrEnumRuleCall();
	    }

)
))*)?(	otherlv_14='transitions' 
    {
    	newLeafNode(otherlv_14, grammarAccess.getStatemachineAccess().getTransitionsKeyword_7_0());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getStatemachineAccess().getTransitionsTransitionParserRuleCall_7_1_0()); 
	    }
		lv_transitions_15_0=ruleTransition		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getStatemachineRule());
	        }
       		add(
       			$current, 
       			"transitions",
        		lv_transitions_15_0, 
        		"Transition");
	        afterParserOrEnumRuleCall();
	    }

)
)(
(
		{ 
	        newCompositeNode(grammarAccess.getStatemachineAccess().getTransitionsTransitionParserRuleCall_7_2_0()); 
	    }
		lv_transitions_16_0=ruleTransition		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getStatemachineRule());
	        }
       		add(
       			$current, 
       			"transitions",
        		lv_transitions_16_0, 
        		"Transition");
	        afterParserOrEnumRuleCall();
	    }

)
)*)?	otherlv_17='end' 
    {
    	newLeafNode(otherlv_17, grammarAccess.getStatemachineAccess().getEndKeyword_8());
    }
)
;





// Entry rule entryRuleAbstractNode
entryRuleAbstractNode returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getAbstractNodeRule()); }
	 iv_ruleAbstractNode=ruleAbstractNode 
	 { $current=$iv_ruleAbstractNode.current; } 
	 EOF 
;

// Rule AbstractNode
ruleAbstractNode returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(
    { 
        newCompositeNode(grammarAccess.getAbstractNodeAccess().getStateParserRuleCall_0()); 
    }
    this_State_0=ruleState
    { 
        $current = $this_State_0.current; 
        afterParserOrEnumRuleCall();
    }

    |
    { 
        newCompositeNode(grammarAccess.getAbstractNodeAccess().getInitialParserRuleCall_1()); 
    }
    this_Initial_1=ruleInitial
    { 
        $current = $this_Initial_1.current; 
        afterParserOrEnumRuleCall();
    }

    |
    { 
        newCompositeNode(grammarAccess.getAbstractNodeAccess().getFinalParserRuleCall_2()); 
    }
    this_Final_2=ruleFinal
    { 
        $current = $this_Final_2.current; 
        afterParserOrEnumRuleCall();
    }

    |
    { 
        newCompositeNode(grammarAccess.getAbstractNodeAccess().getAnyParserRuleCall_3()); 
    }
    this_Any_3=ruleAny
    { 
        $current = $this_Any_3.current; 
        afterParserOrEnumRuleCall();
    }

    |
    { 
        newCompositeNode(grammarAccess.getAbstractNodeAccess().getJunctionParserRuleCall_4()); 
    }
    this_Junction_4=ruleJunction
    { 
        $current = $this_Junction_4.current; 
        afterParserOrEnumRuleCall();
    }

    |
    { 
        newCompositeNode(grammarAccess.getAbstractNodeAccess().getForkParserRuleCall_5()); 
    }
    this_Fork_5=ruleFork
    { 
        $current = $this_Fork_5.current; 
        afterParserOrEnumRuleCall();
    }
)
;







// Entry rule entryRuleEString
entryRuleEString returns [String current=null] 
	:
	{ newCompositeNode(grammarAccess.getEStringRule()); } 
	 iv_ruleEString=ruleEString 
	 { $current=$iv_ruleEString.current.getText(); }  
	 EOF 
;

// Rule EString
ruleEString returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(    this_STRING_0=RULE_STRING    {
		$current.merge(this_STRING_0);
    }

    { 
    newLeafNode(this_STRING_0, grammarAccess.getEStringAccess().getSTRINGTerminalRuleCall_0()); 
    }

    |    this_ID_1=RULE_ID    {
		$current.merge(this_ID_1);
    }

    { 
    newLeafNode(this_ID_1, grammarAccess.getEStringAccess().getIDTerminalRuleCall_1()); 
    }
)
    ;





// Entry rule entryRuleTransition
entryRuleTransition returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getTransitionRule()); }
	 iv_ruleTransition=ruleTransition 
	 { $current=$iv_ruleTransition.current; } 
	 EOF 
;

// Rule Transition
ruleTransition returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
    {
        $current = forceCreateModelElement(
            grammarAccess.getTransitionAccess().getTransitionAction_0(),
            $current);
    }
)(
(
		lv_extended_1_0=	'extended' 
    {
        newLeafNode(lv_extended_1_0, grammarAccess.getTransitionAccess().getExtendedExtendedKeyword_1_0());
    }
 
	    {
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getTransitionRule());
	        }
       		setWithLastConsumed($current, "extended", true, "extended");
	    }

)
)?	otherlv_2='Transition' 
    {
    	newLeafNode(otherlv_2, grammarAccess.getTransitionAccess().getTransitionKeyword_2());
    }
(	otherlv_3='>' 
    {
    	newLeafNode(otherlv_3, grammarAccess.getTransitionAccess().getGreaterThanSignKeyword_3_0());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getTransitionAccess().getCommentEStringParserRuleCall_3_1_0()); 
	    }
		lv_comment_4_0=ruleEString		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getTransitionRule());
	        }
       		set(
       			$current, 
       			"comment",
        		lv_comment_4_0, 
        		"EString");
	        afterParserOrEnumRuleCall();
	    }

)
))?(	otherlv_5='elaborates' 
    {
    	newLeafNode(otherlv_5, grammarAccess.getTransitionAccess().getElaboratesKeyword_4_0());
    }
(
(
		{
			if ($current==null) {
	            $current = createModelElement(grammarAccess.getTransitionRule());
	        }
        }
		{ 
	        newCompositeNode(grammarAccess.getTransitionAccess().getElaboratesEventCrossReference_4_1_0()); 
	    }
		ruleEString		{ 
	        afterParserOrEnumRuleCall();
	    }

)
)(
(
		{
			if ($current==null) {
	            $current = createModelElement(grammarAccess.getTransitionRule());
	        }
        }
		{ 
	        newCompositeNode(grammarAccess.getTransitionAccess().getElaboratesEventCrossReference_4_2_0()); 
	    }
		ruleEString		{ 
	        afterParserOrEnumRuleCall();
	    }

)
)*)?(	otherlv_8='target' 
    {
    	newLeafNode(otherlv_8, grammarAccess.getTransitionAccess().getTargetKeyword_5_0());
    }
(
(
		{
			if ($current==null) {
	            $current = createModelElement(grammarAccess.getTransitionRule());
	        }
        }
		{ 
	        newCompositeNode(grammarAccess.getTransitionAccess().getTargetAbstractNodeCrossReference_5_1_0()); 
	    }
		ruleEString		{ 
	        afterParserOrEnumRuleCall();
	    }

)
))?(	otherlv_10='source' 
    {
    	newLeafNode(otherlv_10, grammarAccess.getTransitionAccess().getSourceKeyword_6_0());
    }
(
(
		{
			if ($current==null) {
	            $current = createModelElement(grammarAccess.getTransitionRule());
	        }
        }
		{ 
	        newCompositeNode(grammarAccess.getTransitionAccess().getSourceAbstractNodeCrossReference_6_1_0()); 
	    }
		ruleEString		{ 
	        afterParserOrEnumRuleCall();
	    }

)
))?(	otherlv_12='when' 
    {
    	newLeafNode(otherlv_12, grammarAccess.getTransitionAccess().getWhenKeyword_7_0());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getTransitionAccess().getGuardsGuardParserRuleCall_7_1_0()); 
	    }
		lv_guards_13_0=ruleGuard		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getTransitionRule());
	        }
       		add(
       			$current, 
       			"guards",
        		lv_guards_13_0, 
        		"Guard");
	        afterParserOrEnumRuleCall();
	    }

)
)(
(
		{ 
	        newCompositeNode(grammarAccess.getTransitionAccess().getGuardsGuardParserRuleCall_7_2_0()); 
	    }
		lv_guards_14_0=ruleGuard		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getTransitionRule());
	        }
       		add(
       			$current, 
       			"guards",
        		lv_guards_14_0, 
        		"Guard");
	        afterParserOrEnumRuleCall();
	    }

)
)*)?(	otherlv_15='any' 
    {
    	newLeafNode(otherlv_15, grammarAccess.getTransitionAccess().getAnyKeyword_8_0());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getTransitionAccess().getParametersTypedParameterParserRuleCall_8_1_0()); 
	    }
		lv_parameters_16_0=ruleTypedParameter		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getTransitionRule());
	        }
       		add(
       			$current, 
       			"parameters",
        		lv_parameters_16_0, 
        		"TypedParameter");
	        afterParserOrEnumRuleCall();
	    }

)
)(
(
		{ 
	        newCompositeNode(grammarAccess.getTransitionAccess().getParametersTypedParameterParserRuleCall_8_2_0()); 
	    }
		lv_parameters_17_0=ruleTypedParameter		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getTransitionRule());
	        }
       		add(
       			$current, 
       			"parameters",
        		lv_parameters_17_0, 
        		"TypedParameter");
	        afterParserOrEnumRuleCall();
	    }

)
)*	otherlv_18='where' 
    {
    	newLeafNode(otherlv_18, grammarAccess.getTransitionAccess().getWhereKeyword_8_3());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getTransitionAccess().getGuardsGuardParserRuleCall_8_4_0()); 
	    }
		lv_guards_19_0=ruleGuard		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getTransitionRule());
	        }
       		add(
       			$current, 
       			"guards",
        		lv_guards_19_0, 
        		"Guard");
	        afterParserOrEnumRuleCall();
	    }

)
)(
(
		{ 
	        newCompositeNode(grammarAccess.getTransitionAccess().getGuardsGuardParserRuleCall_8_5_0()); 
	    }
		lv_guards_20_0=ruleGuard		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getTransitionRule());
	        }
       		add(
       			$current, 
       			"guards",
        		lv_guards_20_0, 
        		"Guard");
	        afterParserOrEnumRuleCall();
	    }

)
)*)?(	otherlv_21='with' 
    {
    	newLeafNode(otherlv_21, grammarAccess.getTransitionAccess().getWithKeyword_9_0());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getTransitionAccess().getWitnessesWitnessParserRuleCall_9_1_0()); 
	    }
		lv_witnesses_22_0=ruleWitness		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getTransitionRule());
	        }
       		add(
       			$current, 
       			"witnesses",
        		lv_witnesses_22_0, 
        		"Witness");
	        afterParserOrEnumRuleCall();
	    }

)
)(
(
		{ 
	        newCompositeNode(grammarAccess.getTransitionAccess().getWitnessesWitnessParserRuleCall_9_2_0()); 
	    }
		lv_witnesses_23_0=ruleWitness		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getTransitionRule());
	        }
       		add(
       			$current, 
       			"witnesses",
        		lv_witnesses_23_0, 
        		"Witness");
	        afterParserOrEnumRuleCall();
	    }

)
)*)?(	otherlv_24='then' 
    {
    	newLeafNode(otherlv_24, grammarAccess.getTransitionAccess().getThenKeyword_10_0());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getTransitionAccess().getActionsActionParserRuleCall_10_1_0()); 
	    }
		lv_actions_25_0=ruleAction		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getTransitionRule());
	        }
       		add(
       			$current, 
       			"actions",
        		lv_actions_25_0, 
        		"Action");
	        afterParserOrEnumRuleCall();
	    }

)
)(
(
		{ 
	        newCompositeNode(grammarAccess.getTransitionAccess().getActionsActionParserRuleCall_10_2_0()); 
	    }
		lv_actions_26_0=ruleAction		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getTransitionRule());
	        }
       		add(
       			$current, 
       			"actions",
        		lv_actions_26_0, 
        		"Action");
	        afterParserOrEnumRuleCall();
	    }

)
)*)?	otherlv_27='end' 
    {
    	newLeafNode(otherlv_27, grammarAccess.getTransitionAccess().getEndKeyword_11());
    }
)
;







// Entry rule entryRuleState
entryRuleState returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getStateRule()); }
	 iv_ruleState=ruleState 
	 { $current=$iv_ruleState.current; } 
	 EOF 
;

// Rule State
ruleState returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
    {
        $current = forceCreateModelElement(
            grammarAccess.getStateAccess().getStateAction_0(),
            $current);
    }
)	otherlv_1='State' 
    {
    	newLeafNode(otherlv_1, grammarAccess.getStateAccess().getStateKeyword_1());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getStateAccess().getNameEStringParserRuleCall_2_0()); 
	    }
		lv_name_2_0=ruleEString		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getStateRule());
	        }
       		set(
       			$current, 
       			"name",
        		lv_name_2_0, 
        		"EString");
	        afterParserOrEnumRuleCall();
	    }

)
)(	otherlv_3='refines' 
    {
    	newLeafNode(otherlv_3, grammarAccess.getStateAccess().getRefinesKeyword_3_0());
    }
(
(
		{
			if ($current==null) {
	            $current = createModelElement(grammarAccess.getStateRule());
	        }
        }
		{ 
	        newCompositeNode(grammarAccess.getStateAccess().getRefinesStateCrossReference_3_1_0()); 
	    }
		ruleEString		{ 
	        afterParserOrEnumRuleCall();
	    }

)
))?(	otherlv_5='statemachines' 
    {
    	newLeafNode(otherlv_5, grammarAccess.getStateAccess().getStatemachinesKeyword_4_0());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getStateAccess().getStatemachinesStatemachineParserRuleCall_4_1_0()); 
	    }
		lv_statemachines_6_0=ruleStatemachine		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getStateRule());
	        }
       		add(
       			$current, 
       			"statemachines",
        		lv_statemachines_6_0, 
        		"Statemachine");
	        afterParserOrEnumRuleCall();
	    }

)
)(
(
		{ 
	        newCompositeNode(grammarAccess.getStateAccess().getStatemachinesStatemachineParserRuleCall_4_2_0()); 
	    }
		lv_statemachines_7_0=ruleStatemachine		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getStateRule());
	        }
       		add(
       			$current, 
       			"statemachines",
        		lv_statemachines_7_0, 
        		"Statemachine");
	        afterParserOrEnumRuleCall();
	    }

)
)*	otherlv_8='end' 
    {
    	newLeafNode(otherlv_8, grammarAccess.getStateAccess().getEndKeyword_4_3());
    }
)?(	otherlv_9='invariants' 
    {
    	newLeafNode(otherlv_9, grammarAccess.getStateAccess().getInvariantsKeyword_5_0());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getStateAccess().getInvariantsInvariantParserRuleCall_5_1_0()); 
	    }
		lv_invariants_10_0=ruleInvariant		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getStateRule());
	        }
       		add(
       			$current, 
       			"invariants",
        		lv_invariants_10_0, 
        		"Invariant");
	        afterParserOrEnumRuleCall();
	    }

)
)(
(
		{ 
	        newCompositeNode(grammarAccess.getStateAccess().getInvariantsInvariantParserRuleCall_5_2_0()); 
	    }
		lv_invariants_11_0=ruleInvariant		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getStateRule());
	        }
       		add(
       			$current, 
       			"invariants",
        		lv_invariants_11_0, 
        		"Invariant");
	        afterParserOrEnumRuleCall();
	    }

)
)*)?)
;





// Entry rule entryRuleInitial
entryRuleInitial returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getInitialRule()); }
	 iv_ruleInitial=ruleInitial 
	 { $current=$iv_ruleInitial.current; } 
	 EOF 
;

// Rule Initial
ruleInitial returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
    {
        $current = forceCreateModelElement(
            grammarAccess.getInitialAccess().getInitialAction_0(),
            $current);
    }
)	otherlv_1='Initial' 
    {
    	newLeafNode(otherlv_1, grammarAccess.getInitialAccess().getInitialKeyword_1());
    }
(	otherlv_2='internalId' 
    {
    	newLeafNode(otherlv_2, grammarAccess.getInitialAccess().getInternalIdKeyword_2_0());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getInitialAccess().getInternalIdEStringParserRuleCall_2_1_0()); 
	    }
		lv_internalId_3_0=ruleEString		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getInitialRule());
	        }
       		set(
       			$current, 
       			"internalId",
        		lv_internalId_3_0, 
        		"EString");
	        afterParserOrEnumRuleCall();
	    }

)
))?)
;





// Entry rule entryRuleFinal
entryRuleFinal returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getFinalRule()); }
	 iv_ruleFinal=ruleFinal 
	 { $current=$iv_ruleFinal.current; } 
	 EOF 
;

// Rule Final
ruleFinal returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
    {
        $current = forceCreateModelElement(
            grammarAccess.getFinalAccess().getFinalAction_0(),
            $current);
    }
)	otherlv_1='Final' 
    {
    	newLeafNode(otherlv_1, grammarAccess.getFinalAccess().getFinalKeyword_1());
    }
(	otherlv_2='internalId' 
    {
    	newLeafNode(otherlv_2, grammarAccess.getFinalAccess().getInternalIdKeyword_2_0());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getFinalAccess().getInternalIdEStringParserRuleCall_2_1_0()); 
	    }
		lv_internalId_3_0=ruleEString		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getFinalRule());
	        }
       		set(
       			$current, 
       			"internalId",
        		lv_internalId_3_0, 
        		"EString");
	        afterParserOrEnumRuleCall();
	    }

)
))?)
;





// Entry rule entryRuleAny
entryRuleAny returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getAnyRule()); }
	 iv_ruleAny=ruleAny 
	 { $current=$iv_ruleAny.current; } 
	 EOF 
;

// Rule Any
ruleAny returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
    {
        $current = forceCreateModelElement(
            grammarAccess.getAnyAccess().getAnyAction_0(),
            $current);
    }
)	otherlv_1='Any' 
    {
    	newLeafNode(otherlv_1, grammarAccess.getAnyAccess().getAnyKeyword_1());
    }
(	otherlv_2='internalId' 
    {
    	newLeafNode(otherlv_2, grammarAccess.getAnyAccess().getInternalIdKeyword_2_0());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getAnyAccess().getInternalIdEStringParserRuleCall_2_1_0()); 
	    }
		lv_internalId_3_0=ruleEString		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getAnyRule());
	        }
       		set(
       			$current, 
       			"internalId",
        		lv_internalId_3_0, 
        		"EString");
	        afterParserOrEnumRuleCall();
	    }

)
))?)
;





// Entry rule entryRuleJunction
entryRuleJunction returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getJunctionRule()); }
	 iv_ruleJunction=ruleJunction 
	 { $current=$iv_ruleJunction.current; } 
	 EOF 
;

// Rule Junction
ruleJunction returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
    {
        $current = forceCreateModelElement(
            grammarAccess.getJunctionAccess().getJunctionAction_0(),
            $current);
    }
)	otherlv_1='Junction' 
    {
    	newLeafNode(otherlv_1, grammarAccess.getJunctionAccess().getJunctionKeyword_1());
    }
(	otherlv_2='internalId' 
    {
    	newLeafNode(otherlv_2, grammarAccess.getJunctionAccess().getInternalIdKeyword_2_0());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getJunctionAccess().getInternalIdEStringParserRuleCall_2_1_0()); 
	    }
		lv_internalId_3_0=ruleEString		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getJunctionRule());
	        }
       		set(
       			$current, 
       			"internalId",
        		lv_internalId_3_0, 
        		"EString");
	        afterParserOrEnumRuleCall();
	    }

)
))?)
;





// Entry rule entryRuleFork
entryRuleFork returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getForkRule()); }
	 iv_ruleFork=ruleFork 
	 { $current=$iv_ruleFork.current; } 
	 EOF 
;

// Rule Fork
ruleFork returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
    {
        $current = forceCreateModelElement(
            grammarAccess.getForkAccess().getForkAction_0(),
            $current);
    }
)	otherlv_1='Fork' 
    {
    	newLeafNode(otherlv_1, grammarAccess.getForkAccess().getForkKeyword_1());
    }
(	otherlv_2='internalId' 
    {
    	newLeafNode(otherlv_2, grammarAccess.getForkAccess().getInternalIdKeyword_2_0());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getForkAccess().getInternalIdEStringParserRuleCall_2_1_0()); 
	    }
		lv_internalId_3_0=ruleEString		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getForkRule());
	        }
       		set(
       			$current, 
       			"internalId",
        		lv_internalId_3_0, 
        		"EString");
	        afterParserOrEnumRuleCall();
	    }

)
))?)
;





// Entry rule entryRuleInvariant
entryRuleInvariant returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getInvariantRule()); }
	 iv_ruleInvariant=ruleInvariant 
	 { $current=$iv_ruleInvariant.current; } 
	 EOF 
;

// Rule Invariant
ruleInvariant returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(	otherlv_0='Invariant' 
    {
    	newLeafNode(otherlv_0, grammarAccess.getInvariantAccess().getInvariantKeyword_0());
    }
	otherlv_1='@' 
    {
    	newLeafNode(otherlv_1, grammarAccess.getInvariantAccess().getCommercialAtKeyword_1());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getInvariantAccess().getNameEStringParserRuleCall_2_0()); 
	    }
		lv_name_2_0=ruleEString		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getInvariantRule());
	        }
       		set(
       			$current, 
       			"name",
        		lv_name_2_0, 
        		"EString");
	        afterParserOrEnumRuleCall();
	    }

)
)(
(
		lv_theorem_3_0=	'theorem' 
    {
        newLeafNode(lv_theorem_3_0, grammarAccess.getInvariantAccess().getTheoremTheoremKeyword_3_0());
    }
 
	    {
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getInvariantRule());
	        }
       		setWithLastConsumed($current, "theorem", true, "theorem");
	    }

)
)?(
(
		{ 
	        newCompositeNode(grammarAccess.getInvariantAccess().getPredicateEStringParserRuleCall_4_0()); 
	    }
		lv_predicate_4_0=ruleEString		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getInvariantRule());
	        }
       		set(
       			$current, 
       			"predicate",
        		lv_predicate_4_0, 
        		"EString");
	        afterParserOrEnumRuleCall();
	    }

)
)(	otherlv_5='>' 
    {
    	newLeafNode(otherlv_5, grammarAccess.getInvariantAccess().getGreaterThanSignKeyword_5_0());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getInvariantAccess().getCommentEStringParserRuleCall_5_1_0()); 
	    }
		lv_comment_6_0=ruleEString		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getInvariantRule());
	        }
       		set(
       			$current, 
       			"comment",
        		lv_comment_6_0, 
        		"EString");
	        afterParserOrEnumRuleCall();
	    }

)
))?)
;





// Entry rule entryRuleTypedParameter
entryRuleTypedParameter returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getTypedParameterRule()); }
	 iv_ruleTypedParameter=ruleTypedParameter 
	 { $current=$iv_ruleTypedParameter.current; } 
	 EOF 
;

// Rule TypedParameter
ruleTypedParameter returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(	otherlv_0='TypedParameter' 
    {
    	newLeafNode(otherlv_0, grammarAccess.getTypedParameterAccess().getTypedParameterKeyword_0());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getTypedParameterAccess().getNameEStringParserRuleCall_1_0()); 
	    }
		lv_name_1_0=ruleEString		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getTypedParameterRule());
	        }
       		set(
       			$current, 
       			"name",
        		lv_name_1_0, 
        		"EString");
	        afterParserOrEnumRuleCall();
	    }

)
)	otherlv_2='type' 
    {
    	newLeafNode(otherlv_2, grammarAccess.getTypedParameterAccess().getTypeKeyword_2());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getTypedParameterAccess().getTypeEStringParserRuleCall_3_0()); 
	    }
		lv_type_3_0=ruleEString		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getTypedParameterRule());
	        }
       		set(
       			$current, 
       			"type",
        		lv_type_3_0, 
        		"EString");
	        afterParserOrEnumRuleCall();
	    }

)
)(	otherlv_4='>' 
    {
    	newLeafNode(otherlv_4, grammarAccess.getTypedParameterAccess().getGreaterThanSignKeyword_4_0());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getTypedParameterAccess().getCommentEStringParserRuleCall_4_1_0()); 
	    }
		lv_comment_5_0=ruleEString		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getTypedParameterRule());
	        }
       		set(
       			$current, 
       			"comment",
        		lv_comment_5_0, 
        		"EString");
	        afterParserOrEnumRuleCall();
	    }

)
))?)
;





// Entry rule entryRuleGuard
entryRuleGuard returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getGuardRule()); }
	 iv_ruleGuard=ruleGuard 
	 { $current=$iv_ruleGuard.current; } 
	 EOF 
;

// Rule Guard
ruleGuard returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
(
		lv_theorem_0_0=	'theorem' 
    {
        newLeafNode(lv_theorem_0_0, grammarAccess.getGuardAccess().getTheoremTheoremKeyword_0_0());
    }
 
	    {
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getGuardRule());
	        }
       		setWithLastConsumed($current, "theorem", true, "theorem");
	    }

)
)?	otherlv_1='@' 
    {
    	newLeafNode(otherlv_1, grammarAccess.getGuardAccess().getCommercialAtKeyword_1());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getGuardAccess().getNameEStringParserRuleCall_2_0()); 
	    }
		lv_name_2_0=ruleEString		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getGuardRule());
	        }
       		set(
       			$current, 
       			"name",
        		lv_name_2_0, 
        		"EString");
	        afterParserOrEnumRuleCall();
	    }

)
)(
(
		{ 
	        newCompositeNode(grammarAccess.getGuardAccess().getPredicateEStringParserRuleCall_3_0()); 
	    }
		lv_predicate_3_0=ruleEString		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getGuardRule());
	        }
       		set(
       			$current, 
       			"predicate",
        		lv_predicate_3_0, 
        		"EString");
	        afterParserOrEnumRuleCall();
	    }

)
)(	otherlv_4='>' 
    {
    	newLeafNode(otherlv_4, grammarAccess.getGuardAccess().getGreaterThanSignKeyword_4_0());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getGuardAccess().getCommentEStringParserRuleCall_4_1_0()); 
	    }
		lv_comment_5_0=ruleEString		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getGuardRule());
	        }
       		set(
       			$current, 
       			"comment",
        		lv_comment_5_0, 
        		"EString");
	        afterParserOrEnumRuleCall();
	    }

)
))?)
;





// Entry rule entryRuleAction
entryRuleAction returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getActionRule()); }
	 iv_ruleAction=ruleAction 
	 { $current=$iv_ruleAction.current; } 
	 EOF 
;

// Rule Action
ruleAction returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(	otherlv_0='@' 
    {
    	newLeafNode(otherlv_0, grammarAccess.getActionAccess().getCommercialAtKeyword_0());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getActionAccess().getNameEStringParserRuleCall_1_0()); 
	    }
		lv_name_1_0=ruleEString		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getActionRule());
	        }
       		set(
       			$current, 
       			"name",
        		lv_name_1_0, 
        		"EString");
	        afterParserOrEnumRuleCall();
	    }

)
)(
(
		{ 
	        newCompositeNode(grammarAccess.getActionAccess().getActionEStringParserRuleCall_2_0()); 
	    }
		lv_action_2_0=ruleEString		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getActionRule());
	        }
       		set(
       			$current, 
       			"action",
        		lv_action_2_0, 
        		"EString");
	        afterParserOrEnumRuleCall();
	    }

)
)(	otherlv_3='>' 
    {
    	newLeafNode(otherlv_3, grammarAccess.getActionAccess().getGreaterThanSignKeyword_3_0());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getActionAccess().getCommentEStringParserRuleCall_3_1_0()); 
	    }
		lv_comment_4_0=ruleEString		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getActionRule());
	        }
       		set(
       			$current, 
       			"comment",
        		lv_comment_4_0, 
        		"EString");
	        afterParserOrEnumRuleCall();
	    }

)
))?)
;





// Entry rule entryRuleWitness
entryRuleWitness returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getWitnessRule()); }
	 iv_ruleWitness=ruleWitness 
	 { $current=$iv_ruleWitness.current; } 
	 EOF 
;

// Rule Witness
ruleWitness returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(	otherlv_0='Witness' 
    {
    	newLeafNode(otherlv_0, grammarAccess.getWitnessAccess().getWitnessKeyword_0());
    }
	otherlv_1='@' 
    {
    	newLeafNode(otherlv_1, grammarAccess.getWitnessAccess().getCommercialAtKeyword_1());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getWitnessAccess().getNameEStringParserRuleCall_2_0()); 
	    }
		lv_name_2_0=ruleEString		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getWitnessRule());
	        }
       		set(
       			$current, 
       			"name",
        		lv_name_2_0, 
        		"EString");
	        afterParserOrEnumRuleCall();
	    }

)
)(
(
		{ 
	        newCompositeNode(grammarAccess.getWitnessAccess().getPredicateEStringParserRuleCall_3_0()); 
	    }
		lv_predicate_3_0=ruleEString		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getWitnessRule());
	        }
       		set(
       			$current, 
       			"predicate",
        		lv_predicate_3_0, 
        		"EString");
	        afterParserOrEnumRuleCall();
	    }

)
)(	otherlv_4='>' 
    {
    	newLeafNode(otherlv_4, grammarAccess.getWitnessAccess().getGreaterThanSignKeyword_4_0());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getWitnessAccess().getCommentEStringParserRuleCall_4_1_0()); 
	    }
		lv_comment_5_0=ruleEString		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getWitnessRule());
	        }
       		set(
       			$current, 
       			"comment",
        		lv_comment_5_0, 
        		"EString");
	        afterParserOrEnumRuleCall();
	    }

)
))?)
;





// Rule TranslationKind
ruleTranslationKind returns [Enumerator current=null] 
    @init { enterRule(); }
    @after { leaveRule(); }:
((	enumLiteral_0='MULTIVAR' 
	{
        $current = grammarAccess.getTranslationKindAccess().getMULTIVAREnumLiteralDeclaration_0().getEnumLiteral().getInstance();
        newLeafNode(enumLiteral_0, grammarAccess.getTranslationKindAccess().getMULTIVAREnumLiteralDeclaration_0()); 
    }
)
    |(	enumLiteral_1='SINGLEVAR' 
	{
        $current = grammarAccess.getTranslationKindAccess().getSINGLEVAREnumLiteralDeclaration_1().getEnumLiteral().getInstance();
        newLeafNode(enumLiteral_1, grammarAccess.getTranslationKindAccess().getSINGLEVAREnumLiteralDeclaration_1()); 
    }
)
    |(	enumLiteral_2='REFINEDVAR' 
	{
        $current = grammarAccess.getTranslationKindAccess().getREFINEDVAREnumLiteralDeclaration_2().getEnumLiteral().getInstance();
        newLeafNode(enumLiteral_2, grammarAccess.getTranslationKindAccess().getREFINEDVAREnumLiteralDeclaration_2()); 
    }
));



RULE_ID : '^'? ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'_'|'0'..'9')*;

RULE_INT : ('0'..'9')+;

RULE_STRING : ('"' ('\\' ('b'|'t'|'n'|'f'|'r'|'u'|'"'|'\''|'\\')|~(('\\'|'"')))* '"'|'\'' ('\\' ('b'|'t'|'n'|'f'|'r'|'u'|'"'|'\''|'\\')|~(('\\'|'\'')))* '\'');

RULE_ML_COMMENT : '/*' ( options {greedy=false;} : . )*'*/';

RULE_SL_COMMENT : '//' ~(('\n'|'\r'))* ('\r'? '\n')?;

RULE_WS : (' '|'\t'|'\r'|'\n')+;

RULE_ANY_OTHER : .;


