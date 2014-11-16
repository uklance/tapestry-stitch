package org.lazan.t5.stitch.services.internal;

import java.util.Map;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.ioc.annotations.UsesMappedConfiguration;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.lazan.t5.stitch.model.Syntax;
import org.lazan.t5.stitch.services.SyntaxSource;

@UsesMappedConfiguration(key=String.class, value=Syntax.class)
public class SyntaxSourceImpl implements SyntaxSource {
	private Map<String, Syntax> syntaxBySuffix;
	
	public SyntaxSourceImpl(Map<String, Syntax> syntaxBySuffix) {
		super();
		this.syntaxBySuffix = CollectionFactory.newCaseInsensitiveMap();
		this.syntaxBySuffix.putAll(syntaxBySuffix);
	}

	public Syntax getSyntax(Asset source) {
		String name = source.getResource().getFile();
		int dotIndex = name.lastIndexOf('.');
		Syntax syntax = null;
		if (dotIndex != -1) {
			String suffix = name.substring(dotIndex + 1);
			syntax = syntaxBySuffix.get(suffix);
		}
		return syntax == null ? Syntax.AUTO_DETECT : syntax;
	}
}
