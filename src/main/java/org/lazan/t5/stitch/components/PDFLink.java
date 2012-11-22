package org.lazan.t5.stitch.components;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.ContentType;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.dom.Document;
import org.apache.tapestry5.internal.services.HeartbeatImpl;
import org.apache.tapestry5.internal.services.RenderQueueImpl;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.runtime.RenderCommand;
import org.apache.tapestry5.services.Environment;
import org.apache.tapestry5.services.Heartbeat;
import org.apache.tapestry5.services.MarkupWriterFactory;
import org.apache.tapestry5.services.Response;
import org.slf4j.Logger;

public class PDFLink {
	@Inject
	private ComponentResources componentResources;

	@Inject
	private MarkupWriterFactory markupWriterFactory;
	
	@Inject
	private Logger logger;
	
	@Inject
	private Environment environment;
	
	@Inject
	private TypeCoercer typeCoercer;

	@Parameter(defaultPrefix = BindingConstants.LITERAL, required=true)
	@Property
	private Block label;

	@Parameter(defaultPrefix = BindingConstants.LITERAL, value = "literal:UTF-8")
	private String charset;

	@Property
	private String context;

	public Link getPdfLink() {
		Object[] contextArray = context == null ? new Object[0] : new Object[] { context };
		return componentResources.createEventLink("generatePdf", contextArray);
	}

	StreamResponse onGeneratePdf(EventContext eventContext) throws FOPException, TransformerException {
		String fo = getBodyAsString(eventContext);
		final byte[] pdf = createPdf(fo);
		
		return new StreamResponse() {
			public void prepareResponse(Response response) {
			}
			
			public InputStream getStream() throws IOException {
				return new ByteArrayInputStream(pdf);
			}
			
			public String getContentType() {
				return MimeConstants.MIME_PDF;
			}
		};
	}

	private byte[] createPdf(String fo) throws FOPException, TransformerException {
		FopFactory fopFactory = FopFactory.newInstance();
		ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
		Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, byteBuffer);
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer();
		Result res = new SAXResult(fop.getDefaultHandler());
		Source source = new StreamSource(new StringReader(fo));
		transformer.transform(source, res);
		return byteBuffer.toByteArray();
	}

	private String getBodyAsString(EventContext eventContext) {
		environment.push(Heartbeat.class, new HeartbeatImpl());
		if (eventContext.getCount() > 0) {
			context = eventContext.get(String.class, 0);
		}
		ContentType foContentType = new ContentType("text/xml", charset);
		MarkupWriter markupWriter = markupWriterFactory.newPartialMarkupWriter(foContentType);
		RenderQueueImpl renderQueue = new RenderQueueImpl(logger);
		RenderCommand renderCommand = typeCoercer.coerce(componentResources.getBody(), RenderCommand.class);
		renderQueue.push(renderCommand);
		renderQueue.run(markupWriter);
		StringWriter stringWriter = new StringWriter();
		PrintWriter foWriter = new PrintWriter(stringWriter);
		Map<String, String> namespaceURIToPrefix = new HashMap<String, String>();
		Document document = markupWriter.getDocument();
		document.toMarkup(document, foWriter, namespaceURIToPrefix);
		return stringWriter.toString();
	}
}
