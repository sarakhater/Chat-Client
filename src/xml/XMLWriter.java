package xml;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.ProcessingInstruction;
import view.ChatFrame;

public class XMLWriter {

    private static String schema = "<?xml version=\"1.0\"?>\n"
            + "<!--\n"
            + "To change this license header, choose License Headers in Project Properties.\n"
            + "To change this template file, choose Tools | Templates\n"
            + "and open the template in the editor.\n"
            + "-->\n"
            + "\n"
            + "<xs:schema version=\"1.0\"\n"
            + "           xmlns:xs=\"http://www.w3.org/2001/XMLSchema\"\n"
            + "           elementFormDefault=\"qualified\">\n"
            + "    <xs:complexType name=\"msg\">\n"
            + "        <xs:simpleContent>\n"
            + "            <xs:extension base=\"xs:string\">\n"
            + "                <xs:attribute name=\"sender\" type=\"xs:string\"/>\n"
            + "            </xs:extension>\n"
            + "        </xs:simpleContent> \n"
            + "    </xs:complexType>\n"
            + "    <xs:complexType name=\"chat\">\n"
            + "        <xs:sequence>\n"
            + "            <xs:element name=\"message\" type=\"msg\" maxOccurs=\"unbounded\"/>\n"
            + "        </xs:sequence>\n"
            + "    </xs:complexType>\n"
            + "    <xs:element name=\"mychat\" type=\"chat\"/>\n"
            + "\n"
            + "</xs:schema>";

    private static String stylesheet = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<xsl:stylesheet version=\"1.0\"\n"
            + "xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">\n"
            + "\n"
            + "<xsl:template match=\"/\">\n"
            + "  <html>\n"
            + "  <body background=\"##6BD2DB\">\n"
            + "  <h2>My message History</h2>\n"
            + "  <table border=\"2\">\n"
            + "    <tr bgcolor=\"#9acd32\">\n"
            + "      <th>Sender</th>\n"
            + "      <th>Message</th>\n"
            + "    </tr>\n"
            + "    <xsl:for-each select=\"mychat/message\">\n"
            + "    <tr>\n"
            + "      <td><xsl:value-of select=\"attribute::sender\"/></td>\n"
            + "      <td><xsl:value-of select=\".\"/></td>\n"
            + "    </tr>\n"
            + "    </xsl:for-each>\n"
            + "  </table>\n"
            + "  </body>\n"
            + "  </html>\n"
            + "</xsl:template>\n"
            + "\n"
            + "</xsl:stylesheet>";

    private XMLWriter() {
    }

    public static void writeXML(String path, Vector<ChatFrame.Message> messages) {
        try {
            Document doc;
            Element root;
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            try {
                doc = db.parse(path);
                root = doc.getDocumentElement();
            } catch (Exception ex) {
                doc = db.newDocument();
                root = doc.createElement("mychat");
                doc.appendChild(root);
            }

            ProcessingInstruction pi = doc.createProcessingInstruction("xml:stylesheet", "type=\"text/xsl\" href=\"projectStylesheet.xsl\"");
            doc.insertBefore(pi, root);

            for (int i = 0; i < messages.size(); i++) {
                Element message = doc.createElement("message");
                CDATASection content = doc.createCDATASection(messages.elementAt(i).getBody());
                message.setAttribute("sender", messages.elementAt(i).getSender());
                message.appendChild(content);
                root.appendChild(message);
            }

            root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/xmlschema-instance");
            root.setAttribute("xsi:noNamespaceSchemaLocation", "projectSchema.xsd");

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer t = tf.newTransformer();
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            DOMSource ds = new DOMSource(doc);

            String xmlPath = path;
            if (!xmlPath.endsWith(".xml")) {
                xmlPath += ".xml";
            }

            StreamResult sr = new StreamResult(new FileOutputStream(xmlPath));
            t.transform(ds, sr);
        } catch (ParserConfigurationException | FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (TransformerConfigurationException ex) {
            ex.printStackTrace();
        } catch (TransformerException ex) {
            ex.printStackTrace();
        }

        path = path.substring(0, path.lastIndexOf("\\") + 1);
        
        System.out.println(path);
        
        try {
            Files.write(Paths.get(path + "projectSchema.xsd"), schema.getBytes());
            Files.write(Paths.get(path + "projectStylesheet.xsl"), stylesheet.getBytes());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
