package com.box.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Coaching Engine model class. Used in REST based call in CoachingEngineCOntroller
 * 
 * @author mike.prasad
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestBody {

    private String responseCode;
    private String responseMessage;
    private String corrdID;
    
    public TestBody() {
    }

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public String getCorrdID() {
		return corrdID;
	}

	public void setCorrdID(String corrdID) {
		this.corrdID = corrdID;
	}

	@Override
	public String toString() {
		return "TestBody [responseCode=" + responseCode + ", responseMessage=" + responseMessage + ", corrdID="
				+ corrdID + "]";
	}
    
}