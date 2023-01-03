[![Latest Status](https://github.com/eventB-Soton/EMF_Translator/actions/workflows/codeql-analysis.yml/badge.svg?branch=main)](https://github.com/eventB-Soton/EMF_Translator/actions/workflows/codeql-analysis.yml/badge.svg)

# EMF_Translator
This feature provides a framework for creating EMF model-to-model translators.

Release history:
------------------------------------------------------------------------------
### 4.0.0 ###
- Translator(4.0.0)
  + update compliance to Java 11 + other maintenance
  + when traversing model ignore derived containments
  + add 'before' to translator descriptor to allow more control over position of generated elements
### 3.0.1 ###
- Translator (3.0.1)
  + Bug fix: Avoid infinite recursion when getting previously translated elements
### 3.0.0 ###
- Translator (3.0.0)
  + move 'getAffectedResources' to adapter so it can be specialised by clients
  + move saving of resources to handler level so that it can be specialised by clients
  + support automatic creation of resources when specified by translation rules
  + Provide an un-translate facility
  + Improve status and exception handling
  + Add support for running validation prior to translation
  + Support several translations for same command id
  + Support translation into same file as source
  + Handler no longer converts Rodin elements to Event-B EMF. A new method getEObject can be overridden by clients to do this
        (this removes dependencies on Rodin and Event-B EMF plugins)
  + feature renamed from ac.soton.emf.utilities (version numbering inherited)
  + log exceptions thrown by translator command
### 2.1.1 ### 
- Add release history for 2.1.0 to build
### 2.1.0 ### 
- Translator (2.1.0): programmatic invocation, pre and post processing, adaptor support for defining position in containment
### 2.0.0 ### 
- Translator (2.0.0): more extensible translator extension - ruleset extension
### 1.1.0 ### 
- Translator (1.1.0): make handler more flexible to generalise usage
### 1.0.0 (Initial prototype) ### 
- Translator (1.0.0): fix dialogue problem
