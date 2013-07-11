package org.lazan.t5.stitch.model;

import static org.junit.Assert.assertArrayEquals;

import java.util.Collection;

import org.junit.Test;

public class DefaultPagerModelTest {
	@Test
	public void testGetPages() {
		DefaultPagerModel pagerModel = new DefaultPagerModel(2, 2, 1, 2, 2);

		assertPages(new int[] {1,2,3,4,5,9,10}, pagerModel.getPages(1, 10));
		assertPages(new int[] {1,2,3,4,5,9,10}, pagerModel.getPages(2, 10));
		assertPages(new int[] {1,2,3,4,5,9,10}, pagerModel.getPages(3, 10));
		assertPages(new int[] {1,2,3,4,5,9,10}, pagerModel.getPages(4, 10));
		assertPages(new int[] {1,2,4,5,6,9,10}, pagerModel.getPages(5, 10));
		assertPages(new int[] {1,2,5,6,7,9,10}, pagerModel.getPages(6, 10));
		assertPages(new int[] {1,2,6,7,8,9,10}, pagerModel.getPages(7, 10));
		assertPages(new int[] {1,2,6,7,8,9,10}, pagerModel.getPages(8, 10));
		assertPages(new int[] {1,2,6,7,8,9,10}, pagerModel.getPages(9, 10));
		assertPages(new int[] {1,2,6,7,8,9,10}, pagerModel.getPages(10, 10));
	}
	
	public void assertPages(int[] expected, Collection<Integer> actual) {
		int[] actualArray = new int[actual.size()];
		int index = 0;
		for (Integer current : actual) {
			actualArray[index++] = current;
		}
		assertArrayEquals(expected, actualArray);
	}
}
