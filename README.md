For html escaping, uses: Apache Commons Lang (http://commons.apache.org/proper/commons-lang/download_lang.cgi)

GET XMI FILE IN ASTAH:
Tool > XML Input & Output > Save as XML Project

COMPILE WITH (make sure working directory is this):
javac -cp ./commons-lang3-3.3.2.jar cscie97/xmlparser/*.java

RUN WITH (make sure working directory is this):
java -cp ./commons-lang3-3.3.2.jar:. cscie97.xmlparser.Parser <xmi file> <output html file>