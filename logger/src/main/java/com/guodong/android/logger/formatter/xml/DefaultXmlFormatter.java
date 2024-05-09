package com.guodong.android.logger.formatter.xml;

import com.guodong.android.logger.internal.Platform;
import com.guodong.android.logger.internal.SystemCompat;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * Created by guodongAndroid on 2024/5/7 13:42.
 */
public final class DefaultXmlFormatter implements XmlFormatter {

    private static final int XML_INDENT = 4;

    @Override
    public String format(String xml) {
        String formattedString;
        if (xml == null || xml.trim().isEmpty()) {
            Platform.get().warn("XML empty.");
            return "";
        }
        try {
            Source xmlInput = new StreamSource(new StringReader(xml));
            StreamResult xmlOutput = new StreamResult(new StringWriter());
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount",
                    String.valueOf(XML_INDENT));
            transformer.transform(xmlInput, xmlOutput);
            formattedString = xmlOutput.getWriter().toString().replaceFirst(">", ">"
                    + SystemCompat.lineSeparator);
        } catch (Exception e) {
            Platform.get().warn(e.getMessage());
            return xml;
        }
        return formattedString;
    }
}