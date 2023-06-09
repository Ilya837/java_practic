
package com.example.kafka_camel_spring_project.generated;

import javax.xml.namespace.QName;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.example.kafka_camel_spring_project.generated package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Donate_QNAME = new QName("/jaxb/gen", "Donate");
    private final static QName _User_QNAME = new QName("/jaxb/gen", "User");
    private final static QName _Status_QNAME = new QName("/jaxb/gen", "Status");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.example.kafka_camel_spring_project.generated
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Donate }
     * 
     */
    public Donate createDonate() {
        return new Donate();
    }

    /**
     * Create an instance of {@link User }
     * 
     */
    public User createUser() {
        return new User();
    }

    /**
     * Create an instance of {@link Status }
     * 
     */
    public Status createStatus() {
        return new Status();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Donate }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Donate }{@code >}
     */
    @XmlElementDecl(namespace = "/jaxb/gen", name = "Donate")
    public JAXBElement<Donate> createDonate(Donate value) {
        return new JAXBElement<Donate>(_Donate_QNAME, Donate.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link User }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link User }{@code >}
     */
    @XmlElementDecl(namespace = "/jaxb/gen", name = "User")
    public JAXBElement<User> createUser(User value) {
        return new JAXBElement<User>(_User_QNAME, User.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Status }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Status }{@code >}
     */
    @XmlElementDecl(namespace = "/jaxb/gen", name = "Status")
    public JAXBElement<Status> createStatus(Status value) {
        return new JAXBElement<Status>(_Status_QNAME, Status.class, null, value);
    }

}
