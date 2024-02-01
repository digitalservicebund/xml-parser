package de.bund.digitalservice.xml.parser

import org.apache.commons.text.StringEscapeUtils
import org.w3c.dom.Document
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.StringWriter
import javax.xml.XMLConstants
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import javax.xml.xpath.XPath
import javax.xml.xpath.XPathConstants
import javax.xml.xpath.XPathFactory

class XmlDocument(content: ByteArray) {
    private var document: Document
    private val xpathInstance: XPath = XPathFactory.newInstance().newXPath()

    init {
        val factory = DocumentBuilderFactory.newInstance()
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true)
        document = factory.newDocumentBuilder().parse(ByteArrayInputStream(content))
    }

    fun getSimpleElementByXpath(xpath: String): String? {
        val node = xpathInstance.evaluate(xpath, document, XPathConstants.NODE)

        return if (node != null) (node as Node).textContent else null
    }

    fun getArrayElementsByXpath(xpath: String): List<String> {
        val itemsTypeT1 = xpathInstance.evaluate(xpath, document, XPathConstants.NODESET) as NodeList

        val itemList: MutableList<String> = ArrayList()

        for (i in 0 until itemsTypeT1.length) {
            itemList.add(itemsTypeT1.item(i).textContent)
        }

        return itemList
    }

    fun getArrayElementsWithChildrenByXpath(xpath: String): List<List<String>> {
        val itemsTypeT1 = xpathInstance.evaluate(xpath, document, XPathConstants.NODESET) as NodeList
        val itemList: MutableList<MutableList<String>> = ArrayList()
        for (i in 0 until itemsTypeT1.length) {
            val children = itemsTypeT1.item(i).childNodes
            val childrenList: MutableList<String> = ArrayList()
            for (j in 0 until children.length) {
                val node = children.item(j)
                if (node.nodeType == Node.ELEMENT_NODE) {
                    childrenList.add(node.textContent)
                }
            }
            itemList.add(childrenList)
        }
        return itemList
    }

    fun getInnerHtmlByXpath(xpath: String) = getHtmlElementByXpath(xpath)?.replace(Regex("(^<[^>]+>)|(</[^>]+>$)"), "")?.trim()

    fun getHtmlElementByXpath(xpath: String): String? {
        val node = xpathInstance.evaluate(xpath, document, XPathConstants.NODE) ?: return null
        val writer = StringWriter()
        val transformer = TransformerFactory.newInstance().newTransformer()
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes")
        transformer.transform(DOMSource(node as Node), StreamResult(writer))
        return writer.toString()
    }

    fun getArrayElementsHtmlByXpath(xpath: String): List<String> {
        val items = xpathInstance.evaluate(xpath, document, XPathConstants.NODESET) as NodeList

        val itemList: MutableList<String> = ArrayList()

        for (i in 0 until items.length) {
            itemList.add(getInnerHTML(items.item(i)))
        }

        return itemList
    }

    private fun getInnerHTML(node: Node): String {
        val writer = StringWriter()
        val result = StreamResult(writer)
        val transformer: Transformer = TransformerFactory.newInstance().newTransformer()
        transformer.setOutputProperty(OutputKeys.METHOD, "html")
        for (i in 0 until node.childNodes.length) {
            transformer.transform(DOMSource(node.childNodes.item(i)), result)
        }

        return StringEscapeUtils.unescapeHtml4(writer.toString())
    }

    companion object {
        fun fromDocument(document: Document): XmlDocument = XmlDocument(documentToByteArray(document))

        fun documentToByteArray(document: Document): ByteArray {
            val output = ByteArrayOutputStream()
            val result = StreamResult(output)
            TransformerFactory.newInstance().newTransformer().transform(DOMSource(document), result)
            return output.toByteArray()
        }
    }
}
