

startGeneration = {
}

endGeneration = {
}

codeGenerator = { 

	imports = ""

  if ( codeGen.isCodeGenerationNeeded(helper) ) {
	  code = """
${codeGen.generateGettersAndSetters(helper)}
${codeGen.generateContainerContext(helper)}
"""

  } else {
    code = null
  }

}
