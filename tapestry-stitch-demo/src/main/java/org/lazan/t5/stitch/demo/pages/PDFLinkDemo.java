package org.lazan.t5.stitch.demo.pages;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.services.Response;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.DefaultKeyedValues;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

public class PDFLinkDemo {
	@Property
	private CountryStats stats;
	
	@Inject
	private ComponentResources componentResources;

	public static class CountryStats {
		public int rank;
		public String country;
		public int population;

		public CountryStats(int rank, String country, int population) {
			super();
			this.rank = rank;
			this.country = country;
			this.population = population;
		}
	}

	StreamResponse onChart() {
		DefaultKeyedValues values = new DefaultKeyedValues();
		for (CountryStats current : getCountryStats()) {
			values.addValue(current.country, current.population);
		}
		PieDataset pieDataset = new DefaultPieDataset(values);

		PiePlot3D plot = new PiePlot3D(pieDataset);
		plot.setForegroundAlpha(0.7f);
		plot.setDepthFactor(0.1);
		plot.setCircular(true);

		final JFreeChart chart = new JFreeChart(plot);
		chart.removeLegend();
		chart.setTitle("Top 10 Country Populations");

		return new StreamResponse() {
			public String getContentType() {
				return "image/png";
			}

			public InputStream getStream() throws IOException {
				BufferedImage image = chart.createBufferedImage(400, 370);
				ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
				ChartUtilities.writeBufferedImageAsPNG(byteArray, image);
				return new ByteArrayInputStream(byteArray.toByteArray());
			}

			public void prepareResponse(Response response) {
			}
		};
	}

	public String getChartURI() {
		return componentResources.createEventLink("chart").toAbsoluteURI();
	}

	/**
	 * Data from http://en.wikipedia.org/wiki/List_of_countries_by_population
	 * 
	 * @return
	 */
	@Cached
	public CountryStats[] getCountryStats() {
		return new CountryStats[] { 
			new CountryStats(1, "China", 1347350000), 
			new CountryStats(2, "India", 1210193422),
			new CountryStats(3, "United States", 314814000), 
			new CountryStats(4, "Indonesia", 237641326),
			new CountryStats(5, "Brazil", 193946886), 
			new CountryStats(6, "Pakistan", 181301000),
			new CountryStats(7, "Nigeria", 166629000), 
			new CountryStats(8, "Bangladesh", 152518015),
			new CountryStats(9, "Russia", 143228300), 
			new CountryStats(10, "Japan", 127530000)
		};
	}
}
