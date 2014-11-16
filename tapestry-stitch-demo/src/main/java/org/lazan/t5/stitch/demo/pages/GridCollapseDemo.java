package org.lazan.t5.stitch.demo.pages;

import org.apache.tapestry5.annotations.Property;

public class GridCollapseDemo {
	@Property
	private Country country;
	
	@Property
	private City city;
	
	public Country[] getCountries() {
		return new Country[] {
			new Country("Australia", 
				new City("Sydney",
						new Street("Street 1", 1000), new Street("Street 2", 2000), new Street("Street 3", 3000)
				),
				new City("Melbourne",
						new Street("Street 4", 4000), new Street("Street 5", 5000), new Street("Street 6", 6000)
				),
				new City("Perth",
						new Street("Street 7", 7000)
				)
			),
			new Country("America", 
				new City("New York",
						new Street("Street 8", 8000), new Street("Street 9", 9000)
				),
				new City("Las Vegas",
						new Street("Street 10", 10000), new Street("Street 11", 11000), new Street("Street 12", 12000)
				)
			),
			new Country("UK", 
				new City("London",
						new Street("Street 13", 13000), new Street("Street 14", 14000)
				)
			)
		};
	}

	public static class Street {
		public String streetName;
		public long streetPopulation;
		
		public Street(String streetName, long population) {
			super();
			this.streetName = streetName;
			this.streetPopulation = population;
		}
	}
	
	public static class City {
		public String cityName;
		public Street[] streets;
		public long getCityPopulation() {
			long total = 0;
			for (Street street : streets) {
				total += street.streetPopulation;
			}
			return total;
		}
		public City(String cityName, Street... streets) {
			super();
			this.cityName = cityName;
			this.streets = streets;
		}
		
	}
	
	public static class Country {
		public String countryName;
		public City[] cities;
		public long getCountryPopulation() {
			long total = 0;
			for (City city : cities) {
				total += city.getCityPopulation();
			}
			return total;
		}
		public Country(String countryName, City... cities) {
			super();
			this.countryName = countryName;
			this.cities = cities;
		}
		
	}
}
