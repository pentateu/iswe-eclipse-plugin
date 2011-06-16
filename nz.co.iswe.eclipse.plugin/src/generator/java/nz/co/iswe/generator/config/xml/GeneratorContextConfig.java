//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.05.14 at 01:39:19 PM NZST 
//


package nz.co.iswe.generator.config.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GeneratorContextConfig complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GeneratorContextConfig">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="generatorContext" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="entityInfoFactory" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="groups" type="{http://www.iswe.co.nz/isweframework/xsd/iswe-generator-1.0.0}ListOfGroups"/>
 *         &lt;element name="oneToOne" type="{http://www.iswe.co.nz/isweframework/xsd/iswe-generator-1.0.0}ListOfOneToOneGenerators" minOccurs="0"/>
 *         &lt;element name="manyToOne" type="{http://www.iswe.co.nz/isweframework/xsd/iswe-generator-1.0.0}ListOfManyToOneGenerators" minOccurs="0"/>
 *         &lt;element name="helpers" type="{http://www.iswe.co.nz/isweframework/xsd/iswe-generator-1.0.0}ListOfHelpers" minOccurs="0"/>
 *         &lt;element name="properties" type="{http://www.iswe.co.nz/isweframework/xsd/iswe-generator-1.0.0}ListOfProperties"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GeneratorContextConfig", propOrder = {
    "generatorContext",
    "entityInfoFactory",
    "groups",
    "oneToOne",
    "manyToOne",
    "helpers",
    "properties"
})
public class GeneratorContextConfig {

    protected String generatorContext;
    protected String entityInfoFactory;
    @XmlElement(required = true)
    protected ListOfGroups groups;
    protected ListOfOneToOneGenerators oneToOne;
    protected ListOfManyToOneGenerators manyToOne;
    protected ListOfHelpers helpers;
    @XmlElement(required = true)
    protected ListOfProperties properties;

    /**
     * Gets the value of the generatorContext property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGeneratorContext() {
        return generatorContext;
    }

    /**
     * Sets the value of the generatorContext property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGeneratorContext(String value) {
        this.generatorContext = value;
    }

    /**
     * Gets the value of the entityInfoFactory property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntityInfoFactory() {
        return entityInfoFactory;
    }

    /**
     * Sets the value of the entityInfoFactory property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntityInfoFactory(String value) {
        this.entityInfoFactory = value;
    }

    /**
     * Gets the value of the groups property.
     * 
     * @return
     *     possible object is
     *     {@link ListOfGroups }
     *     
     */
    public ListOfGroups getGroups() {
        return groups;
    }

    /**
     * Sets the value of the groups property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListOfGroups }
     *     
     */
    public void setGroups(ListOfGroups value) {
        this.groups = value;
    }

    /**
     * Gets the value of the oneToOne property.
     * 
     * @return
     *     possible object is
     *     {@link ListOfOneToOneGenerators }
     *     
     */
    public ListOfOneToOneGenerators getOneToOne() {
        return oneToOne;
    }

    /**
     * Sets the value of the oneToOne property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListOfOneToOneGenerators }
     *     
     */
    public void setOneToOne(ListOfOneToOneGenerators value) {
        this.oneToOne = value;
    }

    /**
     * Gets the value of the manyToOne property.
     * 
     * @return
     *     possible object is
     *     {@link ListOfManyToOneGenerators }
     *     
     */
    public ListOfManyToOneGenerators getManyToOne() {
        return manyToOne;
    }

    /**
     * Sets the value of the manyToOne property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListOfManyToOneGenerators }
     *     
     */
    public void setManyToOne(ListOfManyToOneGenerators value) {
        this.manyToOne = value;
    }

    /**
     * Gets the value of the helpers property.
     * 
     * @return
     *     possible object is
     *     {@link ListOfHelpers }
     *     
     */
    public ListOfHelpers getHelpers() {
        return helpers;
    }

    /**
     * Sets the value of the helpers property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListOfHelpers }
     *     
     */
    public void setHelpers(ListOfHelpers value) {
        this.helpers = value;
    }

    /**
     * Gets the value of the properties property.
     * 
     * @return
     *     possible object is
     *     {@link ListOfProperties }
     *     
     */
    public ListOfProperties getProperties() {
        return properties;
    }

    /**
     * Sets the value of the properties property.
     * 
     * @param value
     *     allowed object is
     *     {@link ListOfProperties }
     *     
     */
    public void setProperties(ListOfProperties value) {
        this.properties = value;
    }

}
