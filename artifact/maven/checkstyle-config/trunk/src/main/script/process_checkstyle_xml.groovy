/*
this script removes the suppression filter module from checkstyle configurations, as maven configures its differently.

<module>
  ...
  <module name="SuppressionFilter">
    <property name="file" value="${config_loc}/checkstyle_suppression.xml"/>
  </module>
</module>
*/

def checkstyleXmlPath = project.properties['checkstyleXmlPath'] 
def targetXmlFilePath = project.properties['targetXmlFilePath']

println("[INFO] Removing SuppressionFilter module from '$checkstyleXmlPath'.")
def checkstyleXml = new XmlParser().parse(checkstyleXmlPath)

println("AAA 1; checkstyleXml='$checkstyleXml'")
def rootModules = checkstyleXml.children()
println("AAA 2")
def suppressionModule = rootModules.find { it.@name == 'SuppressionFilter' } 
println("AAA 3")
rootModules.remove(suppressionModule)

println("[INFO] Writing result to '$targetXmlFilePath'.")
new File(targetXmlFilePath).getParentFile().mkdirs();
def pWriter = new PrintWriter(new FileWriter(targetXmlFilePath))
pWriter.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
pWriter.println("<!DOCTYPE module PUBLIC \"-//Puppy Crawl//DTD Check Configuration 1.3//EN\" \"http://www.puppycrawl.com/dtds/configuration_1_3.dtd\">")
new XmlNodePrinter(pWriter).print(checkstyleXml)
