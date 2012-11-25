package org.lazan.t5.stitch.components;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import javax.inject.Inject;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.ContentType;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.internal.services.PageRenderQueue;
import org.apache.tapestry5.internal.services.PageSource;
import org.apache.tapestry5.internal.structure.Page;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.runtime.RenderCommand;
import org.apache.tapestry5.services.MarkupWriterFactory;
import org.apache.tapestry5.services.PartialMarkupRenderer;
import org.apache.tapestry5.services.PartialMarkupRendererFilter;
import org.apache.tapestry5.services.Response;

public class PDFLink {
	@Inject
	private ComponentResources componentResources;

	@Inject
	private PageSource pageSource;

	@Inject
	private PageRenderQueue pageRenderQueue;

	@Inject
	private MarkupWriterFactory markupWriterFactory;

	@Inject
	private PartialMarkupRenderer partialMarkupRenderer;

	/**
	 * This is the link text
	 */
	@Parameter(defaultPrefix = BindingConstants.LITERAL, required = true)
	@Property
	private Block label;

	/**
	 * This {@link Block} renders the FO which in used to generate the PDF
	 */
	@Parameter(required = true)
	private RenderCommand fo;

	/**
	 * If a filename is provided, the browser will prompt the user to save the
	 * PDF with this as a default 
	 */
	@Parameter
	private String fileName;

	public Link getLink() {
		return componentResources.createEventLink("pdf");
	}

	/**
	 * Render the PDF
	 */
	StreamResponse onPdf() {
		return new StreamResponse() {
			public void prepareResponse(Response response) {
				if (fileName != null) {
					response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
				}
			}

			public InputStream getStream() throws IOException {
				String foString = getFoAsString();
				return createPdf(foString);
			}

			public String getContentType() {
				return MimeConstants.MIME_PDF;
			}
		};
	}

	/**
	 * Use Apache FOP to convert the XML FO to a PDF binary stream
	 */
	protected InputStream createPdf(String foString) {
		try {
			FopFactory fopFactory = FopFactory.newInstance();
			ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
			Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, byteBuffer);
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer();
			Result res = new SAXResult(fop.getDefaultHandler());
			Source source = new StreamSource(new StringReader(foString));
			transformer.transform(source, res);
			return new ByteArrayInputStream(byteBuffer.toByteArray());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Run tapestry's template rendering to convert the TML parameter to an XML string
	 */
	protected String getFoAsString() {
		final StringBuilder foBuilder = new StringBuilder();
		String pageName = componentResources.getPageName();
		Page page = pageSource.getPage(pageName);
		pageRenderQueue.setRenderingPage(page);
		pageRenderQueue.addPartialMarkupRendererFilter(new PartialMarkupRendererFilter() {
			public void renderMarkup(MarkupWriter writer, JSONObject reply, PartialMarkupRenderer renderer) {
				Element root = writer.element("partial");
				// ajaxFormUpdateController.setupBeforePartialZoneRender(writer);
				renderer.renderMarkup(writer, reply);
				// ajaxFormUpdateController.cleanupAfterPartialZoneRender();
				writer.end();
				foBuilder.append(root.getChildMarkup().trim());
			}
		});
		pageRenderQueue.addPartialRenderer(fo);

		MarkupWriter markupWriter = markupWriterFactory.newMarkupWriter(new ContentType("text/xml"));
		partialMarkupRenderer.renderMarkup(markupWriter, new JSONObject());
		return foBuilder.toString();
	}
}
