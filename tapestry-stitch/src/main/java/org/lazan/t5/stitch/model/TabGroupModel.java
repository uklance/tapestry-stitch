package org.lazan.t5.stitch.model;

import org.apache.tapestry5.Block;
 
public interface TabGroupModel {
	void addTab(String name, String label, Block body);
}