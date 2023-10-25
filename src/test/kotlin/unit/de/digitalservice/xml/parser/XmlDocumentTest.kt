package unit.de.digitalservice.xml.parser

import de.bund.digitalservice.xml.parser.XmlDocument
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.xml.sax.SAXParseException

class XmlDocumentTest {
    val document =
        """
        <?xml version="1.0"?>
        <catalog>
           <book id="bk101">
              <author>Gambardella, Matthew</author>
              <title>XML Developer's <br/> Guide</title>
              <genre>Computer</genre>
              <price>44.95</price>
              <publish_date>2000-10-01</publish_date>
              <description>An in-depth look at creating applications 
              with XML.</description>
           </book>
           <book id="bk102">
              <author>Ralls, Kim</author>
              <title>Midnight Rain</title>
              <genre>Fantasy</genre>
              <price>5.95</price>
              <publish_date>2000-12-16</publish_date>
              <description>A former architect battles corporate zombies, 
              an evil sorceress, and her own childhood to become queen 
              of the world.</description>
           </book>
        </catalog>
        """.trimIndent()

    @Test
    fun `it can extracts html code as string from a node`() {
        val xml = XmlDocument(document.toByteArray())
        assertThat(xml.getInnerHtmlByXpath("catalog/book/title")).isEqualTo("XML Developer's <br/> Guide")
    }

    @Test
    fun `it can extracts only text string from a node`() {
        val xml = XmlDocument(document.toByteArray())
        assertThat(xml.getSimpleElementByXpath("catalog/book/title")).isEqualTo("XML Developer's  Guide")
    }

    @Test
    fun `it can extracts an array of elements if multiple are found`() {
        val xml = XmlDocument(document.toByteArray())
        assertThat(xml.getArrayElementsByXpath("catalog/book/title")).isEqualTo(listOf("XML Developer's  Guide", "Midnight Rain"))
    }

    @Test
    fun `it can extracts an array of elements with html if multiple are found`() {
        val xml = XmlDocument(document.toByteArray())
        assertThat(xml.getArrayElementsHtmlByXpath("catalog/book/title")).isEqualTo(listOf("XML Developer's <br> Guide", "Midnight Rain"))
    }

    @Test
    fun `it fails to parse input with some injections to be security`() {
        val content =
            """
            <?xml version="1.0"?><!DOCTYPE foo [ <!ENTITY passwords SYSTEM "file:///etc/passwd"> ]>
            <catalog>&passwords;</catalog>
            """.trimIndent()

        assertThatThrownBy { XmlDocument(content.toByteArray()) }.isInstanceOf(SAXParseException::class.java)
    }
}
