package org.lazan.t5.stitch.demo.pages;

import org.apache.tapestry5.annotations.PageActivationContext;
import org.apache.tapestry5.annotations.Property;

/**
 * Content copied from wikipedia
 */
public class TabGroupDemo {
	@PageActivationContext
	@Property
	private String fruitTab;

	public String getApplesContent() {
		return 
			"The apple is the pomaceous fruit of the apple tree, species Malus domestica in the rose family (Rosaceae). " + 
			"It is one of the most widely cultivated tree fruits, and the most widely known of the many members of genus " + 
			"Malus that are used by humans. Apples grow on small, deciduous trees. The tree originated in Western Asia, " + 
			"where its wild ancestor, Malus sieversii, is still found today. Apples have been grown for thousands of years " + 
			"in Asia and Europe, and were brought to North America by European colonists. Apples have been present in the " + 
			"mythology and religions of many cultures, including Norse, Greek and Christian traditions. In 2010, the fruit's " + 
			"genome was decoded, leading to new understandings of disease control and selective breeding in apple production.";
	}

	public String getOrangesContent() {
		return 
			"The orange (specifically, the sweet orange) is the fruit of the citrus Citrus sinensis, species Citrus " + 
			"sinensis in the family Rutaceae. The fruit of the Citrus sinensis is called sweet orange to distinguish it from " + 
			"that of the Citrus aurantium, the bitter orange. The orange is a hybrid, possibly between pomelo (Citrus maxima) " + 
			"and mandarin (Citrus reticulata), cultivated since ancient times. Probably originated in Southeast Asia, oranges " + 
			"were already cultivated in China as far back as 2500 BC. Between the late 15th century and the beginnings of the " + 
			"16th century, Italian and Portuguese merchants brought orange trees in the Mediterranean area. The Spanish " + 
			"introduced the sweet orange to the American continent in the mid 1500s.";
	}

	public String getPearsContent() {
		return 
			"The pear is native to coastal and mildly temperate regions of the Old World, from western Europe and " + 
			"north Africa east right across Asia. It is a medium-sized tree, reaching 10-17 m tall, often with a tall, " + 
			"narrow crown; a few species are shrubby. The leaves are alternately arranged, simple, 2-12 cm long, glossy " + 
			"green on some species, densely silvery-hairy in some others; leaf shape varies from broad oval to narrow " + 
			"lanceolate. Most pears are deciduous, but one or two species in southeast Asia are evergreen. Most are " + 
			"cold-hardy, withstanding temperatures between -25C and -40C in winter, except for the evergreen species, " + 
			"which only tolerate temperatures down to about -15C. The flowers are white, rarely tinted yellow or pink, " + 
			"2-4 cm diameter, and have five petals. Like that of the related apple, the pear fruit is a pome, in most " + 
			"wild species 1-4 cm diameter, but in some cultivated forms up to 18 cm long and 8 cm broad; the shape " + 
			"varies in most species from oblate or globose, to the classic pyriform 'pear-shape' of the European pear with " + 
			"an elongated basal portion and a bulbous end.";
	}
}
