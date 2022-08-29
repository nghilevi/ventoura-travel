package com.Mindelo.Ventoura.Entity;

import java.util.ArrayList;
import java.util.List;

public class GuideProfileTagsEntity {

	private List<String> tagsOfEachRow;

	public GuideProfileTagsEntity() {
		tagsOfEachRow = new ArrayList<>();
	}

	public void addTags(String tag) {
		tagsOfEachRow.add(tag);
	}

	public List<String> getTagsOfEachRow() {
		return tagsOfEachRow;
	}

}
