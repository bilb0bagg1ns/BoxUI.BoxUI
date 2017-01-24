package com.box.model.domain;

import java.util.ArrayList;

/**
 * Box UI user.
 * 
 * @author mike.prasad
 *
 */

public class GradeListWrapper {

	private ArrayList<GradeComposite> gradeCompositeList;

	public ArrayList<GradeComposite> getGradeCompositeList() {
		return gradeCompositeList;
	}

	public void setGradeCompositeList(ArrayList<GradeComposite> gradeCompositeList) {
		this.gradeCompositeList = gradeCompositeList;
	}
}
